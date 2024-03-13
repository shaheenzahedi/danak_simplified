package org.aydm.danak.service

import org.aydm.danak.service.dto.TabletLogDTO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

/**
 * Service Interface for managing [org.aydm.danak.domain.TabletLog].
 */
interface TabletLogService {

    /**
     * Save a tabletLog.
     *
     * @param tabletLogDTO the entity to save.
     * @return the persisted entity.
     */
    fun save(tabletLogDTO: TabletLogDTO): TabletLogDTO

    /**
     * Updates a tabletLog.
     *
     * @param tabletLogDTO the entity to update.
     * @return the persisted entity.
     */
    fun update(tabletLogDTO: TabletLogDTO): TabletLogDTO

    /**
     * Partially updates a tabletLog.
     *
     * @param tabletLogDTO the entity to update partially.
     * @return the persisted entity.
     */
    fun partialUpdate(tabletLogDTO: TabletLogDTO): Optional<TabletLogDTO>

    /**
     * Get all the tabletLogs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    fun findAll(pageable: Pageable): Page<TabletLogDTO>

    /**
     * Get the "id" tabletLog.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findOne(id: Long): Optional<TabletLogDTO>

    /**
     * Delete the "id" tabletLog.
     *
     * @param id the id of the entity.
     */
    fun delete(id: Long)
}
