package org.aydm.danak.web.rest

import org.aydm.danak.repository.TabletWatchListRepository
import org.aydm.danak.service.TabletWatchListQueryService
import org.aydm.danak.service.TabletWatchListService
import org.aydm.danak.service.criteria.TabletWatchListCriteria
import org.aydm.danak.service.dto.TabletWatchListDTO
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

private const val ENTITY_NAME = "tabletWatchList"

/**
 * REST controller for managing [org.aydm.danak.domain.TabletWatchList].
 */
@RestController
@RequestMapping("/api")
class TabletWatchListResource(
    private val tabletWatchListService: TabletWatchListService,
    private val tabletWatchListRepository: TabletWatchListRepository,
    private val tabletWatchListQueryService: TabletWatchListQueryService,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    companion object {
        const val ENTITY_NAME = "tabletWatchList"
    }

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /tablet-watch-lists` : Create a new tabletWatchList.
     *
     * @param tabletWatchListDTO the tabletWatchListDTO to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new tabletWatchListDTO, or with status `400 (Bad Request)` if the tabletWatchList has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tablet-watch-lists")
    fun createTabletWatchList(@RequestBody tabletWatchListDTO: TabletWatchListDTO): ResponseEntity<TabletWatchListDTO> {
        log.debug("REST request to save TabletWatchList : $tabletWatchListDTO")
        if (tabletWatchListDTO.id != null) {
            throw BadRequestAlertException(
                "A new tabletWatchList cannot already have an ID",
                ENTITY_NAME, "idexists"
            )
        }
        val result = tabletWatchListService.save(tabletWatchListDTO)
        return ResponseEntity.created(URI("/api/tablet-watch-lists/${result.id}"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * {@code PUT  /tablet-watch-lists/:id} : Updates an existing tabletWatchList.
     *
     * @param id the id of the tabletWatchListDTO to save.
     * @param tabletWatchListDTO the tabletWatchListDTO to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated tabletWatchListDTO,
     * or with status `400 (Bad Request)` if the tabletWatchListDTO is not valid,
     * or with status `500 (Internal Server Error)` if the tabletWatchListDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tablet-watch-lists/{id}")
    fun updateTabletWatchList(
        @PathVariable(value = "id", required = false) id: Long,
        @RequestBody tabletWatchListDTO: TabletWatchListDTO
    ): ResponseEntity<TabletWatchListDTO> {
        log.debug("REST request to update TabletWatchList : {}, {}", id, tabletWatchListDTO)
        if (tabletWatchListDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }

        if (!Objects.equals(id, tabletWatchListDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!tabletWatchListRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = tabletWatchListService.update(tabletWatchListDTO)
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName, true, ENTITY_NAME,
                    tabletWatchListDTO.id.toString()
                )
            )
            .body(result)
    }

    /**
     * {@code PATCH  /tablet-watch-lists/:id} : Partial updates given fields of an existing tabletWatchList, field will ignore if it is null
     *
     * @param id the id of the tabletWatchListDTO to save.
     * @param tabletWatchListDTO the tabletWatchListDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tabletWatchListDTO,
     * or with status {@code 400 (Bad Request)} if the tabletWatchListDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tabletWatchListDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tabletWatchListDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = ["/tablet-watch-lists/{id}"], consumes = ["application/json", "application/merge-patch+json"])
    @Throws(URISyntaxException::class)
    fun partialUpdateTabletWatchList(
        @PathVariable(value = "id", required = false) id: Long,
        @RequestBody tabletWatchListDTO: TabletWatchListDTO
    ): ResponseEntity<TabletWatchListDTO> {
        log.debug("REST request to partial update TabletWatchList partially : {}, {}", id, tabletWatchListDTO)
        if (tabletWatchListDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        if (!Objects.equals(id, tabletWatchListDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!tabletWatchListRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = tabletWatchListService.partialUpdate(tabletWatchListDTO)

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tabletWatchListDTO.id.toString())
        )
    }

    /**
     * `GET  /tablet-watch-lists` : get all the tabletWatchLists.
     *
     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the [ResponseEntity] with status `200 (OK)` and the list of tabletWatchLists in body.
     */
    @GetMapping("/tablet-watch-lists")
    fun getAllTabletWatchLists(
        criteria: TabletWatchListCriteria,
        @org.springdoc.api.annotations.ParameterObject pageable: Pageable

    ): ResponseEntity<MutableList<TabletWatchListDTO>> {
        log.debug("REST request to get TabletWatchLists by criteria: $criteria")
        val page = tabletWatchListQueryService.findByCriteria(criteria, pageable)
        val headers =
            PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page)
        return ResponseEntity.ok().headers(headers).body(page.content)
    }

    /**
     * `GET  /tablet-watch-lists/count}` : count all the tabletWatchLists.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the [ResponseEntity] with status `200 (OK)` and the count in body.
     */
    @GetMapping("/tablet-watch-lists/count")
    fun countTabletWatchLists(criteria: TabletWatchListCriteria): ResponseEntity<Long> {
        log.debug("REST request to count TabletWatchLists by criteria: $criteria")
        return ResponseEntity.ok().body(tabletWatchListQueryService.countByCriteria(criteria))
    }

    /**
     * `GET  /tablet-watch-lists/:id` : get the "id" tabletWatchList.
     *
     * @param id the id of the tabletWatchListDTO to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the tabletWatchListDTO, or with status `404 (Not Found)`.
     */
    @GetMapping("/tablet-watch-lists/{id}")
    fun getTabletWatchList(@PathVariable id: Long): ResponseEntity<TabletWatchListDTO> {
        log.debug("REST request to get TabletWatchList : $id")
        val tabletWatchListDTO = tabletWatchListService.findOne(id)
        return ResponseUtil.wrapOrNotFound(tabletWatchListDTO)
    }

    /**
     *  `DELETE  /tablet-watch-lists/:id` : delete the "id" tabletWatchList.
     *
     * @param id the id of the tabletWatchListDTO to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/tablet-watch-lists/{id}")
    fun deleteTabletWatchList(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete TabletWatchList : $id")

        tabletWatchListService.delete(id)
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build()
    }
}
