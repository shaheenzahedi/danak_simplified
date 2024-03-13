package org.aydm.danak.web.rest

import org.aydm.danak.repository.DonorImageRepository
import org.aydm.danak.service.DonorImageQueryService
import org.aydm.danak.service.DonorImageService
import org.aydm.danak.service.criteria.DonorImageCriteria
import org.aydm.danak.service.dto.DonorImageDTO
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

private const val ENTITY_NAME = "donorImage"

/**
 * REST controller for managing [org.aydm.danak.domain.DonorImage].
 */
@RestController
@RequestMapping("/api")
class DonorImageResource(
    private val donorImageService: DonorImageService,
    private val donorImageRepository: DonorImageRepository,
    private val donorImageQueryService: DonorImageQueryService,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    companion object {
        const val ENTITY_NAME = "donorImage"
    }

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /donor-images` : Create a new donorImage.
     *
     * @param donorImageDTO the donorImageDTO to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new donorImageDTO, or with status `400 (Bad Request)` if the donorImage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/donor-images")
    fun createDonorImage(@RequestBody donorImageDTO: DonorImageDTO): ResponseEntity<DonorImageDTO> {
        log.debug("REST request to save DonorImage : $donorImageDTO")
        if (donorImageDTO.id != null) {
            throw BadRequestAlertException(
                "A new donorImage cannot already have an ID",
                ENTITY_NAME, "idexists"
            )
        }
        val result = donorImageService.save(donorImageDTO)
        return ResponseEntity.created(URI("/api/donor-images/${result.id}"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * {@code PUT  /donor-images/:id} : Updates an existing donorImage.
     *
     * @param id the id of the donorImageDTO to save.
     * @param donorImageDTO the donorImageDTO to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated donorImageDTO,
     * or with status `400 (Bad Request)` if the donorImageDTO is not valid,
     * or with status `500 (Internal Server Error)` if the donorImageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/donor-images/{id}")
    fun updateDonorImage(
        @PathVariable(value = "id", required = false) id: Long,
        @RequestBody donorImageDTO: DonorImageDTO
    ): ResponseEntity<DonorImageDTO> {
        log.debug("REST request to update DonorImage : {}, {}", id, donorImageDTO)
        if (donorImageDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }

        if (!Objects.equals(id, donorImageDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!donorImageRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = donorImageService.update(donorImageDTO)
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName, true, ENTITY_NAME,
                    donorImageDTO.id.toString()
                )
            )
            .body(result)
    }

    /**
     * {@code PATCH  /donor-images/:id} : Partial updates given fields of an existing donorImage, field will ignore if it is null
     *
     * @param id the id of the donorImageDTO to save.
     * @param donorImageDTO the donorImageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated donorImageDTO,
     * or with status {@code 400 (Bad Request)} if the donorImageDTO is not valid,
     * or with status {@code 404 (Not Found)} if the donorImageDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the donorImageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = ["/donor-images/{id}"], consumes = ["application/json", "application/merge-patch+json"])
    @Throws(URISyntaxException::class)
    fun partialUpdateDonorImage(
        @PathVariable(value = "id", required = false) id: Long,
        @RequestBody donorImageDTO: DonorImageDTO
    ): ResponseEntity<DonorImageDTO> {
        log.debug("REST request to partial update DonorImage partially : {}, {}", id, donorImageDTO)
        if (donorImageDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        if (!Objects.equals(id, donorImageDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!donorImageRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = donorImageService.partialUpdate(donorImageDTO)

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, donorImageDTO.id.toString())
        )
    }

    /**
     * `GET  /donor-images` : get all the donorImages.
     *
     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the [ResponseEntity] with status `200 (OK)` and the list of donorImages in body.
     */
    @GetMapping("/donor-images")
    fun getAllDonorImages(
        criteria: DonorImageCriteria,
        @org.springdoc.api.annotations.ParameterObject pageable: Pageable

    ): ResponseEntity<MutableList<DonorImageDTO>> {
        log.debug("REST request to get DonorImages by criteria: $criteria")
        val page = donorImageQueryService.findByCriteria(criteria, pageable)
        val headers =
            PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page)
        return ResponseEntity.ok().headers(headers).body(page.content)
    }

    /**
     * `GET  /donor-images/count}` : count all the donorImages.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the [ResponseEntity] with status `200 (OK)` and the count in body.
     */
    @GetMapping("/donor-images/count")
    fun countDonorImages(criteria: DonorImageCriteria): ResponseEntity<Long> {
        log.debug("REST request to count DonorImages by criteria: $criteria")
        return ResponseEntity.ok().body(donorImageQueryService.countByCriteria(criteria))
    }

    /**
     * `GET  /donor-images/:id` : get the "id" donorImage.
     *
     * @param id the id of the donorImageDTO to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the donorImageDTO, or with status `404 (Not Found)`.
     */
    @GetMapping("/donor-images/{id}")
    fun getDonorImage(@PathVariable id: Long): ResponseEntity<DonorImageDTO> {
        log.debug("REST request to get DonorImage : $id")
        val donorImageDTO = donorImageService.findOne(id)
        return ResponseUtil.wrapOrNotFound(donorImageDTO)
    }

    /**
     *  `DELETE  /donor-images/:id` : delete the "id" donorImage.
     *
     * @param id the id of the donorImageDTO to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/donor-images/{id}")
    fun deleteDonorImage(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete DonorImage : $id")

        donorImageService.delete(id)
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build()
    }
}
