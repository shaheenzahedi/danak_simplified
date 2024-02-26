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
import org.springframework.data.domain.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.web.danak.service.dto.SubmitDTO
import org.web.danak.service.dto.SubmitUserDTO
import java.time.*
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
    private val tabletUserServiceImpl: TabletUserServiceImpl
) : UserActivityService {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun save(userActivityDTO: UserActivityDTO): UserActivityDTO {
        log.debug("Request to save UserActivity : $userActivityDTO")
        var userActivity = userActivityMapper.toEntity(userActivityDTO)
        userActivity = userActivityRepository.save(userActivity)
        return userActivityMapper.toDto(userActivity)
    }

    override fun update(userActivityDTO: UserActivityDTO): UserActivityDTO {
        log.debug("Request to save UserActivity : {}", userActivityDTO)
        var userActivity = userActivityMapper.toEntity(userActivityDTO)
        userActivity = userActivityRepository.save(userActivity)
        return userActivityMapper.toDto(userActivity)
    }

    override fun partialUpdate(userActivityDTO: UserActivityDTO): Optional<UserActivityDTO> {
        log.debug("Request to partially update UserActivity : {}", userActivityDTO)

        return userActivityRepository.findById(userActivityDTO.id!!)
            .map {
                userActivityMapper.partialUpdate(it, userActivityDTO)
                it
            }
            .map { userActivityRepository.save(it) }
            .map { userActivityMapper.toDto(it) }
    }

    @Transactional(readOnly = true)
    override fun findAll(): MutableList<UserActivityDTO> {
        log.debug("Request to get all UserActivities")
        return userActivityRepository.findAll()
            .mapTo(mutableListOf(), userActivityMapper::toDto)
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
                        completed = it.completed
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
        val tablets = tabletServiceImpl.findAll()
        val tabletUsers = tabletUserServiceImpl.findAll()
        val userActivities = userActivityRepository.findAll()
        return tablets.map { tablet ->
            SubmitDTO(
                tablet = tablet.name!!,
                users = tabletUsers.filter { it.tablet?.id == tablet.id }
                    .map { tabletUser ->
                        SubmitUserDTO(
                            firstName = tabletUser.firstName!!,
                            lastName = tabletUser.lastName!!,
                            activity = userActivities.filter { it.activity?.tablet?.id == tabletUser.tablet?.id }
                                .map { userActivity ->
                                    SubmitActivityDTO(
                                        id = userActivity.id,
                                        listName = userActivity.listName,
                                        uniqueName = userActivity.uniqueName,
                                        total = userActivity.total,
                                        completed = userActivity.total
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
            )
        )
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
                        val latestByDay =
                            group.filter { it.createTimeStamp != null }.maxByOrNull { it.createTimeStamp!! }
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

    fun getUserData(results: Page<Array<Any?>?>?): Page<OverallUserActivities?>? {
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
                center = center?.let { centerMapper.toDto(it) },
                userActivities = tabletUser.userActivities
                    ?.groupBy { it.uniqueName }
                    ?.map { (_, activities) ->
                        var maxActivity: UserActivity? = null
                        var secondMaxActivity: UserActivity? = null

                        for (activity in activities) {
                            if (maxActivity == null || activity.id!! > maxActivity.id!!) {
                                secondMaxActivity = maxActivity
                                maxActivity = activity
                            } else if (secondMaxActivity == null || activity.id!! > secondMaxActivity.id!!) {
                                secondMaxActivity = activity
                            }
                        }

                        userActivityMapper.toDto(maxActivity!!).apply {
                            lastChange = completed!! - (secondMaxActivity?.completed ?: 0)
                        }
                    }
            )
        }?.let { PageImpl(it, results.pageable, results.totalElements) }
    }

    override fun getAllActivityByUser(): List<OverallUserActivities> {
        val tablets = tabletServiceImpl.findAll()
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
                userActivities = null
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
        activity = tabletUser,
        createTimeStamp = Instant.now()
    )
}
