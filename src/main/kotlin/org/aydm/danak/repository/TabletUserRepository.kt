package org.aydm.danak.repository

import org.aydm.danak.domain.TabletUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Spring Data SQL repository for the [TabletUser] entity.
 */
@Suppress("unused")
@Repository
interface TabletUserRepository : JpaRepository<TabletUser, Long>
