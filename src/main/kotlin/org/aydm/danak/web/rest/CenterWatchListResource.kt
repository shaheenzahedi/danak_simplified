package org.aydm.danak.web.rest

import org.aydm.danak.repository.CenterWatchListRepository
import org.aydm.danak.service.CenterWatchListQueryService
import org.aydm.danak.service.CenterWatchListService
import org.aydm.danak.service.criteria.CenterWatchListCriteria
import org.aydm.danak.service.dto.CenterWatchListDTO
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

private const val ENTITY_NAME = "centerWatchList"

/**
 * REST controller for managing [org.aydm.danak.domain.CenterWatchList].
 */
@RestController
@RequestMapping("/api")
class CenterWatchListResource(
    private val centerWatchListService: CenterWatchListService,
    private val centerWatchListRepository: CenterWatchListRepository,
    private val centerWatchListQueryService: CenterWatchListQueryService,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    companion object {
        const val ENTITY_NAME = "centerWatchList"
    }

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /center-watch-lists` : Create a new centerWatchList.
     *
     * @param centerWatchListDTO the centerWatchListDTO to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new centerWatchListDTO, or with status `400 (Bad Request)` if the centerWatchList has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/center-watch-lists")
    fun createCenterWatchList(@RequestBody centerWatchListDTO: CenterWatchListDTO): ResponseEntity<CenterWatchListDTO> {
        log.debug("REST request to save CenterWatchList : $centerWatchListDTO")
        if (centerWatchListDTO.id != null) {
            throw BadRequestAlertException(
                "A new centerWatchList cannot already have an ID",
                ENTITY_NAME, "idexists"
            )
        }
        val result = centerWatchListService.save(centerWatchListDTO)
        return ResponseEntity.created(URI("/api/center-watch-lists/${result.id}"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * {@code PUT  /center-watch-lists/:id} : Updates an existing centerWatchList.
     *
     * @param id the id of the centerWatchListDTO to save.
     * @param centerWatchListDTO the centerWatchListDTO to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated centerWatchListDTO,
     * or with status `400 (Bad Request)` if the centerWatchListDTO is not valid,
     * or with status `500 (Internal Server Error)` if the centerWatchListDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/center-watch-lists/{id}")
    fun updateCenterWatchList(
        @PathVariable(value = "id", required = false) id: Long,
        @RequestBody centerWatchListDTO: CenterWatchListDTO
    ): ResponseEntity<CenterWatchListDTO> {
        log.debug("REST request to update CenterWatchList : {}, {}", id, centerWatchListDTO)
        if (centerWatchListDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }

        if (!Objects.equals(id, centerWatchListDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!centerWatchListRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = centerWatchListService.update(centerWatchListDTO)
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName, true, ENTITY_NAME,
                    centerWatchListDTO.id.toString()
                )
            )
            .body(result)
    }

    /**
     * {@code PATCH  /center-watch-lists/:id} : Partial updates given fields of an existing centerWatchList, field will ignore if it is null
     *
     * @param id the id of the centerWatchListDTO to save.
     * @param centerWatchListDTO the centerWatchListDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated centerWatchListDTO,
     * or with status {@code 400 (Bad Request)} if the centerWatchListDTO is not valid,
     * or with status {@code 404 (Not Found)} if the centerWatchListDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the centerWatchListDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = ["/center-watch-lists/{id}"], consumes = ["application/json", "application/merge-patch+json"])
    @Throws(URISyntaxException::class)
    fun partialUpdateCenterWatchList(
        @PathVariable(value = "id", required = false) id: Long,
        @RequestBody centerWatchListDTO: CenterWatchListDTO
    ): ResponseEntity<CenterWatchListDTO> {
        log.debug("REST request to partial update CenterWatchList partially : {}, {}", id, centerWatchListDTO)
        if (centerWatchListDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        if (!Objects.equals(id, centerWatchListDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!centerWatchListRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = centerWatchListService.partialUpdate(centerWatchListDTO)

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, centerWatchListDTO.id.toString())
        )
    }

    /**
     * `GET  /center-watch-lists` : get all the centerWatchLists.
     *
     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the [ResponseEntity] with status `200 (OK)` and the list of centerWatchLists in body.
     */
    @GetMapping("/center-watch-lists")
    fun getAllCenterWatchLists(
        criteria: CenterWatchListCriteria,
        @org.springdoc.api.annotations.ParameterObject pageable: Pageable

    ): ResponseEntity<MutableList<CenterWatchListDTO>> {
        log.debug("REST request to get CenterWatchLists by criteria: $criteria")
        val page = centerWatchListQueryService.findByCriteria(criteria, pageable)
        val headers =
            PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page)
        return ResponseEntity.ok().headers(headers).body(page.content)
    }

    /**
     * `GET  /center-watch-lists/count}` : count all the centerWatchLists.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the [ResponseEntity] with status `200 (OK)` and the count in body.
     */
    @GetMapping("/center-watch-lists/count")
    fun countCenterWatchLists(criteria: CenterWatchListCriteria): ResponseEntity<Long> {
        log.debug("REST request to count CenterWatchLists by criteria: $criteria")
        return ResponseEntity.ok().body(centerWatchListQueryService.countByCriteria(criteria))
    }

    /**
     * `GET  /center-watch-lists/:id` : get the "id" centerWatchList.
     *
     * @param id the id of the centerWatchListDTO to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the centerWatchListDTO, or with status `404 (Not Found)`.
     */
    @GetMapping("/center-watch-lists/{id}")
    fun getCenterWatchList(@PathVariable id: Long): ResponseEntity<CenterWatchListDTO> {
        log.debug("REST request to get CenterWatchList : $id")
        val centerWatchListDTO = centerWatchListService.findOne(id)
        return ResponseUtil.wrapOrNotFound(centerWatchListDTO)
    }

    /**
     *  `DELETE  /center-watch-lists/:id` : delete the "id" centerWatchList.
     *
     * @param id the id of the centerWatchListDTO to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/center-watch-lists/{id}")
    fun deleteCenterWatchList(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete CenterWatchList : $id")

        centerWatchListService.delete(id)
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build()
    }
}
