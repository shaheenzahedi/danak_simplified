package org.aydm.danak.service.impl

import org.aydm.danak.domain.UserActivity
import org.aydm.danak.repository.UserActivityRepository
import org.aydm.danak.service.UserActivityService
import org.aydm.danak.service.dto.*
import org.aydm.danak.service.mapper.UserActivityMapper
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.web.danak.service.dto.SubmitDTO
import org.web.danak.service.dto.SubmitUserDTO
import java.util.Optional

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
    override fun submit(submitDTO: SubmitDTO): Boolean {
        val tablet = tabletServiceImpl.createSave(submitDTO.tablet)
        submitDTO.users.map { user ->
            val tabletUser = tabletUserServiceImpl.createSave(
                TabletUserDTO(
                    firstName = user.firstName,
                    lastName = user.lastName,
                    tablet = tablet
                )
            )
            user.activity.forEach { activity ->
                save(
                    UserActivityDTO(
                        listName = activity.listName,
                        uniqueName = activity.uniqueName,
                        total = activity.total,
                        completed = activity.completed,
                        activity = tabletUser
                    )
                )
            }
        }
        return true
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

    override fun getAllActivityByUser(): List<OverallUserActivities> {
        val tablets = tabletServiceImpl.findAll()
        val tabletUsers = tabletUserServiceImpl.findAllByFirstLastNameImplicit()
            .filter { tablets.any { tablet-> it.tablet?.id == tablet.id } }
        val userActivities = findAllDistinctActivityIdSummary()
        return tabletUsers.map { tabletUser ->
            val activityDTOs =
                userActivities?.filter { userActivityDTO -> userActivityDTO.tabletUserId == tabletUser.id }
            OverallUserActivities(
                firstName = tabletUser.firstName,
                lastName = tabletUser.lastName,
                tabletName = tablets.first { tablet->tablet.id==tabletUser.tablet?.id }.name,
                userActivities = activityDTOs
            )
        }
    }

    override fun findAllDistinctActivityIdSummary(): List<AggregatedUserActivity>? {
        return userActivityRepository.findAllDistinctActivityIdSummary()
            ?.map {
                AggregatedUserActivity(
                    tabletUserId = it.activity?.id,
                    displayListName = it.listName,
                    listName = it.uniqueName,
                    totals = it.total,
                    completes = it.completed
                )
            }
    }
}
