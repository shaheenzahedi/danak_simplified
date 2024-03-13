package org.aydm.danak.service

import org.aydm.danak.service.dto.PanelLogDTO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

/**
 * Service Interface for managing [org.aydm.danak.domain.PanelLog].
 */
interface PanelLogService {

    /**
     * Save a panelLog.
     *
     * @param panelLogDTO the entity to save.
     * @return the persisted entity.
     */
    fun save(panelLogDTO: PanelLogDTO): PanelLogDTO

    /**
     * Updates a panelLog.
     *
     * @param panelLogDTO the entity to update.
     * @return the persisted entity.
     */
    fun update(panelLogDTO: PanelLogDTO): PanelLogDTO

    /**
     * Partially updates a panelLog.
     *
     * @param panelLogDTO the entity to update partially.
     * @return the persisted entity.
     */
    fun partialUpdate(panelLogDTO: PanelLogDTO): Optional<PanelLogDTO>

    /**
     * Get all the panelLogs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    fun findAll(pageable: Pageable): Page<PanelLogDTO>

    /**
     * Get the "id" panelLog.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findOne(id: Long): Optional<PanelLogDTO>

    /**
     * Delete the "id" panelLog.
     *
     * @param id the id of the entity.
     */
    fun delete(id: Long)
}
