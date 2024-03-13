package org.aydm.danak.web.rest

import org.aydm.danak.repository.CenterImageRepository
import org.aydm.danak.service.CenterImageQueryService
import org.aydm.danak.service.CenterImageService
import org.aydm.danak.service.criteria.CenterImageCriteria
import org.aydm.danak.service.dto.CenterImageDTO
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

private const val ENTITY_NAME = "centerImage"

/**
 * REST controller for managing [org.aydm.danak.domain.CenterImage].
 */
@RestController
@RequestMapping("/api")
class CenterImageResource(
    private val centerImageService: CenterImageService,
    private val centerImageRepository: CenterImageRepository,
    private val centerImageQueryService: CenterImageQueryService,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    companion object {
        const val ENTITY_NAME = "centerImage"
    }

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /center-images` : Create a new centerImage.
     *
     * @param centerImageDTO the centerImageDTO to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new centerImageDTO, or with status `400 (Bad Request)` if the centerImage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/center-images")
    fun createCenterImage(@RequestBody centerImageDTO: CenterImageDTO): ResponseEntity<CenterImageDTO> {
        log.debug("REST request to save CenterImage : $centerImageDTO")
        if (centerImageDTO.id != null) {
            throw BadRequestAlertException(
                "A new centerImage cannot already have an ID",
                ENTITY_NAME, "idexists"
            )
        }
        val result = centerImageService.save(centerImageDTO)
        return ResponseEntity.created(URI("/api/center-images/${result.id}"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * {@code PUT  /center-images/:id} : Updates an existing centerImage.
     *
     * @param id the id of the centerImageDTO to save.
     * @param centerImageDTO the centerImageDTO to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated centerImageDTO,
     * or with status `400 (Bad Request)` if the centerImageDTO is not valid,
     * or with status `500 (Internal Server Error)` if the centerImageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/center-images/{id}")
    fun updateCenterImage(
        @PathVariable(value = "id", required = false) id: Long,
        @RequestBody centerImageDTO: CenterImageDTO
    ): ResponseEntity<CenterImageDTO> {
        log.debug("REST request to update CenterImage : {}, {}", id, centerImageDTO)
        if (centerImageDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }

        if (!Objects.equals(id, centerImageDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!centerImageRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = centerImageService.update(centerImageDTO)
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName, true, ENTITY_NAME,
                    centerImageDTO.id.toString()
                )
            )
            .body(result)
    }

    /**
     * {@code PATCH  /center-images/:id} : Partial updates given fields of an existing centerImage, field will ignore if it is null
     *
     * @param id the id of the centerImageDTO to save.
     * @param centerImageDTO the centerImageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated centerImageDTO,
     * or with status {@code 400 (Bad Request)} if the centerImageDTO is not valid,
     * or with status {@code 404 (Not Found)} if the centerImageDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the centerImageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = ["/center-images/{id}"], consumes = ["application/json", "application/merge-patch+json"])
    @Throws(URISyntaxException::class)
    fun partialUpdateCenterImage(
        @PathVariable(value = "id", required = false) id: Long,
        @RequestBody centerImageDTO: CenterImageDTO
    ): ResponseEntity<CenterImageDTO> {
        log.debug("REST request to partial update CenterImage partially : {}, {}", id, centerImageDTO)
        if (centerImageDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        if (!Objects.equals(id, centerImageDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!centerImageRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = centerImageService.partialUpdate(centerImageDTO)

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, centerImageDTO.id.toString())
        )
    }

    /**
     * `GET  /center-images` : get all the centerImages.
     *
     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the [ResponseEntity] with status `200 (OK)` and the list of centerImages in body.
     */
    @GetMapping("/center-images")
    fun getAllCenterImages(
        criteria: CenterImageCriteria,
        @org.springdoc.api.annotations.ParameterObject pageable: Pageable

    ): ResponseEntity<MutableList<CenterImageDTO>> {
        log.debug("REST request to get CenterImages by criteria: $criteria")
        val page = centerImageQueryService.findByCriteria(criteria, pageable)
        val headers =
            PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page)
        return ResponseEntity.ok().headers(headers).body(page.content)
    }

    /**
     * `GET  /center-images/count}` : count all the centerImages.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the [ResponseEntity] with status `200 (OK)` and the count in body.
     */
    @GetMapping("/center-images/count")
    fun countCenterImages(criteria: CenterImageCriteria): ResponseEntity<Long> {
        log.debug("REST request to count CenterImages by criteria: $criteria")
        return ResponseEntity.ok().body(centerImageQueryService.countByCriteria(criteria))
    }

    /**
     * `GET  /center-images/:id` : get the "id" centerImage.
     *
     * @param id the id of the centerImageDTO to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the centerImageDTO, or with status `404 (Not Found)`.
     */
    @GetMapping("/center-images/{id}")
    fun getCenterImage(@PathVariable id: Long): ResponseEntity<CenterImageDTO> {
        log.debug("REST request to get CenterImage : $id")
        val centerImageDTO = centerImageService.findOne(id)
        return ResponseUtil.wrapOrNotFound(centerImageDTO)
    }

    /**
     *  `DELETE  /center-images/:id` : delete the "id" centerImage.
     *
     * @param id the id of the centerImageDTO to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/center-images/{id}")
    fun deleteCenterImage(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete CenterImage : $id")

        centerImageService.delete(id)
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build()
    }
}
