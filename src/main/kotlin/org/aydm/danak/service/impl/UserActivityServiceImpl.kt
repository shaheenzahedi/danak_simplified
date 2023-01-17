package org.aydm.danak.service.impl

import org.aydm.danak.domain.UserActivity
import org.aydm.danak.repository.UserActivityRepository
import org.aydm.danak.service.UserActivityService
import org.aydm.danak.service.dto.UserActivityDTO
import org.aydm.danak.service.mapper.UserActivityMapper
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.Optional

/**
 * Service Implementation for managing [UserActivity].
 */
@Service
@Transactional
class UserActivityServiceImpl(
    private val userActivityRepository: UserActivityRepository,
    private val userActivityMapper: UserActivityMapper,
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
}
