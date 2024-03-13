package org.aydm.danak.web.rest

import org.aydm.danak.repository.TabletUserRepository
import org.aydm.danak.service.TabletUserService
import org.aydm.danak.service.criteria.TabletUserCriteria
import org.aydm.danak.service.dto.TabletUserDTO
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

private const val ENTITY_NAME = "tabletUser"

/**
 * REST controller for managing [org.aydm.danak.domain.TabletUser].
 */
@RestController
@RequestMapping("/api")
class TabletUserResource(
    private val tabletUserService: TabletUserService,
    private val userFacade: UserFacade,
    private val tabletUserRepository: TabletUserRepository,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    companion object {
        const val ENTITY_NAME = "tabletUser"
    }

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /tablet-users` : Create a new tabletUser.
     *
     * @param tabletUserDTO the tabletUserDTO to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new tabletUserDTO, or with status `400 (Bad Request)` if the tabletUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tablet-users")
    fun createTabletUser(@RequestBody tabletUserDTO: TabletUserDTO): ResponseEntity<TabletUserDTO> {
        log.debug("REST request to save TabletUser : $tabletUserDTO")
        if (tabletUserDTO.id != null) {
            throw BadRequestAlertException(
                "A new tabletUser cannot already have an ID",
                ENTITY_NAME, "idexists"
            )
        }
        val result = tabletUserService.save(tabletUserDTO)
        return ResponseEntity.created(URI("/api/tablet-users/${result.id}"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * {@code PUT  /tablet-users/:id} : Updates an existing tabletUser.
     *
     * @param id the id of the tabletUserDTO to save.
     * @param tabletUserDTO the tabletUserDTO to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated tabletUserDTO,
     * or with status `400 (Bad Request)` if the tabletUserDTO is not valid,
     * or with status `500 (Internal Server Error)` if the tabletUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tablet-users/{id}")
    fun updateTabletUser(
        @PathVariable(value = "id", required = false) id: Long,
        @RequestBody tabletUserDTO: TabletUserDTO
    ): ResponseEntity<TabletUserDTO> {
        log.debug("REST request to update TabletUser : {}, {}", id, tabletUserDTO)
        if (tabletUserDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }

        if (!Objects.equals(id, tabletUserDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!tabletUserRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = tabletUserService.update(tabletUserDTO)
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName, true, ENTITY_NAME,
                    tabletUserDTO.id.toString()
                )
            )
            .body(result)
    }

    /**
     * {@code PATCH  /tablet-users/:id} : Partial updates given fields of an existing tabletUser, field will ignore if it is null
     *
     * @param id the id of the tabletUserDTO to save.
     * @param tabletUserDTO the tabletUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tabletUserDTO,
     * or with status {@code 400 (Bad Request)} if the tabletUserDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tabletUserDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tabletUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = ["/tablet-users/{id}"], consumes = ["application/json", "application/merge-patch+json"])
    @Throws(URISyntaxException::class)
    fun partialUpdateTabletUser(
        @PathVariable(value = "id", required = false) id: Long,
        @RequestBody tabletUserDTO: TabletUserDTO
    ): ResponseEntity<TabletUserDTO> {
        log.debug("REST request to partial update TabletUser partially : {}, {}", id, tabletUserDTO)
        if (tabletUserDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        if (!Objects.equals(id, tabletUserDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!tabletUserRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = tabletUserService.partialUpdate(tabletUserDTO)

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tabletUserDTO.id.toString())
        )
    }

    /**
     * `GET  /tablet-users` : get all the tabletUsers.
     *

     * @return the [ResponseEntity] with status `200 (OK)` and the list of tabletUsers in body.
     */
    @GetMapping("/tablet-users")
    fun getAllTabletUsers(): Page<TabletUserDTO> {

        log.debug("REST request to get all TabletUsers")

        return tabletUserService.findAll(Pageable.unpaged())
    }

    @GetMapping("/tablet-users-panel")
    fun getAllTabletUsers(
        criteria: TabletUserCriteria?,
        @org.springdoc.api.annotations.ParameterObject pageable: Pageable
    ): Page<TabletUserDTO> {
        log.debug("REST request to get all TabletUsers")
        return userFacade.findAllTabletUsers(criteria, pageable)
    }

    /**
     * `GET  /tablet-users/:id` : get the "id" tabletUser.
     *
     * @param id the id of the tabletUserDTO to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the tabletUserDTO, or with status `404 (Not Found)`.
     */
    @GetMapping("/tablet-users/{id}")
    fun getTabletUser(@PathVariable id: Long): ResponseEntity<TabletUserDTO> {
        log.debug("REST request to get TabletUser : $id")
        val tabletUserDTO = tabletUserService.findOne(id)
        return ResponseUtil.wrapOrNotFound(tabletUserDTO)
    }

    /**
     *  `DELETE  /tablet-users/:id` : delete the "id" tabletUser.
     *
     * @param id the id of the tabletUserDTO to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/tablet-users/{id}")
    fun deleteTabletUser(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete TabletUser : $id")

        tabletUserService.delete(id)
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build()
    }
}
