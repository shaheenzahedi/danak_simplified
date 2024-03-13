package org.aydm.danak.repository

import org.aydm.danak.domain.PanelLog
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

/**
 * Spring Data JPA repository for the PanelLog entity.
 */
@Suppress("unused")
@Repository
interface PanelLogRepository : JpaRepository<PanelLog, Long>, JpaSpecificationExecutor<PanelLog> {

    @Query("select panelLog from PanelLog panelLog where panelLog.user.login = ?#{principal.username}")
    fun findByUserIsCurrentUser(): MutableList<PanelLog>
}
