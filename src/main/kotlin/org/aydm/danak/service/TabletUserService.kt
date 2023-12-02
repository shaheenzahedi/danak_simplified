package org.aydm.danak.service
import org.aydm.danak.service.dto.TabletUserDTO
import java.util.Optional

/**
 * Service Interface for managing [org.aydm.danak.domain.TabletUser].
 */
interface TabletUserService {

    /**
     * Save a tabletUser.
     *
     * @param tabletUserDTO the entity to save.
     * @return the persisted entity.
     */
    fun save(tabletUserDTO: TabletUserDTO): TabletUserDTO

    /**
     * Updates a tabletUser.
     *
     * @param tabletUserDTO the entity to update.
     * @return the persisted entity.
     */
    fun update(tabletUserDTO: TabletUserDTO): TabletUserDTO

    /**
     * Partially updates a tabletUser.
     *
     * @param tabletUserDTO the entity to update partially.
     * @return the persisted entity.
     */
    fun partialUpdate(tabletUserDTO: TabletUserDTO): Optional<TabletUserDTO>

    /**
     * Get all the tabletUsers.
     *
     * @return the list of entities.
     */
    fun findAll(): MutableList<TabletUserDTO>
    fun findAllByFirstLastNameImplicit(): MutableList<TabletUserDTO>

    /**
     * Get the "id" tabletUser.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findOne(id: Long): Optional<TabletUserDTO>

    /**
     * Delete the "id" tabletUser.
     *
     * @param id the id of the entity.
     */
    fun delete(id: Long)

    fun createSave(userToUpdate: TabletUserDTO): TabletUserDTO
}
