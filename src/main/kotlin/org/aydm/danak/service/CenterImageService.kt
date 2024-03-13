package org.aydm.danak.service

import org.aydm.danak.service.dto.CenterImageDTO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

/**
 * Service Interface for managing [org.aydm.danak.domain.CenterImage].
 */
interface CenterImageService {

    /**
     * Save a centerImage.
     *
     * @param centerImageDTO the entity to save.
     * @return the persisted entity.
     */
    fun save(centerImageDTO: CenterImageDTO): CenterImageDTO

    /**
     * Updates a centerImage.
     *
     * @param centerImageDTO the entity to update.
     * @return the persisted entity.
     */
    fun update(centerImageDTO: CenterImageDTO): CenterImageDTO

    /**
     * Partially updates a centerImage.
     *
     * @param centerImageDTO the entity to update partially.
     * @return the persisted entity.
     */
    fun partialUpdate(centerImageDTO: CenterImageDTO): Optional<CenterImageDTO>

    /**
     * Get all the centerImages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    fun findAll(pageable: Pageable): Page<CenterImageDTO>

    /**
     * Get the "id" centerImage.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findOne(id: Long): Optional<CenterImageDTO>

    /**
     * Delete the "id" centerImage.
     *
     * @param id the id of the entity.
     */
    fun delete(id: Long)
}
