package org.aydm.danak.repository

import org.aydm.danak.domain.DonorImage
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

/**
 * Spring Data JPA repository for the DonorImage entity.
 */
@Suppress("unused")
@Repository
interface DonorImageRepository : JpaRepository<DonorImage, Long>, JpaSpecificationExecutor<DonorImage>
