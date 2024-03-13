package org.aydm.danak.service

import org.aydm.danak.service.dto.CenterWatchListDTO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

/**
 * Service Interface for managing [org.aydm.danak.domain.CenterWatchList].
 */
interface CenterWatchListService {

    /**
     * Save a centerWatchList.
     *
     * @param centerWatchListDTO the entity to save.
     * @return the persisted entity.
     */
    fun save(centerWatchListDTO: CenterWatchListDTO): CenterWatchListDTO

    /**
     * Updates a centerWatchList.
     *
     * @param centerWatchListDTO the entity to update.
     * @return the persisted entity.
     */
    fun update(centerWatchListDTO: CenterWatchListDTO): CenterWatchListDTO

    /**
     * Partially updates a centerWatchList.
     *
     * @param centerWatchListDTO the entity to update partially.
     * @return the persisted entity.
     */
    fun partialUpdate(centerWatchListDTO: CenterWatchListDTO): Optional<CenterWatchListDTO>

    /**
     * Get all the centerWatchLists.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    fun findAll(pageable: Pageable): Page<CenterWatchListDTO>

    /**
     * Get the "id" centerWatchList.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findOne(id: Long): Optional<CenterWatchListDTO>

    /**
     * Delete the "id" centerWatchList.
     *
     * @param id the id of the entity.
     */
    fun delete(id: Long)
}
