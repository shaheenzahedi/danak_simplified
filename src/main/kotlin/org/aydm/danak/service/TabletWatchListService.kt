package org.aydm.danak.service

import org.aydm.danak.service.dto.TabletWatchListDTO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

/**
 * Service Interface for managing [org.aydm.danak.domain.TabletWatchList].
 */
interface TabletWatchListService {

    /**
     * Save a tabletWatchList.
     *
     * @param tabletWatchListDTO the entity to save.
     * @return the persisted entity.
     */
    fun save(tabletWatchListDTO: TabletWatchListDTO): TabletWatchListDTO

    /**
     * Updates a tabletWatchList.
     *
     * @param tabletWatchListDTO the entity to update.
     * @return the persisted entity.
     */
    fun update(tabletWatchListDTO: TabletWatchListDTO): TabletWatchListDTO

    /**
     * Partially updates a tabletWatchList.
     *
     * @param tabletWatchListDTO the entity to update partially.
     * @return the persisted entity.
     */
    fun partialUpdate(tabletWatchListDTO: TabletWatchListDTO): Optional<TabletWatchListDTO>

    /**
     * Get all the tabletWatchLists.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    fun findAll(pageable: Pageable): Page<TabletWatchListDTO>

    /**
     * Get the "id" tabletWatchList.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findOne(id: Long): Optional<TabletWatchListDTO>

    /**
     * Delete the "id" tabletWatchList.
     *
     * @param id the id of the entity.
     */
    fun delete(id: Long)
}
