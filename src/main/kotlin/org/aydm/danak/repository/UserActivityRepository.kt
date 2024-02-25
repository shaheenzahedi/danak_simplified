package org.aydm.danak.repository

import org.aydm.danak.domain.UserActivity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.Instant

/**
 * Spring Data SQL repository for the [UserActivity] entity.
 */
@Suppress("unused")
@Repository
interface UserActivityRepository : JpaRepository<UserActivity, Long>, JpaSpecificationExecutor<UserActivity> {
    @Query("SELECT ua FROM UserActivity ua WHERE ua.id IN (SELECT MAX(ua2.id) FROM UserActivity ua2 GROUP BY ua2.uniqueName,ua2.activity.id)")
    fun findAllDistinctActivityIdSummary(): List<UserActivity>?

    @Query(
        "SELECT tu, t, c " +
            "FROM TabletUser tu " +
            "JOIN tu.tablet t " +
            "LEFT JOIN tu.tablet.center c " +
            "LEFT JOIN tu.userActivities ua " +
            "WHERE " +
            "   (:searchString IS NULL OR " +
            "   LOWER(tu.firstName) LIKE CONCAT('%', LOWER(:searchString), '%') OR " +
            "   LOWER(tu.lastName) LIKE CONCAT('%', LOWER(:searchString), '%') OR " +
            "   LOWER(t.name) LIKE CONCAT('%', LOWER(:searchString), '%')) " +
            "   AND (:centerId IS NULL OR c.id = :centerId) " +
            "   AND (COALESCE(:tabletIds, NULL) IS NULL OR t.id IN :tabletIds) " +
            "   AND ua.id IN (SELECT ua2.id FROM UserActivity ua2 " +
            "                 WHERE ua2.createTimeStamp BETWEEN :startDay AND :endDay " +
            "                 ORDER BY ua2.id) " +
            "GROUP BY tu.id"
    )
    fun getAllActivityByUserPageable(
        @Param("searchString") searchString: String?,
        @Param("centerId") centerId: Long?,
        @Param("tabletIds") tabletIds: List<Long>,
        @Param("startDay") startDay: Instant,
        @Param("endDay") endDay: Instant,
        pageable: Pageable?
    ): Page<Array<Any?>?>?


    fun findAllByActivityId(activityId: Long): List<UserActivity>
}
