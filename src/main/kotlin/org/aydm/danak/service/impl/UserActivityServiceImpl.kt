package org.aydm.danak.service.impl

import org.aydm.danak.domain.Tablet
import org.aydm.danak.domain.TabletUser
import org.aydm.danak.domain.UserActivity
import org.aydm.danak.repository.UserActivityRepository
import org.aydm.danak.service.UserActivityService
import org.aydm.danak.service.dto.*
import org.aydm.danak.service.mapper.UserActivityMapper
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.web.danak.service.dto.SubmitDTO
import org.web.danak.service.dto.SubmitUserDTO
import java.time.Instant
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

        return userActivityRepository.findById(userActivityDTO.id)
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

            val activities = decideActivities(tabletUser.id, user.activity, tabletUser)
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
        userId: Long?,
        inputActivities: List<SubmitActivityDTO>,
        tabletUser: TabletUserDTO
    ): List<UserActivityDTO> {
        val existingActivities =
            userId?.let { userActivityRepository.findAllByActivityId(it).map(userActivityMapper::toDto) }
        return inputActivities.map { inputActivity ->
            val foundActivities =
                existingActivities?.filter { it.uniqueName == inputActivity.uniqueName }?.toMutableList()
            val userActivityDTO = UserActivityDTO.fromSubmitActivity(inputActivity, tabletUser)
            val activityWithMaxTotal =
                foundActivities?.apply { add(userActivityDTO) }?.maxByOrNull { it.completed ?: 0 }
            save(activityWithMaxTotal?.apply { if (id != userActivityDTO.id) updateTimeStamp = Instant.now() }
                ?: userActivityDTO)
        }
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
        pageable: Pageable?
    ): Page<OverallUserActivities?>? {
        return getUserData(userActivityRepository.getAllActivityByUserPageable(search, pageable))
    }

    fun getUserData(results: Page<Array<Any?>?>?): Page<OverallUserActivities?>? {
        return results?.filterNotNull()?.map { result ->
            val tabletUser = result[0] as TabletUser
            val tablet = result[1] as Tablet
            OverallUserActivities(
                tabletUserId = tabletUser.id,
                firstName = tabletUser.firstName,
                lastName = tabletUser.lastName,
                tabletName = tablet.name,
                tabletId = tablet.id,
                tabletIdentifier = tablet.identifier,
                userActivities = tabletUser.userActivities?.map {
                    AggregatedUserActivity(
                        userActivityId = it.activity?.id,
                        displayListName = it.listName,
                        listName = it.uniqueName,
                        totals = it.total,
                        completes = it.completed
                    )
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
                userActivities = activityDTOs?.toMutableList()
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
