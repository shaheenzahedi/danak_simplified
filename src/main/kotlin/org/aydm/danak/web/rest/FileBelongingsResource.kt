package org.aydm.danak.web.rest

import org.aydm.danak.repository.FileBelongingsRepository
import org.aydm.danak.service.FileBelongingsService
import org.aydm.danak.web.rest.errors.BadRequestAlertException
import org.aydm.danak.service.dto.FileBelongingsDTO

import tech.jhipster.web.util.HeaderUtil
import tech.jhipster.web.util.ResponseUtil
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

import java.net.URI
import java.net.URISyntaxException
import java.util.Objects

private const val ENTITY_NAME = "fileBelongings"
/**
 * REST controller for managing [org.aydm.danak.domain.FileBelongings].
 */
@RestController
@RequestMapping("/api")
class FileBelongingsResource(
        private val fileBelongingsService: FileBelongingsService,
        private val fileBelongingsRepository: FileBelongingsRepository,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    companion object {
        const val ENTITY_NAME = "fileBelongings"
    }

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /file-belongings` : Create a new fileBelongings.
     *
     * @param fileBelongingsDTO the fileBelongingsDTO to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new fileBelongingsDTO, or with status `400 (Bad Request)` if the fileBelongings has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/file-belongings")
    fun createFileBelongings(@RequestBody fileBelongingsDTO: FileBelongingsDTO): ResponseEntity<FileBelongingsDTO> {
        log.debug("REST request to save FileBelongings : $fileBelongingsDTO")
        if (fileBelongingsDTO.id != null) {
            throw BadRequestAlertException(
                "A new fileBelongings cannot already have an ID",
                ENTITY_NAME, "idexists"
            )
        }
        val result = fileBelongingsService.save(fileBelongingsDTO)
            return ResponseEntity.created(URI("/api/file-belongings/${result.id}"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * {@code PUT  /file-belongings/:id} : Updates an existing fileBelongings.
     *
     * @param id the id of the fileBelongingsDTO to save.
     * @param fileBelongingsDTO the fileBelongingsDTO to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated fileBelongingsDTO,
     * or with status `400 (Bad Request)` if the fileBelongingsDTO is not valid,
     * or with status `500 (Internal Server Error)` if the fileBelongingsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/file-belongings/{id}")
    fun updateFileBelongings(
        @PathVariable(value = "id", required = false) id: Long,
        @RequestBody fileBelongingsDTO: FileBelongingsDTO
    ): ResponseEntity<FileBelongingsDTO> {
        log.debug("REST request to update FileBelongings : {}, {}", id, fileBelongingsDTO)
        if (fileBelongingsDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }

        if (!Objects.equals(id, fileBelongingsDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }


        if (!fileBelongingsRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = fileBelongingsService.update(fileBelongingsDTO)
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName, true, ENTITY_NAME,
                     fileBelongingsDTO.id.toString()
                )
            )
            .body(result)
    }

    /**
    * {@code PATCH  /file-belongings/:id} : Partial updates given fields of an existing fileBelongings, field will ignore if it is null
    *
    * @param id the id of the fileBelongingsDTO to save.
    * @param fileBelongingsDTO the fileBelongingsDTO to update.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fileBelongingsDTO,
    * or with status {@code 400 (Bad Request)} if the fileBelongingsDTO is not valid,
    * or with status {@code 404 (Not Found)} if the fileBelongingsDTO is not found,
    * or with status {@code 500 (Internal Server Error)} if the fileBelongingsDTO couldn't be updated.
    * @throws URISyntaxException if the Location URI syntax is incorrect.
    */
    @PatchMapping(value = ["/file-belongings/{id}"], consumes = ["application/json", "application/merge-patch+json"])
    @Throws(URISyntaxException::class)
    fun partialUpdateFileBelongings(
        @PathVariable(value = "id", required = false) id: Long,
        @RequestBody fileBelongingsDTO:FileBelongingsDTO
    ): ResponseEntity<FileBelongingsDTO> {
        log.debug("REST request to partial update FileBelongings partially : {}, {}", id, fileBelongingsDTO)
        if (fileBelongingsDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        if (!Objects.equals(id, fileBelongingsDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!fileBelongingsRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }



            val result = fileBelongingsService.partialUpdate(fileBelongingsDTO)

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fileBelongingsDTO.id.toString())
        )
    }

    /**
     * `GET  /file-belongings` : get all the fileBelongings.
     *

     * @return the [ResponseEntity] with status `200 (OK)` and the list of fileBelongings in body.
     */
    @GetMapping("/file-belongings")    
    fun getAllFileBelongings(): MutableList<FileBelongingsDTO> {
        
        

            log.debug("REST request to get all FileBelongings")
            
            return fileBelongingsService.findAll()
                }

    /**
     * `GET  /file-belongings/:id` : get the "id" fileBelongings.
     *
     * @param id the id of the fileBelongingsDTO to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the fileBelongingsDTO, or with status `404 (Not Found)`.
     */
    @GetMapping("/file-belongings/{id}")
    fun getFileBelongings(@PathVariable id: Long): ResponseEntity<FileBelongingsDTO> {
        log.debug("REST request to get FileBelongings : $id")
        val fileBelongingsDTO = fileBelongingsService.findOne(id)
        return ResponseUtil.wrapOrNotFound(fileBelongingsDTO)
    }
    /**
     *  `DELETE  /file-belongings/:id` : delete the "id" fileBelongings.
     *
     * @param id the id of the fileBelongingsDTO to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/file-belongings/{id}")
    fun deleteFileBelongings(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete FileBelongings : $id")

        fileBelongingsService.delete(id)
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build()
    }
}
