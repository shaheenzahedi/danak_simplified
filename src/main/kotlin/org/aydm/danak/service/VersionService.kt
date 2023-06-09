package org.aydm.danak.service
import org.aydm.danak.service.dto.VersionDTO
import java.util.Optional

/**
 * Service Interface for managing [org.aydm.danak.domain.Version].
 */
interface VersionService {

    /**
     * Save a version.
     *
     * @param versionDTO the entity to save.
     * @return the persisted entity.
     */
    fun save(versionDTO: VersionDTO): VersionDTO

    /**
     * Updates a version.
     *
     * @param versionDTO the entity to update.
     * @return the persisted entity.
     */
    fun update(versionDTO: VersionDTO): VersionDTO

    /**
     * Partially updates a version.
     *
     * @param versionDTO the entity to update partially.
     * @return the persisted entity.
     */
    fun partialUpdate(versionDTO: VersionDTO): Optional<VersionDTO>

    /**
     * Get all the versions.
     *
     * @return the list of entities.
     */
    fun findAll(): MutableList<VersionDTO>

    /**
     * Get the "id" version.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findOne(id: Long): Optional<VersionDTO>

    /**
     * Delete the "id" version.
     *
     * @param id the id of the entity.
     */
    fun delete(id: Long)
    fun findIdByVersion(fromVersion: Int): Long
    fun saveOrGet(dto: VersionDTO): VersionDTO
}
