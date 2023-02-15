package org.aydm.danak.service
import org.aydm.danak.service.dto.OverallUserActivities
import org.aydm.danak.service.dto.UserActivityDTO
import org.springframework.transaction.annotation.Transactional
import org.web.danak.service.dto.SubmitDTO
import java.util.Optional

/**
 * Service Interface for managing [org.aydm.danak.domain.UserActivity].
 */
interface UserActivityService {

    /**
     * Save a userActivity.
     *
     * @param userActivityDTO the entity to save.
     * @return the persisted entity.
     */
    fun save(userActivityDTO: UserActivityDTO): UserActivityDTO

    /**
     * Updates a userActivity.
     *
     * @param userActivityDTO the entity to update.
     * @return the persisted entity.
     */
    fun update(userActivityDTO: UserActivityDTO): UserActivityDTO

    /**
     * Partially updates a userActivity.
     *
     * @param userActivityDTO the entity to update partially.
     * @return the persisted entity.
     */
    fun partialUpdate(userActivityDTO: UserActivityDTO): Optional<UserActivityDTO>

    /**
     * Get all the userActivities.
     *
     * @return the list of entities.
     */
    fun findAll(): MutableList<UserActivityDTO>

    /**
     * Get the "id" userActivity.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findOne(id: Long): Optional<UserActivityDTO>

    /**
     * Delete the "id" userActivity.
     *
     * @param id the id of the entity.
     */
    fun delete(id: Long)

    @Transactional
    fun submit(submitDTO: SubmitDTO): Boolean
    fun getAllActivityByTablet(): List<SubmitDTO>
    fun getAllActivityByUser(): List<OverallUserActivities>
    fun findAllDistinctActivityIdSummary(): Any
}
