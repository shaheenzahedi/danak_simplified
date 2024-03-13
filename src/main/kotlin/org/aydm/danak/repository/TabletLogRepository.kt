package org.aydm.danak.repository

import org.aydm.danak.domain.TabletLog
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

/**
 * Spring Data JPA repository for the TabletLog entity.
 */
@Suppress("unused")
@Repository
interface TabletLogRepository : JpaRepository<TabletLog, Long>, JpaSpecificationExecutor<TabletLog>
