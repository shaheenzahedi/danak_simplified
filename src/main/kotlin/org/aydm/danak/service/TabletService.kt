package org.aydm.danak.service
import org.aydm.danak.service.dto.TabletDTO
import java.util.Optional

/**
 * Service Interface for managing [org.aydm.danak.domain.Tablet].
 */
interface TabletService {

    /**
     * Save a tablet.
     *
     * @param tabletDTO the entity to save.
     * @return the persisted entity.
     */
    fun save(tabletDTO: TabletDTO): TabletDTO

    /**
     * Updates a tablet.
     *
     * @param tabletDTO the entity to update.
     * @return the persisted entity.
     */
    fun update(tabletDTO: TabletDTO): TabletDTO

    /**
     * Partially updates a tablet.
     *
     * @param tabletDTO the entity to update partially.
     * @return the persisted entity.
     */
    fun partialUpdate(tabletDTO: TabletDTO): Optional<TabletDTO>

    /**
     * Get all the tablets.
     *
     * @return the list of entities.
     */
    fun findAll(): MutableList<TabletDTO>

    /**
     * Get the "id" tablet.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findOne(id: Long): Optional<TabletDTO>

    /**
     * Delete the "id" tablet.
     *
     * @param id the id of the entity.
     */
    fun delete(id: Long)
    fun createSave(tabletName: String): TabletDTO
    fun findAllRegistered(): List<Long>?
}
