package org.aydm.danak.repository

import org.aydm.danak.domain.TabletUserImage
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

/**
 * Spring Data JPA repository for the TabletUserImage entity.
 */
@Suppress("unused")
@Repository
interface TabletUserImageRepository : JpaRepository<TabletUserImage, Long>, JpaSpecificationExecutor<TabletUserImage>
