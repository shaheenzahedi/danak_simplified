package org.aydm.danak.repository

import org.aydm.danak.domain.UserActivity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

/**
 * Spring Data SQL repository for the [UserActivity] entity.
 */
@Suppress("unused")
@Repository
interface UserActivityRepository : JpaRepository<UserActivity, Long> {
    @Query("SELECT ua FROM UserActivity ua WHERE ua.id IN (SELECT MAX(ua2.id) FROM UserActivity ua2 GROUP BY ua2.uniqueName,ua2.activity.id)")
    fun findAllDistinctActivityIdSummary(): List<UserActivity>?
}
