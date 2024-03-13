package org.aydm.danak.repository

import org.aydm.danak.domain.CenterImage
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

/**
 * Spring Data JPA repository for the CenterImage entity.
 */
@Suppress("unused")
@Repository
interface CenterImageRepository : JpaRepository<CenterImage, Long>, JpaSpecificationExecutor<CenterImage>
