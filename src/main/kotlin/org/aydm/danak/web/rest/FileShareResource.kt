package org.aydm.danak.web.rest

import org.aydm.danak.repository.FileShareRepository
import org.aydm.danak.service.FileShareQueryService
import org.aydm.danak.service.FileShareService
import org.aydm.danak.service.criteria.FileShareCriteria
import org.aydm.danak.service.dto.FileShareDTO
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

private const val ENTITY_NAME = "fileShare"

/**
 * REST controller for managing [org.aydm.danak.domain.FileShare].
 */
@RestController
@RequestMapping("/api")
class FileShareResource(
    private val fileShareService: FileShareService,
    private val fileShareRepository: FileShareRepository,
    private val fileShareQueryService: FileShareQueryService,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    companion object {
        const val ENTITY_NAME = "fileShare"
    }

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /file-shares` : Create a new fileShare.
     *
     * @param fileShareDTO the fileShareDTO to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new fileShareDTO, or with status `400 (Bad Request)` if the fileShare has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/file-shares")
    fun createFileShare(@RequestBody fileShareDTO: FileShareDTO): ResponseEntity<FileShareDTO> {
        log.debug("REST request to save FileShare : $fileShareDTO")
        if (fileShareDTO.id != null) {
            throw BadRequestAlertException(
                "A new fileShare cannot already have an ID",
                ENTITY_NAME, "idexists"
            )
        }
        val result = fileShareService.save(fileShareDTO)
        return ResponseEntity.created(URI("/api/file-shares/${result.id}"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * {@code PUT  /file-shares/:id} : Updates an existing fileShare.
     *
     * @param id the id of the fileShareDTO to save.
     * @param fileShareDTO the fileShareDTO to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated fileShareDTO,
     * or with status `400 (Bad Request)` if the fileShareDTO is not valid,
     * or with status `500 (Internal Server Error)` if the fileShareDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/file-shares/{id}")
    fun updateFileShare(
        @PathVariable(value = "id", required = false) id: Long,
        @RequestBody fileShareDTO: FileShareDTO
    ): ResponseEntity<FileShareDTO> {
        log.debug("REST request to update FileShare : {}, {}", id, fileShareDTO)
        if (fileShareDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }

        if (!Objects.equals(id, fileShareDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!fileShareRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = fileShareService.update(fileShareDTO)
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName, true, ENTITY_NAME,
                    fileShareDTO.id.toString()
                )
            )
            .body(result)
    }

    /**
     * {@code PATCH  /file-shares/:id} : Partial updates given fields of an existing fileShare, field will ignore if it is null
     *
     * @param id the id of the fileShareDTO to save.
     * @param fileShareDTO the fileShareDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fileShareDTO,
     * or with status {@code 400 (Bad Request)} if the fileShareDTO is not valid,
     * or with status {@code 404 (Not Found)} if the fileShareDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the fileShareDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = ["/file-shares/{id}"], consumes = ["application/json", "application/merge-patch+json"])
    @Throws(URISyntaxException::class)
    fun partialUpdateFileShare(
        @PathVariable(value = "id", required = false) id: Long,
        @RequestBody fileShareDTO: FileShareDTO
    ): ResponseEntity<FileShareDTO> {
        log.debug("REST request to partial update FileShare partially : {}, {}", id, fileShareDTO)
        if (fileShareDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        if (!Objects.equals(id, fileShareDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!fileShareRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = fileShareService.partialUpdate(fileShareDTO)

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fileShareDTO.id.toString())
        )
    }

    /**
     * `GET  /file-shares` : get all the fileShares.
     *
     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the [ResponseEntity] with status `200 (OK)` and the list of fileShares in body.
     */
    @GetMapping("/file-shares")
    fun getAllFileShares(
        criteria: FileShareCriteria,
        @org.springdoc.api.annotations.ParameterObject pageable: Pageable

    ): ResponseEntity<MutableList<FileShareDTO>> {
        log.debug("REST request to get FileShares by criteria: $criteria")
        val page = fileShareQueryService.findByCriteria(criteria, pageable)
        val headers =
            PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page)
        return ResponseEntity.ok().headers(headers).body(page.content)
    }

    /**
     * `GET  /file-shares/count}` : count all the fileShares.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the [ResponseEntity] with status `200 (OK)` and the count in body.
     */
    @GetMapping("/file-shares/count")
    fun countFileShares(criteria: FileShareCriteria): ResponseEntity<Long> {
        log.debug("REST request to count FileShares by criteria: $criteria")
        return ResponseEntity.ok().body(fileShareQueryService.countByCriteria(criteria))
    }

    /**
     * `GET  /file-shares/:id` : get the "id" fileShare.
     *
     * @param id the id of the fileShareDTO to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the fileShareDTO, or with status `404 (Not Found)`.
     */
    @GetMapping("/file-shares/{id}")
    fun getFileShare(@PathVariable id: Long): ResponseEntity<FileShareDTO> {
        log.debug("REST request to get FileShare : $id")
        val fileShareDTO = fileShareService.findOne(id)
        return ResponseUtil.wrapOrNotFound(fileShareDTO)
    }

    /**
     *  `DELETE  /file-shares/:id` : delete the "id" fileShare.
     *
     * @param id the id of the fileShareDTO to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/file-shares/{id}")
    fun deleteFileShare(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete FileShare : $id")

        fileShareService.delete(id)
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build()
    }
}
