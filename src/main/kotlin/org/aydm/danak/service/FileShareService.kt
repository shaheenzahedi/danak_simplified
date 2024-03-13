package org.aydm.danak.service

import org.aydm.danak.service.dto.FileShareDTO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

/**
 * Service Interface for managing [org.aydm.danak.domain.FileShare].
 */
interface FileShareService {

    /**
     * Save a fileShare.
     *
     * @param fileShareDTO the entity to save.
     * @return the persisted entity.
     */
    fun save(fileShareDTO: FileShareDTO): FileShareDTO

    /**
     * Updates a fileShare.
     *
     * @param fileShareDTO the entity to update.
     * @return the persisted entity.
     */
    fun update(fileShareDTO: FileShareDTO): FileShareDTO

    /**
     * Partially updates a fileShare.
     *
     * @param fileShareDTO the entity to update partially.
     * @return the persisted entity.
     */
    fun partialUpdate(fileShareDTO: FileShareDTO): Optional<FileShareDTO>

    /**
     * Get all the fileShares.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    fun findAll(pageable: Pageable): Page<FileShareDTO>

    /**
     * Get the "id" fileShare.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findOne(id: Long): Optional<FileShareDTO>

    /**
     * Delete the "id" fileShare.
     *
     * @param id the id of the entity.
     */
    fun delete(id: Long)
}
