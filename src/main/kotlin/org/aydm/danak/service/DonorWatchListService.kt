package org.aydm.danak.service

import org.aydm.danak.service.dto.DonorWatchListDTO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

/**
 * Service Interface for managing [org.aydm.danak.domain.DonorWatchList].
 */
interface DonorWatchListService {

    /**
     * Save a donorWatchList.
     *
     * @param donorWatchListDTO the entity to save.
     * @return the persisted entity.
     */
    fun save(donorWatchListDTO: DonorWatchListDTO): DonorWatchListDTO

    /**
     * Updates a donorWatchList.
     *
     * @param donorWatchListDTO the entity to update.
     * @return the persisted entity.
     */
    fun update(donorWatchListDTO: DonorWatchListDTO): DonorWatchListDTO

    /**
     * Partially updates a donorWatchList.
     *
     * @param donorWatchListDTO the entity to update partially.
     * @return the persisted entity.
     */
    fun partialUpdate(donorWatchListDTO: DonorWatchListDTO): Optional<DonorWatchListDTO>

    /**
     * Get all the donorWatchLists.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    fun findAll(pageable: Pageable): Page<DonorWatchListDTO>

    /**
     * Get the "id" donorWatchList.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findOne(id: Long): Optional<DonorWatchListDTO>

    /**
     * Delete the "id" donorWatchList.
     *
     * @param id the id of the entity.
     */
    fun delete(id: Long)
}
