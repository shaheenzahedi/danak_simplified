package org.aydm.danak.repository

import org.aydm.danak.domain.Center
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

/**
 * Spring Data SQL repository for the [Center] entity.
 */
@Suppress("unused")
@Repository
interface CenterRepository : JpaRepository<Center, Long>, JpaSpecificationExecutor<Center>
