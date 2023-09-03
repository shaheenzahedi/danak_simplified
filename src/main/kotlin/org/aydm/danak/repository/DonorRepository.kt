package org.aydm.danak.repository

import org.aydm.danak.domain.Donor
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

/**
 * Spring Data SQL repository for the [Donor] entity.
 */
@Suppress("unused")
@Repository
interface DonorRepository : JpaRepository<Donor, Long>, JpaSpecificationExecutor<Donor>
