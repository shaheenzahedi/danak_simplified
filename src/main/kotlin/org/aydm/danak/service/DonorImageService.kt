package org.aydm.danak.service

import org.aydm.danak.service.dto.DonorImageDTO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

/**
 * Service Interface for managing [org.aydm.danak.domain.DonorImage].
 */
interface DonorImageService {

    /**
     * Save a donorImage.
     *
     * @param donorImageDTO the entity to save.
     * @return the persisted entity.
     */
    fun save(donorImageDTO: DonorImageDTO): DonorImageDTO

    /**
     * Updates a donorImage.
     *
     * @param donorImageDTO the entity to update.
     * @return the persisted entity.
     */
    fun update(donorImageDTO: DonorImageDTO): DonorImageDTO

    /**
     * Partially updates a donorImage.
     *
     * @param donorImageDTO the entity to update partially.
     * @return the persisted entity.
     */
    fun partialUpdate(donorImageDTO: DonorImageDTO): Optional<DonorImageDTO>

    /**
     * Get all the donorImages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    fun findAll(pageable: Pageable): Page<DonorImageDTO>

    /**
     * Get the "id" donorImage.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findOne(id: Long): Optional<DonorImageDTO>

    /**
     * Delete the "id" donorImage.
     *
     * @param id the id of the entity.
     */
    fun delete(id: Long)
}
