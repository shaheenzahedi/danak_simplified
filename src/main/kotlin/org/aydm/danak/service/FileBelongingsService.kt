package org.aydm.danak.service

import org.aydm.danak.service.dto.FileBelongingsDTO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

/**
 * Service Interface for managing [org.aydm.danak.domain.FileBelongings].
 */
interface FileBelongingsService {

    /**
     * Save a fileBelongings.
     *
     * @param fileBelongingsDTO the entity to save.
     * @return the persisted entity.
     */
    fun save(fileBelongingsDTO: FileBelongingsDTO): FileBelongingsDTO

    /**
     * Updates a fileBelongings.
     *
     * @param fileBelongingsDTO the entity to update.
     * @return the persisted entity.
     */
    fun update(fileBelongingsDTO: FileBelongingsDTO): FileBelongingsDTO

    /**
     * Partially updates a fileBelongings.
     *
     * @param fileBelongingsDTO the entity to update partially.
     * @return the persisted entity.
     */
    fun partialUpdate(fileBelongingsDTO: FileBelongingsDTO): Optional<FileBelongingsDTO>

    /**
     * Get all the fileBelongings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    fun findAll(pageable: Pageable): Page<FileBelongingsDTO>

    /**
     * Get the "id" fileBelongings.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findOne(id: Long): Optional<FileBelongingsDTO>

    /**
     * Delete the "id" fileBelongings.
     *
     * @param id the id of the entity.
     */
    fun delete(id: Long)
    fun saveAll(fileBelongings: MutableList<FileBelongingsDTO>): MutableList<FileBelongingsDTO>
}
