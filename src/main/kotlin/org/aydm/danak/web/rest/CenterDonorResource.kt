package org.aydm.danak.web.rest

import org.aydm.danak.repository.CenterDonorRepository
import org.aydm.danak.service.CenterDonorService
import org.aydm.danak.service.dto.CenterDonorDTO
import org.aydm.danak.web.rest.errors.BadRequestAlertException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import tech.jhipster.web.util.HeaderUtil
import tech.jhipster.web.util.ResponseUtil
import java.net.URI
import java.net.URISyntaxException
import java.util.Objects

private const val ENTITY_NAME = "centerDonor"
/**
 * REST controller for managing [org.aydm.domain.CenterDonor].
 */
@RestController
@RequestMapping("/api")
class CenterDonorResource(
    private val centerDonorService: CenterDonorService,
    private val centerDonorRepository: CenterDonorRepository,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    companion object {
        const val ENTITY_NAME = "centerDonor"
    }

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /center-donors` : Create a new centerDonor.
     *
     * @param centerDonorDTO the centerDonorDTO to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new centerDonorDTO, or with status `400 (Bad Request)` if the centerDonor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/center-donors")
    fun createCenterDonor(@RequestBody centerDonorDTO: CenterDonorDTO): ResponseEntity<CenterDonorDTO> {
        log.debug("REST request to save CenterDonor : $centerDonorDTO")
        if (centerDonorDTO.id != null) {
            throw BadRequestAlertException(
                "A new centerDonor cannot already have an ID",
                ENTITY_NAME, "idexists"
            )
        }
        val result = centerDonorService.save(centerDonorDTO)
        return ResponseEntity.created(URI("/api/center-donors/${result.id}"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * {@code PUT  /center-donors/:id} : Updates an existing centerDonor.
     *
     * @param id the id of the centerDonorDTO to save.
     * @param centerDonorDTO the centerDonorDTO to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated centerDonorDTO,
     * or with status `400 (Bad Request)` if the centerDonorDTO is not valid,
     * or with status `500 (Internal Server Error)` if the centerDonorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/center-donors/{id}")
    fun updateCenterDonor(
        @PathVariable(value = "id", required = false) id: Long,
        @RequestBody centerDonorDTO: CenterDonorDTO
    ): ResponseEntity<CenterDonorDTO> {
        log.debug("REST request to update CenterDonor : {}, {}", id, centerDonorDTO)
        if (centerDonorDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }

        if (!Objects.equals(id, centerDonorDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!centerDonorRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = centerDonorService.update(centerDonorDTO)
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName, true, ENTITY_NAME,
                    centerDonorDTO.id.toString()
                )
            )
            .body(result)
    }

    /**
     * {@code PATCH  /center-donors/:id} : Partial updates given fields of an existing centerDonor, field will ignore if it is null
     *
     * @param id the id of the centerDonorDTO to save.
     * @param centerDonorDTO the centerDonorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated centerDonorDTO,
     * or with status {@code 400 (Bad Request)} if the centerDonorDTO is not valid,
     * or with status {@code 404 (Not Found)} if the centerDonorDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the centerDonorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = ["/center-donors/{id}"], consumes = ["application/json", "application/merge-patch+json"])
    @Throws(URISyntaxException::class)
    fun partialUpdateCenterDonor(
        @PathVariable(value = "id", required = false) id: Long,
        @RequestBody centerDonorDTO: CenterDonorDTO
    ): ResponseEntity<CenterDonorDTO> {
        log.debug("REST request to partial update CenterDonor partially : {}, {}", id, centerDonorDTO)
        if (centerDonorDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        if (!Objects.equals(id, centerDonorDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!centerDonorRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = centerDonorService.partialUpdate(centerDonorDTO)

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, centerDonorDTO.id.toString())
        )
    }

    /**
     * `GET  /center-donors` : get all the centerDonors.
     *

     * @return the [ResponseEntity] with status `200 (OK)` and the list of centerDonors in body.
     */
    @GetMapping("/center-donors")
    fun getAllCenterDonors(): MutableList<CenterDonorDTO> {

        log.debug("REST request to get all CenterDonors")

        return centerDonorService.findAll()
    }

    /**
     * `GET  /center-donors/:id` : get the "id" centerDonor.
     *
     * @param id the id of the centerDonorDTO to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the centerDonorDTO, or with status `404 (Not Found)`.
     */
    @GetMapping("/center-donors/{id}")
    fun getCenterDonor(@PathVariable id: Long): ResponseEntity<CenterDonorDTO> {
        log.debug("REST request to get CenterDonor : $id")
        val centerDonorDTO = centerDonorService.findOne(id)
        return ResponseUtil.wrapOrNotFound(centerDonorDTO)
    }
    /**
     *  `DELETE  /center-donors/:id` : delete the "id" centerDonor.
     *
     * @param id the id of the centerDonorDTO to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/center-donors/{id}")
    fun deleteCenterDonor(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete CenterDonor : $id")

        centerDonorService.delete(id)
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build()
    }
}
