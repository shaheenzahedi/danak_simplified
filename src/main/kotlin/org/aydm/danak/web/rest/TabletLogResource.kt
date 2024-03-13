package org.aydm.danak.web.rest

import org.aydm.danak.repository.TabletLogRepository
import org.aydm.danak.service.TabletLogQueryService
import org.aydm.danak.service.TabletLogService
import org.aydm.danak.service.criteria.TabletLogCriteria
import org.aydm.danak.service.dto.TabletLogDTO
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

private const val ENTITY_NAME = "tabletLog"

/**
 * REST controller for managing [org.aydm.danak.domain.TabletLog].
 */
@RestController
@RequestMapping("/api")
class TabletLogResource(
    private val tabletLogService: TabletLogService,
    private val tabletLogRepository: TabletLogRepository,
    private val tabletLogQueryService: TabletLogQueryService,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    companion object {
        const val ENTITY_NAME = "tabletLog"
    }

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /tablet-logs` : Create a new tabletLog.
     *
     * @param tabletLogDTO the tabletLogDTO to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new tabletLogDTO, or with status `400 (Bad Request)` if the tabletLog has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tablet-logs")
    fun createTabletLog(@RequestBody tabletLogDTO: TabletLogDTO): ResponseEntity<TabletLogDTO> {
        log.debug("REST request to save TabletLog : $tabletLogDTO")
        if (tabletLogDTO.id != null) {
            throw BadRequestAlertException(
                "A new tabletLog cannot already have an ID",
                ENTITY_NAME, "idexists"
            )
        }
        val result = tabletLogService.save(tabletLogDTO)
        return ResponseEntity.created(URI("/api/tablet-logs/${result.id}"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * {@code PUT  /tablet-logs/:id} : Updates an existing tabletLog.
     *
     * @param id the id of the tabletLogDTO to save.
     * @param tabletLogDTO the tabletLogDTO to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated tabletLogDTO,
     * or with status `400 (Bad Request)` if the tabletLogDTO is not valid,
     * or with status `500 (Internal Server Error)` if the tabletLogDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tablet-logs/{id}")
    fun updateTabletLog(
        @PathVariable(value = "id", required = false) id: Long,
        @RequestBody tabletLogDTO: TabletLogDTO
    ): ResponseEntity<TabletLogDTO> {
        log.debug("REST request to update TabletLog : {}, {}", id, tabletLogDTO)
        if (tabletLogDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }

        if (!Objects.equals(id, tabletLogDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!tabletLogRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = tabletLogService.update(tabletLogDTO)
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName, true, ENTITY_NAME,
                    tabletLogDTO.id.toString()
                )
            )
            .body(result)
    }

    /**
     * {@code PATCH  /tablet-logs/:id} : Partial updates given fields of an existing tabletLog, field will ignore if it is null
     *
     * @param id the id of the tabletLogDTO to save.
     * @param tabletLogDTO the tabletLogDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tabletLogDTO,
     * or with status {@code 400 (Bad Request)} if the tabletLogDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tabletLogDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tabletLogDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = ["/tablet-logs/{id}"], consumes = ["application/json", "application/merge-patch+json"])
    @Throws(URISyntaxException::class)
    fun partialUpdateTabletLog(
        @PathVariable(value = "id", required = false) id: Long,
        @RequestBody tabletLogDTO: TabletLogDTO
    ): ResponseEntity<TabletLogDTO> {
        log.debug("REST request to partial update TabletLog partially : {}, {}", id, tabletLogDTO)
        if (tabletLogDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        if (!Objects.equals(id, tabletLogDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!tabletLogRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = tabletLogService.partialUpdate(tabletLogDTO)

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tabletLogDTO.id.toString())
        )
    }

    /**
     * `GET  /tablet-logs` : get all the tabletLogs.
     *
     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the [ResponseEntity] with status `200 (OK)` and the list of tabletLogs in body.
     */
    @GetMapping("/tablet-logs")
    fun getAllTabletLogs(
        criteria: TabletLogCriteria,
        @org.springdoc.api.annotations.ParameterObject pageable: Pageable

    ): ResponseEntity<MutableList<TabletLogDTO>> {
        log.debug("REST request to get TabletLogs by criteria: $criteria")
        val page = tabletLogQueryService.findByCriteria(criteria, pageable)
        val headers =
            PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page)
        return ResponseEntity.ok().headers(headers).body(page.content)
    }

    /**
     * `GET  /tablet-logs/count}` : count all the tabletLogs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the [ResponseEntity] with status `200 (OK)` and the count in body.
     */
    @GetMapping("/tablet-logs/count")
    fun countTabletLogs(criteria: TabletLogCriteria): ResponseEntity<Long> {
        log.debug("REST request to count TabletLogs by criteria: $criteria")
        return ResponseEntity.ok().body(tabletLogQueryService.countByCriteria(criteria))
    }

    /**
     * `GET  /tablet-logs/:id` : get the "id" tabletLog.
     *
     * @param id the id of the tabletLogDTO to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the tabletLogDTO, or with status `404 (Not Found)`.
     */
    @GetMapping("/tablet-logs/{id}")
    fun getTabletLog(@PathVariable id: Long): ResponseEntity<TabletLogDTO> {
        log.debug("REST request to get TabletLog : $id")
        val tabletLogDTO = tabletLogService.findOne(id)
        return ResponseUtil.wrapOrNotFound(tabletLogDTO)
    }

    /**
     *  `DELETE  /tablet-logs/:id` : delete the "id" tabletLog.
     *
     * @param id the id of the tabletLogDTO to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/tablet-logs/{id}")
    fun deleteTabletLog(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete TabletLog : $id")

        tabletLogService.delete(id)
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build()
    }
}
