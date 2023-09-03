package org.aydm.danak.repository

import org.aydm.danak.domain.UserActivity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

/**
 * Spring Data SQL repository for the [UserActivity] entity.
 */
@Suppress("unused")
@Repository
interface UserActivityRepository : JpaRepository<UserActivity, Long>, JpaSpecificationExecutor<UserActivity> {
    @Query("SELECT ua FROM UserActivity ua WHERE ua.id IN (SELECT MAX(ua2.id) FROM UserActivity ua2 GROUP BY ua2.uniqueName,ua2.activity.id)")
    fun findAllDistinctActivityIdSummary(): List<UserActivity>?

    @Query(
        "SELECT tu, t FROM TabletUser tu " +
            "JOIN tu.tablet t " +
            "LEFT JOIN tu.userActivities ua " +
            "WHERE (:searchString IS NULL OR " +
            "LOWER(tu.firstName) LIKE CONCAT('%', LOWER(:searchString), '%') OR " +
            "LOWER(tu.lastName) LIKE CONCAT('%', LOWER(:searchString), '%') OR " +
            "LOWER(t.name) LIKE CONCAT('%', LOWER(:searchString), '%')) " +
            "GROUP BY tu.id"
    )
    fun getAllActivityByUserPageable(@Param("searchString") searchString: String?, pageable: Pageable?): Page<Array<Any?>?>?
    fun findAllByActivityId(activityId: Long): List<UserActivity>
}
