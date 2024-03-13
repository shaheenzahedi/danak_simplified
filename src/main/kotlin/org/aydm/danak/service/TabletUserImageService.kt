package org.aydm.danak.service

import org.aydm.danak.service.dto.TabletUserImageDTO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

/**
 * Service Interface for managing [org.aydm.danak.domain.TabletUserImage].
 */
interface TabletUserImageService {

    /**
     * Save a tabletUserImage.
     *
     * @param tabletUserImageDTO the entity to save.
     * @return the persisted entity.
     */
    fun save(tabletUserImageDTO: TabletUserImageDTO): TabletUserImageDTO

    /**
     * Updates a tabletUserImage.
     *
     * @param tabletUserImageDTO the entity to update.
     * @return the persisted entity.
     */
    fun update(tabletUserImageDTO: TabletUserImageDTO): TabletUserImageDTO

    /**
     * Partially updates a tabletUserImage.
     *
     * @param tabletUserImageDTO the entity to update partially.
     * @return the persisted entity.
     */
    fun partialUpdate(tabletUserImageDTO: TabletUserImageDTO): Optional<TabletUserImageDTO>

    /**
     * Get all the tabletUserImages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    fun findAll(pageable: Pageable): Page<TabletUserImageDTO>

    /**
     * Get the "id" tabletUserImage.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findOne(id: Long): Optional<TabletUserImageDTO>

    /**
     * Delete the "id" tabletUserImage.
     *
     * @param id the id of the entity.
     */
    fun delete(id: Long)
}
