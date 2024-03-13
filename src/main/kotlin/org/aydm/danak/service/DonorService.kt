package org.aydm.danak.service

import org.aydm.danak.service.dto.DonorDTO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

/**
 * Service Interface for managing [org.aydm.danak.domain.Donor].
 */
interface DonorService {

    /**
     * Save a donor.
     *
     * @param donorDTO the entity to save.
     * @return the persisted entity.
     */
    fun save(donorDTO: DonorDTO): DonorDTO

    /**
     * Updates a donor.
     *
     * @param donorDTO the entity to update.
     * @return the persisted entity.
     */
    fun update(donorDTO: DonorDTO): DonorDTO

    /**
     * Partially updates a donor.
     *
     * @param donorDTO the entity to update partially.
     * @return the persisted entity.
     */
    fun partialUpdate(donorDTO: DonorDTO): Optional<DonorDTO>

    /**
     * Get all the donors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    fun findAll(pageable: Pageable): Page<DonorDTO>

    /**
     * Get the "id" donor.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findOne(id: Long): Optional<DonorDTO>

    /**
     * Delete the "id" donor.
     *
     * @param id the id of the entity.
     */
    fun delete(id: Long)
}
