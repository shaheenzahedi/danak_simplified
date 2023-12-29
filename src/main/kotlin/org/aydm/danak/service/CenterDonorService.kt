package org.aydm.danak.service
import org.aydm.danak.service.dto.CenterDonorDTO
import java.util.Optional

/**
 * Service Interface for managing [org.aydm.domain.CenterDonor].
 */
interface CenterDonorService {

    /**
     * Save a centerDonor.
     *
     * @param centerDonorDTO the entity to save.
     * @return the persisted entity.
     */
    fun save(centerDonorDTO: CenterDonorDTO): CenterDonorDTO

    /**
     * Updates a centerDonor.
     *
     * @param centerDonorDTO the entity to update.
     * @return the persisted entity.
     */
    fun update(centerDonorDTO: CenterDonorDTO): CenterDonorDTO

    /**
     * Partially updates a centerDonor.
     *
     * @param centerDonorDTO the entity to update partially.
     * @return the persisted entity.
     */
    fun partialUpdate(centerDonorDTO: CenterDonorDTO): Optional<CenterDonorDTO>

    /**
     * Get all the centerDonors.
     *
     * @return the list of entities.
     */
    fun findAll(): MutableList<CenterDonorDTO>

    /**
     * Get the "id" centerDonor.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findOne(id: Long): Optional<CenterDonorDTO>

    /**
     * Delete the "id" centerDonor.
     *
     * @param id the id of the entity.
     */
    fun delete(id: Long)
}
