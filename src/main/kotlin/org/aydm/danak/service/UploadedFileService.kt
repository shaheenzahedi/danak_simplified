package org.aydm.danak.service

import org.aydm.danak.service.dto.UploadedFileDTO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

/**
 * Service Interface for managing [org.aydm.danak.domain.UploadedFile].
 */
interface UploadedFileService {

    /**
     * Save a uploadedFile.
     *
     * @param uploadedFileDTO the entity to save.
     * @return the persisted entity.
     */
    fun save(uploadedFileDTO: UploadedFileDTO): UploadedFileDTO

    /**
     * Updates a uploadedFile.
     *
     * @param uploadedFileDTO the entity to update.
     * @return the persisted entity.
     */
    fun update(uploadedFileDTO: UploadedFileDTO): UploadedFileDTO

    /**
     * Partially updates a uploadedFile.
     *
     * @param uploadedFileDTO the entity to update partially.
     * @return the persisted entity.
     */
    fun partialUpdate(uploadedFileDTO: UploadedFileDTO): Optional<UploadedFileDTO>

    /**
     * Get all the uploadedFiles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    fun findAll(pageable: Pageable): Page<UploadedFileDTO>

    /**
     * Get the "id" uploadedFile.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findOne(id: Long): Optional<UploadedFileDTO>

    /**
     * Delete the "id" uploadedFile.
     *
     * @param id the id of the entity.
     */
    fun delete(id: Long)
}
