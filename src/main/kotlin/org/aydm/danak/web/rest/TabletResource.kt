package org.aydm.danak.web.rest

import org.aydm.danak.repository.TabletRepository
import org.aydm.danak.service.TabletService
import org.aydm.danak.service.dto.TabletDTO
import org.aydm.danak.service.facade.UserFacade
import org.aydm.danak.web.rest.errors.BadRequestAlertException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import tech.jhipster.web.util.HeaderUtil
import tech.jhipster.web.util.ResponseUtil
import java.net.URI
import java.net.URISyntaxException
import java.util.Objects

private const val ENTITY_NAME = "tablet"
/**
 * REST controller for managing [org.aydm.danak.domain.Tablet].
 */
@RestController
@RequestMapping("/api")
class TabletResource(
    private val tabletService: TabletService,
    private val tabletRepository: TabletRepository,
    private val userFacade: UserFacade
) {

    private val log = LoggerFactory.getLogger(javaClass)

    companion object {
        const val ENTITY_NAME = "tablet"
    }

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /tablets` : Create a new tablet.
     *
     * @param tabletDTO the tabletDTO to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new tabletDTO, or with status `400 (Bad Request)` if the tablet has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tablets")
    fun createTablet(@RequestBody tabletDTO: TabletDTO): ResponseEntity<TabletDTO> {
        log.debug("REST request to save Tablet : $tabletDTO")
        if (tabletDTO.id != null) {
            throw BadRequestAlertException(
                "A new tablet cannot already have an ID",
                ENTITY_NAME, "idexists"
            )
        }
        val result = tabletService.save(tabletDTO)
        return ResponseEntity.created(URI("/api/tablets/${result.id}"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * {@code PUT  /tablets/:id} : Updates an existing tablet.
     *
     * @param id the id of the tabletDTO to save.
     * @param tabletDTO the tabletDTO to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated tabletDTO,
     * or with status `400 (Bad Request)` if the tabletDTO is not valid,
     * or with status `500 (Internal Server Error)` if the tabletDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tablets/{id}")
    fun updateTablet(
        @PathVariable(value = "id", required = false) id: Long,
        @RequestBody tabletDTO: TabletDTO
    ): ResponseEntity<TabletDTO> {
        log.debug("REST request to update Tablet : {}, {}", id, tabletDTO)
        if (tabletDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }

        if (!Objects.equals(id, tabletDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!tabletRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = tabletService.update(tabletDTO)
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName, true, ENTITY_NAME,
                    tabletDTO.id.toString()
                )
            )
            .body(result)
    }

    /**
     * {@code PATCH  /tablets/:id} : Partial updates given fields of an existing tablet, field will ignore if it is null
     *
     * @param id the id of the tabletDTO to save.
     * @param tabletDTO the tabletDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tabletDTO,
     * or with status {@code 400 (Bad Request)} if the tabletDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tabletDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tabletDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = ["/tablets/{id}"], consumes = ["application/json", "application/merge-patch+json"])
    @Throws(URISyntaxException::class)
    fun partialUpdateTablet(
        @PathVariable(value = "id", required = false) id: Long,
        @RequestBody tabletDTO: TabletDTO
    ): ResponseEntity<TabletDTO> {
        log.debug("REST request to partial update Tablet partially : {}, {}", id, tabletDTO)
        if (tabletDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        if (!Objects.equals(id, tabletDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!tabletRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = tabletService.partialUpdate(tabletDTO)

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tabletDTO.id.toString())
        )
    }

    /**
     * `GET  /tablets` : get all the tablets.
     *

     * @return the [ResponseEntity] with status `200 (OK)` and the list of tablets in body.
     */
    @GetMapping("/tablets") fun getAllTablets(): MutableList<TabletDTO> {

        log.debug("REST request to get all Tablets")

        return tabletService.findAll()
    }

    @GetMapping("/tablets-panel") fun getAllTabletsPanel(@org.springdoc.api.annotations.ParameterObject pageable: Pageable): Page<TabletDTO> {

        log.debug("REST request to get all Tablets")

        return userFacade.findAllTablets(pageable)
    }
        /**
     * `GET  /tablets/:id` : get the "id" tablet.
     *
     * @param id the id of the tabletDTO to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the tabletDTO, or with status `404 (Not Found)`.
     */
    @GetMapping("/tablets/{id}")
    fun getTablet(@PathVariable id: Long): ResponseEntity<TabletDTO> {
        log.debug("REST request to get Tablet : $id")
        val tabletDTO = tabletService.findOne(id)
        return ResponseUtil.wrapOrNotFound(tabletDTO)
    }
    /**
     *  `DELETE  /tablets/:id` : delete the "id" tablet.
     *
     * @param id the id of the tabletDTO to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/tablets/{id}")
    fun deleteTablet(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete Tablet : $id")

        tabletService.delete(id)
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build()
    }
}
