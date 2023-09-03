package org.aydm.danak.web.rest

import org.aydm.danak.repository.CenterRepository
import org.aydm.danak.service.CenterQueryService
import org.aydm.danak.service.CenterService
import org.aydm.danak.service.criteria.CenterCriteria
import org.aydm.danak.service.dto.CenterDTO
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
import java.util.Objects

private const val ENTITY_NAME = "center"
/**
 * REST controller for managing [org.aydm.danak.domain.Center].
 */
@RestController
@RequestMapping("/api")
class CenterResource(
    private val centerService: CenterService,
    private val centerRepository: CenterRepository,
    private val centerQueryService: CenterQueryService,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    companion object {
        const val ENTITY_NAME = "center"
    }

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /centers` : Create a new center.
     *
     * @param centerDTO the centerDTO to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new centerDTO, or with status `400 (Bad Request)` if the center has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/centers")
    fun createCenter(@RequestBody centerDTO: CenterDTO): ResponseEntity<CenterDTO> {
        log.debug("REST request to save Center : $centerDTO")
        if (centerDTO.id != null) {
            throw BadRequestAlertException(
                "A new center cannot already have an ID",
                ENTITY_NAME, "idexists"
            )
        }
        val result = centerService.save(centerDTO)
        return ResponseEntity.created(URI("/api/centers/${result.id}"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * {@code PUT  /centers/:id} : Updates an existing center.
     *
     * @param id the id of the centerDTO to save.
     * @param centerDTO the centerDTO to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated centerDTO,
     * or with status `400 (Bad Request)` if the centerDTO is not valid,
     * or with status `500 (Internal Server Error)` if the centerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/centers/{id}")
    fun updateCenter(
        @PathVariable(value = "id", required = false) id: Long,
        @RequestBody centerDTO: CenterDTO
    ): ResponseEntity<CenterDTO> {
        log.debug("REST request to update Center : {}, {}", id, centerDTO)
        if (centerDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }

        if (!Objects.equals(id, centerDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!centerRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = centerService.update(centerDTO)
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName, true, ENTITY_NAME,
                    centerDTO.id.toString()
                )
            )
            .body(result)
    }

    /**
     * {@code PATCH  /centers/:id} : Partial updates given fields of an existing center, field will ignore if it is null
     *
     * @param id the id of the centerDTO to save.
     * @param centerDTO the centerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated centerDTO,
     * or with status {@code 400 (Bad Request)} if the centerDTO is not valid,
     * or with status {@code 404 (Not Found)} if the centerDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the centerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = ["/centers/{id}"], consumes = ["application/json", "application/merge-patch+json"])
    @Throws(URISyntaxException::class)
    fun partialUpdateCenter(
        @PathVariable(value = "id", required = false) id: Long,
        @RequestBody centerDTO: CenterDTO
    ): ResponseEntity<CenterDTO> {
        log.debug("REST request to partial update Center partially : {}, {}", id, centerDTO)
        if (centerDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        if (!Objects.equals(id, centerDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!centerRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = centerService.partialUpdate(centerDTO)

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, centerDTO.id.toString())
        )
    }

    /**
     * `GET  /centers` : get all the centers.
     *
     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the [ResponseEntity] with status `200 (OK)` and the list of centers in body.
     */
    @GetMapping("/centers") fun getAllCenters(
        criteria: CenterCriteria,
        @org.springdoc.api.annotations.ParameterObject pageable: Pageable

    ): ResponseEntity<MutableList<CenterDTO>> {
        log.debug("REST request to get Centers by criteria: $criteria")
        val page = centerQueryService.findByCriteria(criteria, pageable)
        val headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page)
        return ResponseEntity.ok().headers(headers).body(page.content)
    }

    /**
     * `GET  /centers/count}` : count all the centers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the [ResponseEntity] with status `200 (OK)` and the count in body.
     */
    @GetMapping("/centers/count")
    fun countCenters(criteria: CenterCriteria): ResponseEntity<Long> {
        log.debug("REST request to count Centers by criteria: $criteria")
        return ResponseEntity.ok().body(centerQueryService.countByCriteria(criteria))
    }

    /**
     * `GET  /centers/:id` : get the "id" center.
     *
     * @param id the id of the centerDTO to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the centerDTO, or with status `404 (Not Found)`.
     */
    @GetMapping("/centers/{id}")
    fun getCenter(@PathVariable id: Long): ResponseEntity<CenterDTO> {
        log.debug("REST request to get Center : $id")
        val centerDTO = centerService.findOne(id)
        return ResponseUtil.wrapOrNotFound(centerDTO)
    }
    /**
     *  `DELETE  /centers/:id` : delete the "id" center.
     *
     * @param id the id of the centerDTO to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/centers/{id}")
    fun deleteCenter(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete Center : $id")

        centerService.delete(id)
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build()
    }
}
