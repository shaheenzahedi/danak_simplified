package org.aydm.danak.repository

import org.aydm.danak.domain.UserActivity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Spring Data SQL repository for the [UserActivity] entity.
 */
@Suppress("unused")
@Repository
interface UserActivityRepository : JpaRepository<UserActivity, Long>
