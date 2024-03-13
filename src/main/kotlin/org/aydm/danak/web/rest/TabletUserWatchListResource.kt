package org.aydm.danak.web.rest

import org.aydm.danak.repository.TabletUserWatchListRepository
import org.aydm.danak.service.TabletUserWatchListQueryService
import org.aydm.danak.service.TabletUserWatchListService
import org.aydm.danak.service.criteria.TabletUserWatchListCriteria
import org.aydm.danak.service.dto.TabletUserWatchListDTO
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

private const val ENTITY_NAME = "tabletUserWatchList"

/**
 * REST controller for managing [org.aydm.danak.domain.TabletUserWatchList].
 */
@RestController
@RequestMapping("/api")
class TabletUserWatchListResource(
    private val tabletUserWatchListService: TabletUserWatchListService,
    private val tabletUserWatchListRepository: TabletUserWatchListRepository,
    private val tabletUserWatchListQueryService: TabletUserWatchListQueryService,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    companion object {
        const val ENTITY_NAME = "tabletUserWatchList"
    }

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /tablet-user-watch-lists` : Create a new tabletUserWatchList.
     *
     * @param tabletUserWatchListDTO the tabletUserWatchListDTO to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new tabletUserWatchListDTO, or with status `400 (Bad Request)` if the tabletUserWatchList has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tablet-user-watch-lists")
    fun createTabletUserWatchList(@RequestBody tabletUserWatchListDTO: TabletUserWatchListDTO): ResponseEntity<TabletUserWatchListDTO> {
        log.debug("REST request to save TabletUserWatchList : $tabletUserWatchListDTO")
        if (tabletUserWatchListDTO.id != null) {
            throw BadRequestAlertException(
                "A new tabletUserWatchList cannot already have an ID",
                ENTITY_NAME, "idexists"
            )
        }
        val result = tabletUserWatchListService.save(tabletUserWatchListDTO)
        return ResponseEntity.created(URI("/api/tablet-user-watch-lists/${result.id}"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * {@code PUT  /tablet-user-watch-lists/:id} : Updates an existing tabletUserWatchList.
     *
     * @param id the id of the tabletUserWatchListDTO to save.
     * @param tabletUserWatchListDTO the tabletUserWatchListDTO to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated tabletUserWatchListDTO,
     * or with status `400 (Bad Request)` if the tabletUserWatchListDTO is not valid,
     * or with status `500 (Internal Server Error)` if the tabletUserWatchListDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tablet-user-watch-lists/{id}")
    fun updateTabletUserWatchList(
        @PathVariable(value = "id", required = false) id: Long,
        @RequestBody tabletUserWatchListDTO: TabletUserWatchListDTO
    ): ResponseEntity<TabletUserWatchListDTO> {
        log.debug("REST request to update TabletUserWatchList : {}, {}", id, tabletUserWatchListDTO)
        if (tabletUserWatchListDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }

        if (!Objects.equals(id, tabletUserWatchListDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!tabletUserWatchListRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = tabletUserWatchListService.update(tabletUserWatchListDTO)
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName, true, ENTITY_NAME,
                    tabletUserWatchListDTO.id.toString()
                )
            )
            .body(result)
    }

    /**
     * {@code PATCH  /tablet-user-watch-lists/:id} : Partial updates given fields of an existing tabletUserWatchList, field will ignore if it is null
     *
     * @param id the id of the tabletUserWatchListDTO to save.
     * @param tabletUserWatchListDTO the tabletUserWatchListDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tabletUserWatchListDTO,
     * or with status {@code 400 (Bad Request)} if the tabletUserWatchListDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tabletUserWatchListDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tabletUserWatchListDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(
        value = ["/tablet-user-watch-lists/{id}"],
        consumes = ["application/json", "application/merge-patch+json"]
    )
    @Throws(URISyntaxException::class)
    fun partialUpdateTabletUserWatchList(
        @PathVariable(value = "id", required = false) id: Long,
        @RequestBody tabletUserWatchListDTO: TabletUserWatchListDTO
    ): ResponseEntity<TabletUserWatchListDTO> {
        log.debug("REST request to partial update TabletUserWatchList partially : {}, {}", id, tabletUserWatchListDTO)
        if (tabletUserWatchListDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        if (!Objects.equals(id, tabletUserWatchListDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!tabletUserWatchListRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = tabletUserWatchListService.partialUpdate(tabletUserWatchListDTO)

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tabletUserWatchListDTO.id.toString())
        )
    }

    /**
     * `GET  /tablet-user-watch-lists` : get all the tabletUserWatchLists.
     *
     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the [ResponseEntity] with status `200 (OK)` and the list of tabletUserWatchLists in body.
     */
    @GetMapping("/tablet-user-watch-lists")
    fun getAllTabletUserWatchLists(
        criteria: TabletUserWatchListCriteria,
        @org.springdoc.api.annotations.ParameterObject pageable: Pageable

    ): ResponseEntity<MutableList<TabletUserWatchListDTO>> {
        log.debug("REST request to get TabletUserWatchLists by criteria: $criteria")
        val page = tabletUserWatchListQueryService.findByCriteria(criteria, pageable)
        val headers =
            PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page)
        return ResponseEntity.ok().headers(headers).body(page.content)
    }

    /**
     * `GET  /tablet-user-watch-lists/count}` : count all the tabletUserWatchLists.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the [ResponseEntity] with status `200 (OK)` and the count in body.
     */
    @GetMapping("/tablet-user-watch-lists/count")
    fun countTabletUserWatchLists(criteria: TabletUserWatchListCriteria): ResponseEntity<Long> {
        log.debug("REST request to count TabletUserWatchLists by criteria: $criteria")
        return ResponseEntity.ok().body(tabletUserWatchListQueryService.countByCriteria(criteria))
    }

    /**
     * `GET  /tablet-user-watch-lists/:id` : get the "id" tabletUserWatchList.
     *
     * @param id the id of the tabletUserWatchListDTO to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the tabletUserWatchListDTO, or with status `404 (Not Found)`.
     */
    @GetMapping("/tablet-user-watch-lists/{id}")
    fun getTabletUserWatchList(@PathVariable id: Long): ResponseEntity<TabletUserWatchListDTO> {
        log.debug("REST request to get TabletUserWatchList : $id")
        val tabletUserWatchListDTO = tabletUserWatchListService.findOne(id)
        return ResponseUtil.wrapOrNotFound(tabletUserWatchListDTO)
    }

    /**
     *  `DELETE  /tablet-user-watch-lists/:id` : delete the "id" tabletUserWatchList.
     *
     * @param id the id of the tabletUserWatchListDTO to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/tablet-user-watch-lists/{id}")
    fun deleteTabletUserWatchList(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete TabletUserWatchList : $id")

        tabletUserWatchListService.delete(id)
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build()
    }
}
