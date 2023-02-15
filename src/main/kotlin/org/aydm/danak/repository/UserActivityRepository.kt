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
    @Query("SELECT ua.activity.id ,ua.uniqueName,SUM(ua.total),SUM(ua.completed) FROM UserActivity ua group by ua.uniqueName,ua.activity.id order by ua.uniqueName")
    fun findAllDistinctActivityIdSummary():List<ArrayList<*>>
}
