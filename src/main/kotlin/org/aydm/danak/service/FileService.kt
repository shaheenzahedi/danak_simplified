package org.aydm.danak.service
import org.aydm.danak.service.dto.FileDTO
import java.util.Optional

/**
 * Service Interface for managing [org.aydm.danak.domain.File].
 */
interface FileService {

    /**
     * Save a file.
     *
     * @param fileDTO the entity to save.
     * @return the persisted entity.
     */
    fun save(fileDTO: FileDTO): FileDTO

    /**
     * Updates a file.
     *
     * @param fileDTO the entity to update.
     * @return the persisted entity.
     */
    fun update(fileDTO: FileDTO): FileDTO

    /**
     * Partially updates a file.
     *
     * @param fileDTO the entity to update partially.
     * @return the persisted entity.
     */
    fun partialUpdate(fileDTO: FileDTO): Optional<FileDTO>

    /**
     * Get all the files.
     *
     * @return the list of entities.
     */
    fun findAll(): MutableList<FileDTO>

    /**
     * Get the "id" file.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findOne(id: Long): Optional<FileDTO>

    /**
     * Delete the "id" file.
     *
     * @param id the id of the entity.
     */
    fun delete(id: Long)
    fun saveAll(files: MutableList<FileDTO>): MutableList<FileDTO>
    fun findAllLastVersion(version: Long): MutableList<FileDTO>
    fun findAllBelongsToVersion(version: Long): MutableList<FileDTO>
    fun findAllUpdates(v1: Long, v2: Long): MutableList<FileDTO>
}
