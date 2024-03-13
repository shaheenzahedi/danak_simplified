package org.aydm.danak.repository

import org.aydm.danak.domain.CenterDonor
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

/**
 * Spring Data JPA repository for the CenterDonor entity.
 */
@Suppress("unused")
@Repository
interface CenterDonorRepository : JpaRepository<CenterDonor, Long>, JpaSpecificationExecutor<CenterDonor>
