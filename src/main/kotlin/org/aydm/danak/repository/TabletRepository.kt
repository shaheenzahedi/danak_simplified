package org.aydm.danak.repository

import org.aydm.danak.domain.Tablet
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Spring Data SQL repository for the [Tablet] entity.
 */
@Suppress("unused")
@Repository
interface TabletRepository : JpaRepository<Tablet, Long>
