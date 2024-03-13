package org.aydm.danak.web.rest

import org.aydm.danak.repository.DonorWatchListRepository
import org.aydm.danak.service.DonorWatchListQueryService
import org.aydm.danak.service.DonorWatchListService
import org.aydm.danak.service.criteria.DonorWatchListCriteria
import org.aydm.danak.service.dto.DonorWatchListDTO
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

private const val ENTITY_NAME = "donorWatchList"

/**
 * REST controller for managing [org.aydm.danak.domain.DonorWatchList].
 */
@RestController
@RequestMapping("/api")
class DonorWatchListResource(
    private val donorWatchListService: DonorWatchListService,
    private val donorWatchListRepository: DonorWatchListRepository,
    private val donorWatchListQueryService: DonorWatchListQueryService,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    companion object {
        const val ENTITY_NAME = "donorWatchList"
    }

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /donor-watch-lists` : Create a new donorWatchList.
     *
     * @param donorWatchListDTO the donorWatchListDTO to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new donorWatchListDTO, or with status `400 (Bad Request)` if the donorWatchList has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/donor-watch-lists")
    fun createDonorWatchList(@RequestBody donorWatchListDTO: DonorWatchListDTO): ResponseEntity<DonorWatchListDTO> {
        log.debug("REST request to save DonorWatchList : $donorWatchListDTO")
        if (donorWatchListDTO.id != null) {
            throw BadRequestAlertException(
                "A new donorWatchList cannot already have an ID",
                ENTITY_NAME, "idexists"
            )
        }
        val result = donorWatchListService.save(donorWatchListDTO)
        return ResponseEntity.created(URI("/api/donor-watch-lists/${result.id}"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * {@code PUT  /donor-watch-lists/:id} : Updates an existing donorWatchList.
     *
     * @param id the id of the donorWatchListDTO to save.
     * @param donorWatchListDTO the donorWatchListDTO to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated donorWatchListDTO,
     * or with status `400 (Bad Request)` if the donorWatchListDTO is not valid,
     * or with status `500 (Internal Server Error)` if the donorWatchListDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/donor-watch-lists/{id}")
    fun updateDonorWatchList(
        @PathVariable(value = "id", required = false) id: Long,
        @RequestBody donorWatchListDTO: DonorWatchListDTO
    ): ResponseEntity<DonorWatchListDTO> {
        log.debug("REST request to update DonorWatchList : {}, {}", id, donorWatchListDTO)
        if (donorWatchListDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }

        if (!Objects.equals(id, donorWatchListDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!donorWatchListRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = donorWatchListService.update(donorWatchListDTO)
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName, true, ENTITY_NAME,
                    donorWatchListDTO.id.toString()
                )
            )
            .body(result)
    }

    /**
     * {@code PATCH  /donor-watch-lists/:id} : Partial updates given fields of an existing donorWatchList, field will ignore if it is null
     *
     * @param id the id of the donorWatchListDTO to save.
     * @param donorWatchListDTO the donorWatchListDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated donorWatchListDTO,
     * or with status {@code 400 (Bad Request)} if the donorWatchListDTO is not valid,
     * or with status {@code 404 (Not Found)} if the donorWatchListDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the donorWatchListDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = ["/donor-watch-lists/{id}"], consumes = ["application/json", "application/merge-patch+json"])
    @Throws(URISyntaxException::class)
    fun partialUpdateDonorWatchList(
        @PathVariable(value = "id", required = false) id: Long,
        @RequestBody donorWatchListDTO: DonorWatchListDTO
    ): ResponseEntity<DonorWatchListDTO> {
        log.debug("REST request to partial update DonorWatchList partially : {}, {}", id, donorWatchListDTO)
        if (donorWatchListDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        if (!Objects.equals(id, donorWatchListDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!donorWatchListRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = donorWatchListService.partialUpdate(donorWatchListDTO)

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, donorWatchListDTO.id.toString())
        )
    }

    /**
     * `GET  /donor-watch-lists` : get all the donorWatchLists.
     *
     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the [ResponseEntity] with status `200 (OK)` and the list of donorWatchLists in body.
     */
    @GetMapping("/donor-watch-lists")
    fun getAllDonorWatchLists(
        criteria: DonorWatchListCriteria,
        @org.springdoc.api.annotations.ParameterObject pageable: Pageable

    ): ResponseEntity<MutableList<DonorWatchListDTO>> {
        log.debug("REST request to get DonorWatchLists by criteria: $criteria")
        val page = donorWatchListQueryService.findByCriteria(criteria, pageable)
        val headers =
            PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page)
        return ResponseEntity.ok().headers(headers).body(page.content)
    }

    /**
     * `GET  /donor-watch-lists/count}` : count all the donorWatchLists.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the [ResponseEntity] with status `200 (OK)` and the count in body.
     */
    @GetMapping("/donor-watch-lists/count")
    fun countDonorWatchLists(criteria: DonorWatchListCriteria): ResponseEntity<Long> {
        log.debug("REST request to count DonorWatchLists by criteria: $criteria")
        return ResponseEntity.ok().body(donorWatchListQueryService.countByCriteria(criteria))
    }

    /**
     * `GET  /donor-watch-lists/:id` : get the "id" donorWatchList.
     *
     * @param id the id of the donorWatchListDTO to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the donorWatchListDTO, or with status `404 (Not Found)`.
     */
    @GetMapping("/donor-watch-lists/{id}")
    fun getDonorWatchList(@PathVariable id: Long): ResponseEntity<DonorWatchListDTO> {
        log.debug("REST request to get DonorWatchList : $id")
        val donorWatchListDTO = donorWatchListService.findOne(id)
        return ResponseUtil.wrapOrNotFound(donorWatchListDTO)
    }

    /**
     *  `DELETE  /donor-watch-lists/:id` : delete the "id" donorWatchList.
     *
     * @param id the id of the donorWatchListDTO to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/donor-watch-lists/{id}")
    fun deleteDonorWatchList(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete DonorWatchList : $id")

        donorWatchListService.delete(id)
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build()
    }
}
