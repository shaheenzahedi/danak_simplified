package org.aydm.danak.web.rest

import org.aydm.danak.repository.TabletUserImageRepository
import org.aydm.danak.service.TabletUserImageQueryService
import org.aydm.danak.service.TabletUserImageService
import org.aydm.danak.service.criteria.TabletUserImageCriteria
import org.aydm.danak.service.dto.TabletUserImageDTO
import org.aydm.danak.web.rest.errors.BadRequestAlertException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import tech.jhipster.web.util.HeaderUtil
import tech.jhipster.web.util.PaginationUtil
import tech.jhipster.web.util.ResponseUtil
import java.net.URI
import java.net.URISyntaxException
import java.util.*

private const val ENTITY_NAME = "tabletUserImage"

/**
 * REST controller for managing [org.aydm.danak.domain.TabletUserImage].
 */
@RestController
@RequestMapping("/api")
class TabletUserImageResource(
    private val tabletUserImageService: TabletUserImageService,
    private val tabletUserImageRepository: TabletUserImageRepository,
    private val tabletUserImageQueryService: TabletUserImageQueryService,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    companion object {
        const val ENTITY_NAME = "tabletUserImage"
    }

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /tablet-user-images` : Create a new tabletUserImage.
     *
     * @param tabletUserImageDTO the tabletUserImageDTO to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new tabletUserImageDTO, or with status `400 (Bad Request)` if the tabletUserImage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tablet-user-images")
    fun createTabletUserImage(@RequestBody tabletUserImageDTO: TabletUserImageDTO): ResponseEntity<TabletUserImageDTO> {
        log.debug("REST request to save TabletUserImage : $tabletUserImageDTO")
        if (tabletUserImageDTO.id != null) {
            throw BadRequestAlertException(
                "A new tabletUserImage cannot already have an ID",
                ENTITY_NAME, "idexists"
            )
        }
        val result = tabletUserImageService.save(tabletUserImageDTO)
        return ResponseEntity.created(URI("/api/tablet-user-images/${result.id}"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * {@code PUT  /tablet-user-images/:id} : Updates an existing tabletUserImage.
     *
     * @param id the id of the tabletUserImageDTO to save.
     * @param tabletUserImageDTO the tabletUserImageDTO to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated tabletUserImageDTO,
     * or with status `400 (Bad Request)` if the tabletUserImageDTO is not valid,
     * or with status `500 (Internal Server Error)` if the tabletUserImageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tablet-user-images/{id}")
    fun updateTabletUserImage(
        @PathVariable(value = "id", required = false) id: Long,
        @RequestBody tabletUserImageDTO: TabletUserImageDTO
    ): ResponseEntity<TabletUserImageDTO> {
        log.debug("REST request to update TabletUserImage : {}, {}", id, tabletUserImageDTO)
        if (tabletUserImageDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }

        if (!Objects.equals(id, tabletUserImageDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!tabletUserImageRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = tabletUserImageService.update(tabletUserImageDTO)
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName, true, ENTITY_NAME,
                    tabletUserImageDTO.id.toString()
                )
            )
            .body(result)
    }

    /**
     * {@code PATCH  /tablet-user-images/:id} : Partial updates given fields of an existing tabletUserImage, field will ignore if it is null
     *
     * @param id the id of the tabletUserImageDTO to save.
     * @param tabletUserImageDTO the tabletUserImageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tabletUserImageDTO,
     * or with status {@code 400 (Bad Request)} if the tabletUserImageDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tabletUserImageDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tabletUserImageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = ["/tablet-user-images/{id}"], consumes = ["application/json", "application/merge-patch+json"])
    @Throws(URISyntaxException::class)
    fun partialUpdateTabletUserImage(
        @PathVariable(value = "id", required = false) id: Long,
        @RequestBody tabletUserImageDTO: TabletUserImageDTO
    ): ResponseEntity<TabletUserImageDTO> {
        log.debug("REST request to partial update TabletUserImage partially : {}, {}", id, tabletUserImageDTO)
        if (tabletUserImageDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        if (!Objects.equals(id, tabletUserImageDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!tabletUserImageRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = tabletUserImageService.partialUpdate(tabletUserImageDTO)

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tabletUserImageDTO.id.toString())
        )
    }

    /**
     * `GET  /tablet-user-images` : get all the tabletUserImages.
     *
     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the [ResponseEntity] with status `200 (OK)` and the list of tabletUserImages in body.
     */
    @GetMapping("/tablet-user-images")
    fun getAllTabletUserImages(
        criteria: TabletUserImageCriteria,
        @org.springdoc.api.annotations.ParameterObject pageable: Pageable

    ): ResponseEntity<MutableList<TabletUserImageDTO>> {
        log.debug("REST request to get TabletUserImages by criteria: $criteria")
        val page = tabletUserImageQueryService.findByCriteria(criteria, pageable)
        val headers =
            PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page)
        return ResponseEntity.ok().headers(headers).body(page.content)
    }

    /**
     * `GET  /tablet-user-images/count}` : count all the tabletUserImages.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the [ResponseEntity] with status `200 (OK)` and the count in body.
     */
    @GetMapping("/tablet-user-images/count")
    fun countTabletUserImages(criteria: TabletUserImageCriteria): ResponseEntity<Long> {
        log.debug("REST request to count TabletUserImages by criteria: $criteria")
        return ResponseEntity.ok().body(tabletUserImageQueryService.countByCriteria(criteria))
    }

    /**
     * `GET  /tablet-user-images/:id` : get the "id" tabletUserImage.
     *
     * @param id the id of the tabletUserImageDTO to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the tabletUserImageDTO, or with status `404 (Not Found)`.
     */
    @GetMapping("/tablet-user-images/{id}")
    fun getTabletUserImage(@PathVariable id: Long): ResponseEntity<TabletUserImageDTO> {
        log.debug("REST request to get TabletUserImage : $id")
        val tabletUserImageDTO = tabletUserImageService.findOne(id)
        return ResponseUtil.wrapOrNotFound(tabletUserImageDTO)
    }

    /**
     *  `DELETE  /tablet-user-images/:id` : delete the "id" tabletUserImage.
     *
     * @param id the id of the tabletUserImageDTO to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/tablet-user-images/{id}")
    fun deleteTabletUserImage(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete TabletUserImage : $id")

        tabletUserImageService.delete(id)
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build()
    }
}
