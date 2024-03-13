package org.aydm.danak.web.rest

import org.aydm.danak.repository.PanelLogRepository
import org.aydm.danak.service.PanelLogQueryService
import org.aydm.danak.service.PanelLogService
import org.aydm.danak.service.criteria.PanelLogCriteria
import org.aydm.danak.service.dto.PanelLogDTO
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

private const val ENTITY_NAME = "panelLog"

/**
 * REST controller for managing [org.aydm.danak.domain.PanelLog].
 */
@RestController
@RequestMapping("/api")
class PanelLogResource(
    private val panelLogService: PanelLogService,
    private val panelLogRepository: PanelLogRepository,
    private val panelLogQueryService: PanelLogQueryService,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    companion object {
        const val ENTITY_NAME = "panelLog"
    }

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /panel-logs` : Create a new panelLog.
     *
     * @param panelLogDTO the panelLogDTO to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new panelLogDTO, or with status `400 (Bad Request)` if the panelLog has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/panel-logs")
    fun createPanelLog(@RequestBody panelLogDTO: PanelLogDTO): ResponseEntity<PanelLogDTO> {
        log.debug("REST request to save PanelLog : $panelLogDTO")
        if (panelLogDTO.id != null) {
            throw BadRequestAlertException(
                "A new panelLog cannot already have an ID",
                ENTITY_NAME, "idexists"
            )
        }
        val result = panelLogService.save(panelLogDTO)
        return ResponseEntity.created(URI("/api/panel-logs/${result.id}"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * {@code PUT  /panel-logs/:id} : Updates an existing panelLog.
     *
     * @param id the id of the panelLogDTO to save.
     * @param panelLogDTO the panelLogDTO to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated panelLogDTO,
     * or with status `400 (Bad Request)` if the panelLogDTO is not valid,
     * or with status `500 (Internal Server Error)` if the panelLogDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/panel-logs/{id}")
    fun updatePanelLog(
        @PathVariable(value = "id", required = false) id: Long,
        @RequestBody panelLogDTO: PanelLogDTO
    ): ResponseEntity<PanelLogDTO> {
        log.debug("REST request to update PanelLog : {}, {}", id, panelLogDTO)
        if (panelLogDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }

        if (!Objects.equals(id, panelLogDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!panelLogRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = panelLogService.update(panelLogDTO)
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName, true, ENTITY_NAME,
                    panelLogDTO.id.toString()
                )
            )
            .body(result)
    }

    /**
     * {@code PATCH  /panel-logs/:id} : Partial updates given fields of an existing panelLog, field will ignore if it is null
     *
     * @param id the id of the panelLogDTO to save.
     * @param panelLogDTO the panelLogDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated panelLogDTO,
     * or with status {@code 400 (Bad Request)} if the panelLogDTO is not valid,
     * or with status {@code 404 (Not Found)} if the panelLogDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the panelLogDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = ["/panel-logs/{id}"], consumes = ["application/json", "application/merge-patch+json"])
    @Throws(URISyntaxException::class)
    fun partialUpdatePanelLog(
        @PathVariable(value = "id", required = false) id: Long,
        @RequestBody panelLogDTO: PanelLogDTO
    ): ResponseEntity<PanelLogDTO> {
        log.debug("REST request to partial update PanelLog partially : {}, {}", id, panelLogDTO)
        if (panelLogDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        if (!Objects.equals(id, panelLogDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!panelLogRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = panelLogService.partialUpdate(panelLogDTO)

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, panelLogDTO.id.toString())
        )
    }

    /**
     * `GET  /panel-logs` : get all the panelLogs.
     *
     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the [ResponseEntity] with status `200 (OK)` and the list of panelLogs in body.
     */
    @GetMapping("/panel-logs")
    fun getAllPanelLogs(
        criteria: PanelLogCriteria,
        @org.springdoc.api.annotations.ParameterObject pageable: Pageable

    ): ResponseEntity<MutableList<PanelLogDTO>> {
        log.debug("REST request to get PanelLogs by criteria: $criteria")
        val page = panelLogQueryService.findByCriteria(criteria, pageable)
        val headers =
            PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page)
        return ResponseEntity.ok().headers(headers).body(page.content)
    }

    /**
     * `GET  /panel-logs/count}` : count all the panelLogs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the [ResponseEntity] with status `200 (OK)` and the count in body.
     */
    @GetMapping("/panel-logs/count")
    fun countPanelLogs(criteria: PanelLogCriteria): ResponseEntity<Long> {
        log.debug("REST request to count PanelLogs by criteria: $criteria")
        return ResponseEntity.ok().body(panelLogQueryService.countByCriteria(criteria))
    }

    /**
     * `GET  /panel-logs/:id` : get the "id" panelLog.
     *
     * @param id the id of the panelLogDTO to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the panelLogDTO, or with status `404 (Not Found)`.
     */
    @GetMapping("/panel-logs/{id}")
    fun getPanelLog(@PathVariable id: Long): ResponseEntity<PanelLogDTO> {
        log.debug("REST request to get PanelLog : $id")
        val panelLogDTO = panelLogService.findOne(id)
        return ResponseUtil.wrapOrNotFound(panelLogDTO)
    }

    /**
     *  `DELETE  /panel-logs/:id` : delete the "id" panelLog.
     *
     * @param id the id of the panelLogDTO to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/panel-logs/{id}")
    fun deletePanelLog(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete PanelLog : $id")

        panelLogService.delete(id)
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build()
    }
}
