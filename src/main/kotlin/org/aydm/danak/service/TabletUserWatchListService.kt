package org.aydm.danak.service

import org.aydm.danak.service.dto.TabletUserWatchListDTO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

/**
 * Service Interface for managing [org.aydm.danak.domain.TabletUserWatchList].
 */
interface TabletUserWatchListService {

    /**
     * Save a tabletUserWatchList.
     *
     * @param tabletUserWatchListDTO the entity to save.
     * @return the persisted entity.
     */
    fun save(tabletUserWatchListDTO: TabletUserWatchListDTO): TabletUserWatchListDTO

    /**
     * Updates a tabletUserWatchList.
     *
     * @param tabletUserWatchListDTO the entity to update.
     * @return the persisted entity.
     */
    fun update(tabletUserWatchListDTO: TabletUserWatchListDTO): TabletUserWatchListDTO

    /**
     * Partially updates a tabletUserWatchList.
     *
     * @param tabletUserWatchListDTO the entity to update partially.
     * @return the persisted entity.
     */
    fun partialUpdate(tabletUserWatchListDTO: TabletUserWatchListDTO): Optional<TabletUserWatchListDTO>

    /**
     * Get all the tabletUserWatchLists.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    fun findAll(pageable: Pageable): Page<TabletUserWatchListDTO>

    /**
     * Get the "id" tabletUserWatchList.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findOne(id: Long): Optional<TabletUserWatchListDTO>

    /**
     * Delete the "id" tabletUserWatchList.
     *
     * @param id the id of the entity.
     */
    fun delete(id: Long)
}
