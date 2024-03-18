package org.aydm.danak.service.impl

import org.aydm.danak.domain.Center
import org.aydm.danak.domain.Tablet
import org.aydm.danak.domain.TabletUser
import org.aydm.danak.domain.UserActivity
import org.aydm.danak.repository.UserActivityRepository
import org.aydm.danak.service.UserActivityService
import org.aydm.danak.service.dto.*
import org.aydm.danak.service.mapper.CenterMapper
import org.aydm.danak.service.mapper.UserActivityMapper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.*
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.web.danak.service.dto.SubmitDTO
import org.web.danak.service.dto.SubmitUserDTO
import java.io.*
import java.nio.charset.Charset
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*

/**
 * Service Implementation for managing [UserActivity].
 */
@Service
@Transactional
class UserActivityServiceImpl(
    private val userActivityRepository: UserActivityRepository,
    private val userActivityMapper: UserActivityMapper,
    private val tabletServiceImpl: TabletServiceImpl,
    private val centerMapper: CenterMapper,
    private val tabletUserServiceImpl: TabletUserServiceImpl,
    @Value("\${asset.apk.base}") private val apkBasePath: String,
) : UserActivityService {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun save(userActivityDTO: UserActivityDTO): UserActivityDTO {
        log.debug("Request to save UserActivity : $userActivityDTO")
        var userActivity = userActivityMapper.toEntity(userActivityDTO)
        userActivity = userActivityRepository.save(userActivity)
        return userActivityMapper.toDto(userActivity)
    }

    override fun update(userActivityDTO: UserActivityDTO): UserActivityDTO {
        log.debug("Request to update UserActivity : {}", userActivityDTO)
        var userActivity = userActivityMapper.toEntity(userActivityDTO)
        userActivity = userActivityRepository.save(userActivity)
        return userActivityMapper.toDto(userActivity)
    }

    override fun partialUpdate(userActivityDTO: UserActivityDTO): Optional<UserActivityDTO> {
        log.debug("Request to partially update UserActivity : {}", userActivityDTO)

        return userActivityRepository.findById(userActivityDTO.id)
            .map {
                userActivityMapper.partialUpdate(it, userActivityDTO)
                it
            }
            .map { userActivityRepository.save(it) }
            .map { userActivityMapper.toDto(it) }
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable): Page<UserActivityDTO> {
        log.debug("Request to get all UserActivities")
        return userActivityRepository.findAll(pageable)
            .map(userActivityMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findOne(id: Long): Optional<UserActivityDTO> {
        log.debug("Request to get UserActivity : $id")
        return userActivityRepository.findById(id)
            .map(userActivityMapper::toDto)
    }

    override fun delete(id: Long) {
        log.debug("Request to delete UserActivity : $id")

        userActivityRepository.deleteById(id)
    }

    @Transactional
    override fun submit(submitDTO: SubmitDTO): SubmitDTO {
        val tablet = tabletServiceImpl.createSave(submitDTO.tablet, submitDTO.tabletId)
        val result = SubmitDTO(tablet.id, tablet.name!!)
        result.users = submitDTO.users.map { user ->
            val tabletUser = tabletUserServiceImpl.createSave(
                TabletUserDTO(
                    id = user.userId,
                    firstName = user.firstName,
                    lastName = user.lastName,
                    tablet = tablet
                )
            )

            val activities = decideActivities(user.activity, tabletUser)
            SubmitUserDTO(
                tabletUser.id,
                tabletUser.firstName!!,
                tabletUser.lastName!!,
                activities.map {
                    SubmitActivityDTO(
                        id = it.id,
                        listName = it.listName,
                        uniqueName = it.uniqueName,
                        total = it.total,
                        completed = it.completed,
                        versionName = it.version,
                    )
                }
            )
        }
        return result
    }

    private fun decideActivities(
        inputActivities: List<SubmitActivityDTO>,
        tabletUser: TabletUserDTO
    ): List<UserActivityDTO> = inputActivities.map { inputActivity ->
        save(
            UserActivityDTO.fromSubmitActivity(inputActivity, tabletUser)
        )
    }

    override fun getAllActivityByTablet(): List<SubmitDTO> {
        val tablets = tabletServiceImpl.findAll(Pageable.unpaged())
        val tabletUsers = tabletUserServiceImpl.findAll(Pageable.unpaged())
        val userActivities = userActivityRepository.findAll(Pageable.unpaged())
        return tablets.content.map { tablet ->
            SubmitDTO(
                tablet = tablet.name!!,
                users = tabletUsers.content.filter { it.tablet?.id == tablet.id }
                    .map { tabletUser ->
                        SubmitUserDTO(
                            firstName = tabletUser.firstName!!,
                            lastName = tabletUser.lastName!!,
                            activity = userActivities.content.filter { it.activity?.tablet?.id == tabletUser.tablet?.id }
                                .map { userActivity ->
                                    SubmitActivityDTO(
                                        id = userActivity.id,
                                        listName = userActivity.listName,
                                        uniqueName = userActivity.uniqueName,
                                        total = userActivity.total,
                                        completed = userActivity.total,
                                        versionName = userActivity.version,
                                    )
                                }
                        )
                    }
            )
        }
    }

    override fun getAllActivityByUserPageable(
        search: String?,
        centerId: Long?,
        donorId: Long?,
        days: Int,
        pageable: Pageable?
    ): Page<OverallUserActivities?>? {
        val tabletIds = tabletServiceImpl.findAllTabletIdsByDonorId(donorId)
        if (tabletIds.isEmpty() && donorId != null) return Page.empty()
        val now = Instant.now().truncatedTo(ChronoUnit.DAYS)
        val startDate = now.minus(days.toLong(), ChronoUnit.DAYS)
        return getUserData(
            userActivityRepository.getAllActivityByUserPageable(
                searchString = search,
                centerId = centerId,
                tabletIds = tabletIds,
                startDay = startDate,
                endDay = now,
                pageable = pageable
            ),
            startDate,
            now
        )
    }

    @Scheduled(cron = "0 0 1 * * *") // Execute at 1 AM every day
    fun midnightCron() {
        cleanUpUserActivity()
    }

    override fun doExcelExport() {
        log.info("Starting Excel export process")

        val earliestTimeStamp = userActivityRepository.findEarliestTimeStamp() ?: run {
            log.warn("Earliest timestamp not found, aborting export")
            return
        }

        val latestTimeStamp = userActivityRepository.findLatestTimeStamp() ?: run {
            log.warn("Latest timestamp not found, aborting export")
            return
        }

        val pageSize = 1000 // Set your desired page size
        var pageNumber = 0

        val todayDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        val fileName = "$todayDate - ${UUID.randomUUID()}.csv"

        while (true) {
            val pageable = PageRequest.of(pageNumber, pageSize)
            val allActivityByUserPageable = userActivityRepository.exportReports(
                startDay = earliestTimeStamp,
                endDay = latestTimeStamp,
                pageable = pageable
            )
            val userDataPage = if (allActivityByUserPageable != null) allActivityByUserPageable else {
                log.warn("No data found for page number $pageNumber, exiting loop")
                continue
            }

            val userData = getUserData(userDataPage)
            log.info("Saving data for page $pageNumber to file: $fileName")
            saveToCSVFile(userData, apkBasePath, fileName, pageNumber == 0)

            if (!userDataPage.hasNext()) {
                break
            }
            pageNumber++
        }

        log.info("Excel export process completed")
    }

    private fun saveToCSVFile(
        userData: Page<OverallUserActivities?>?,
        basePath: String,
        fileName: String,
        isFirstPage: Boolean,
        delimiter: Char = ';'
    ) {
        if (userData == null) {
            log.warn("No user data provided, aborting save")
            return
        }

        val outputFile = File("$basePath/$fileName")

        try {
            BufferedWriter(FileWriter(outputFile, Charset.defaultCharset(), !isFirstPage)).use { writer ->
                // Write headers only for the first page
                if (isFirstPage) {
                    val headers = "شماره یکتای تبلت${delimiter}نام" +
                        "${delimiter}نام خانوادگی" +
                        "${delimiter}شناسه اندروید" +
                        "${delimiter}شناسه تبلت" +
                        "${delimiter}شناسه داخلی تبلت" +
                        "${delimiter}نام مرکز" +
                        "${delimiter}شهر یا روستای مرکز" +
                        "${delimiter}استان مرکز" +
                        "${delimiter}${userData.first()?.getTitle(delimiter)}" +
                        "${delimiter}تاریخ دقیق دریافت گزارش\n"
                    writer.write(headers)
                }

                // Write data
                for (user in userData) {
                    if (user == null) {
                        log.warn("Null user found, skipping")
                        continue
                    }

                    val userDataString = "${user.tabletUserId}" +
                        "$delimiter${user.firstName}" +
                        "$delimiter${user.lastName}" +
                        "$delimiter${user.tabletName}" +
                        "$delimiter${user.tabletId}" +
                        "$delimiter${user.tabletIdentifier}" +
                        "$delimiter${user.center?.name ?: ""}" +
                        "$delimiter${user.center?.city ?: ""}" +
                        "$delimiter${user.center?.country ?: ""}" +
                        "$delimiter${user.getActivitiesAsCSVLine(delimiter)}" +
                        "$delimiter${user.getActivitiesTimeStamp()}\n"
                    writer.write(userDataString)
                }
            }
            log.info("Data saved to file: $outputFile")
        } catch (e: IOException) {
            log.error("Error occurred while saving data to file", e)
        }
    }

    override fun cleanUpUserActivity(): Boolean {
        try {
            var totalRowsProcessed = 0
            var totalRowsDeleted = 0

            val pageSize = 1000
            val earliestTimeStamp = userActivityRepository.findEarliestTimeStamp() ?: return true
            val latestTimeStamp = userActivityRepository.findLatestTimeStamp() ?: return true
            log.info("Starting cleanup process...")
            log.info("Earliest timestamp: $earliestTimeStamp")
            log.info("Latest timestamp: $latestTimeStamp")
            var currentDate = earliestTimeStamp.atZone(ZoneId.systemDefault()).toLocalDate()
            while (!currentDate.isAfter(latestTimeStamp.atZone(ZoneId.systemDefault()).toLocalDate())) {
                val startOfDay = currentDate.atStartOfDay().toInstant(ZoneOffset.UTC)
                val endOfDay = currentDate.plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC)

                log.info("Processing data for date: $currentDate")
                var page = 0
                var hasNextPage = true
                while (hasNextPage) {
                    val pageable = PageRequest.of(page++, pageSize, Sort.by(Sort.Direction.ASC, "createTimeStamp"))
                    val userActivityPage = userActivityRepository.findByCreateTimeStampBetweenAndCreateTimeStampNotNull(
                        startOfDay,
                        endOfDay,
                        pageable
                    )
                    val userActivityData = userActivityPage.content

                    if (userActivityData.isEmpty()) {
                        hasNextPage = false
                        continue
                    }

                    val groups = userActivityData.groupBy { it.activity?.id to it.uniqueName }
                    val rowsToKeep = mutableListOf<UserActivity>()

                    for ((_, group) in groups) {
                        val latestByDay = group.filter { it.createTimeStamp != null }
                            .maxByOrNull { it.createTimeStamp!! }
                        latestByDay?.let { rowsToKeep.add(it) }
                    }

                    val rowsToDelete = userActivityData - rowsToKeep.toSet()
                    userActivityRepository.deleteAll(rowsToDelete)
                    totalRowsProcessed += userActivityData.size
                    totalRowsDeleted += rowsToDelete.size
                    hasNextPage = userActivityPage.hasNext()
                    log.info("Processed ${userActivityData.size} records. Rows deleted: ${rowsToDelete.size}.")
                }

                currentDate = currentDate.plusDays(1)
            }

            log.info("Cleanup completed. Total rows processed: $totalRowsProcessed, Rows deleted: $totalRowsDeleted")
            return true
        } catch (e: Exception) {
            log.error("Error occurred during cleanup: ${e.message}")
            return false
        }
    }

    fun getUserData(results: Page<Array<Any?>?>?, startDate: Instant?=null, now: Instant?=null): Page<OverallUserActivities?>? {
        return results?.filterNotNull()?.map { result ->
            val tabletUser = result[0] as TabletUser
            val tablet = result[1] as Tablet
            val center = result[2] as? Center
            OverallUserActivities(
                tabletUserId = tabletUser.id,
                firstName = tabletUser.firstName,
                lastName = tabletUser.lastName,
                tabletName = tablet.name,
                tabletId = tablet.id,
                tabletIdentifier = tablet.identifier,
                tabletUserDescription = tabletUser.description,
                center = center?.let { centerMapper.toDto(it) },
                userActivities = tabletUser.userActivities
                    ?.groupBy { it.uniqueName }
                    ?.mapNotNull { (_, activities) ->
                        if (activities.isEmpty())return@mapNotNull null
                        val healthyActivities = activities.filter { it.createTimeStamp != null}
                            .filter { it.createTimeStamp!!.isAfter(startDate) }
                        healthyActivities.maxByOrNull { it.id!! }?.let { activity ->
                                val minCompleted = healthyActivities.minByOrNull { it.id ?: 0 }?.completed ?: 0
                                val maxCompleted = healthyActivities.maxByOrNull { it.id ?: 0 }?.completed ?: 0
                                userActivityMapper.toDto(activity)
                                    .apply { lastChange = maxCompleted - minCompleted }
                            }?: userActivityMapper.toDto(healthyActivities.maxByOrNull { it.id!! }!!)
                    }

            )
        }?.let { PageImpl(it, results.pageable, results.totalElements) }
    }

    override fun getAllActivityByUser(): List<OverallUserActivities> {
        val tablets = tabletServiceImpl.findAll(Pageable.unpaged())
        val tabletUsers = tabletUserServiceImpl.findAllByFirstLastNameImplicit()
            .filter { tablets.any { tablet -> it.tablet?.id == tablet.id } }
        val userActivities = userActivityRepository.findAllDistinctActivityIdSummary()
            ?.map {
                AggregatedUserActivity(
                    userActivityId = it.activity?.id,
                    displayListName = it.listName,
                    listName = it.uniqueName,
                    totals = it.total,
                    completes = it.completed
                )
            }
        return tabletUsers.map { tabletUser ->
            val activityDTOs =
                userActivities?.filter { userActivityDTO -> userActivityDTO.userActivityId == tabletUser.id }
            val first = tablets.first { tablet -> tablet.id == tabletUser.tablet?.id }
            OverallUserActivities(
                tabletUserId = tabletUser.id,
                firstName = tabletUser.firstName,
                lastName = tabletUser.lastName,
                tabletName = first.name,
                tabletId = first.id,
                tabletIdentifier = first.identifier,
                center = null,
                userActivities = null,
                tabletUserDescription = null
            )
        }
    }
}

private fun UserActivityDTO.Companion.fromSubmitActivity(
    inputActivity: SubmitActivityDTO,
    tabletUser: TabletUserDTO
): UserActivityDTO {
    return UserActivityDTO(
        listName = inputActivity.listName,
        uniqueName = inputActivity.uniqueName,
        total = inputActivity.total,
        completed = inputActivity.completed,
        version = inputActivity.versionName,
        activity = tabletUser,
        createTimeStamp = Instant.now()
    )
}
