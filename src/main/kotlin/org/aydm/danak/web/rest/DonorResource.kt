package org.aydm.danak.web.rest

import org.aydm.danak.repository.DonorRepository
import org.aydm.danak.security.ADMIN
import org.aydm.danak.service.DonorQueryService
import org.aydm.danak.service.DonorService
import org.aydm.danak.service.criteria.DonorCriteria
import org.aydm.danak.service.dto.DonorDTO
import org.aydm.danak.service.facade.UserFacade
import org.aydm.danak.web.rest.errors.BadRequestAlertException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import tech.jhipster.web.util.HeaderUtil
import tech.jhipster.web.util.PaginationUtil
import tech.jhipster.web.util.ResponseUtil
import java.net.URI
import java.net.URISyntaxException
import java.util.Objects

private const val ENTITY_NAME = "donor"
/**
 * REST controller for managing [org.aydm.danak.domain.Donor].
 */
@RestController
@RequestMapping("/api")
class DonorResource(
    private val donorService: DonorService,
    private val donorRepository: DonorRepository,
    private val donorQueryService: DonorQueryService,
    private val userFacade: UserFacade,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    companion object {
        const val ENTITY_NAME = "donor"
    }

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /donors` : Create a new donor.
     *
     * @param donorDTO the donorDTO to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new donorDTO, or with status `400 (Bad Request)` if the donor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/donors")
    fun createDonor(@RequestBody donorDTO: DonorDTO): ResponseEntity<DonorDTO> {
        log.debug("REST request to save Donor : $donorDTO")
        if (donorDTO.id != null) {
            throw BadRequestAlertException(
                "A new donor cannot already have an ID",
                ENTITY_NAME, "idexists"
            )
        }
        val result = donorService.save(donorDTO)
        return ResponseEntity.created(URI("/api/donors/${result.id}"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()))
            .body(result)
    }
    @PostMapping("/donors/list")
    @PreAuthorize("hasAuthority(\"$ADMIN\")")
    fun donorList(        @org.springdoc.api.annotations.ParameterObject pageable: Pageable
    ): ResponseEntity<Page<DonorDTO>> {
        log.debug("REST request to get Donors")
        val donors = userFacade.getDonors(pageable)
        return ResponseEntity.ok().body(donors)
    }
    @PostMapping("/donors/register")
    @PreAuthorize("hasAuthority(\"$ADMIN\")")
    fun registerDonor(@RequestBody donorDTO: DonorDTO): ResponseEntity<DonorDTO> {
        log.debug("REST request to save Donor : {}", donorDTO)
        if (donorDTO.id != null) {
            throw BadRequestAlertException(
                "A new donor cannot already have an ID",
                ENTITY_NAME, "idexists"
            )
        }
        val result = userFacade.registerDonor(donorDTO)
        return ResponseEntity.created(URI("/api/donors/${result.id}"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * {@code PUT  /donors/:id} : Updates an existing donor.
     *
     * @param id the id of the donorDTO to save.
     * @param donorDTO the donorDTO to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated donorDTO,
     * or with status `400 (Bad Request)` if the donorDTO is not valid,
     * or with status `500 (Internal Server Error)` if the donorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/donors/{id}")
    fun updateDonor(
        @PathVariable(value = "id", required = false) id: Long,
        @RequestBody donorDTO: DonorDTO
    ): ResponseEntity<DonorDTO> {
        log.debug("REST request to update Donor : {}, {}", id, donorDTO)
        if (donorDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }

        if (!Objects.equals(id, donorDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!donorRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = donorService.update(donorDTO)
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName, true, ENTITY_NAME,
                    donorDTO.id.toString()
                )
            )
            .body(result)
    }

    /**
     * {@code PATCH  /donors/:id} : Partial updates given fields of an existing donor, field will ignore if it is null
     *
     * @param id the id of the donorDTO to save.
     * @param donorDTO the donorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated donorDTO,
     * or with status {@code 400 (Bad Request)} if the donorDTO is not valid,
     * or with status {@code 404 (Not Found)} if the donorDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the donorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = ["/donors/{id}"], consumes = ["application/json", "application/merge-patch+json"])
    @Throws(URISyntaxException::class)
    fun partialUpdateDonor(
        @PathVariable(value = "id", required = false) id: Long,
        @RequestBody donorDTO: DonorDTO
    ): ResponseEntity<DonorDTO> {
        log.debug("REST request to partial update Donor partially : {}, {}", id, donorDTO)
        if (donorDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        if (!Objects.equals(id, donorDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!donorRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = donorService.partialUpdate(donorDTO)

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, donorDTO.id.toString())
        )
    }

    /**
     * `GET  /donors` : get all the donors.
     *
     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the [ResponseEntity] with status `200 (OK)` and the list of donors in body.
     */
    @GetMapping("/donors") fun getAllDonors(
        criteria: DonorCriteria,
        @org.springdoc.api.annotations.ParameterObject pageable: Pageable

    ): ResponseEntity<MutableList<DonorDTO>> {
        log.debug("REST request to get Donors by criteria: $criteria")
        val page = donorQueryService.findByCriteria(criteria, pageable)
        val headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page)
        return ResponseEntity.ok().headers(headers).body(page.content)
    }

    /**
     * `GET  /donors/count}` : count all the donors.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the [ResponseEntity] with status `200 (OK)` and the count in body.
     */
    @GetMapping("/donors/count")
    fun countDonors(criteria: DonorCriteria): ResponseEntity<Long> {
        log.debug("REST request to count Donors by criteria: $criteria")
        return ResponseEntity.ok().body(donorQueryService.countByCriteria(criteria))
    }

    /**
     * `GET  /donors/:id` : get the "id" donor.
     *
     * @param id the id of the donorDTO to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the donorDTO, or with status `404 (Not Found)`.
     */
    @GetMapping("/donors/{id}")
    fun getDonor(@PathVariable id: Long): ResponseEntity<DonorDTO> {
        log.debug("REST request to get Donor : $id")
        val donorDTO = donorService.findOne(id)
        return ResponseUtil.wrapOrNotFound(donorDTO)
    }
    /**
     *  `DELETE  /donors/:id` : delete the "id" donor.
     *
     * @param id the id of the donorDTO to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/donors/{id}")
    fun deleteDonor(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete Donor : $id")

        donorService.delete(id)
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build()
    }
}
