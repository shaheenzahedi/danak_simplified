package org.aydm.danak.service
import org.aydm.danak.service.dto.CenterDTO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.Optional

/**
 * Service Interface for managing [org.aydm.danak.domain.Center].
 */
interface CenterService {

    /**
     * Save a center.
     *
     * @param centerDTO the entity to save.
     * @return the persisted entity.
     */
    fun save(centerDTO: CenterDTO): CenterDTO

    /**
     * Updates a center.
     *
     * @param centerDTO the entity to update.
     * @return the persisted entity.
     */
    fun update(centerDTO: CenterDTO): CenterDTO

    /**
     * Partially updates a center.
     *
     * @param centerDTO the entity to update partially.
     * @return the persisted entity.
     */
    fun partialUpdate(centerDTO: CenterDTO): Optional<CenterDTO>

    /**
     * Get all the centers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    fun findAll(pageable: Pageable): Page<CenterDTO>

    /**
     * Get the "id" center.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findOne(id: Long): Optional<CenterDTO>

    /**
     * Delete the "id" center.
     *
     * @param id the id of the entity.
     */
    fun delete(id: Long)
}
