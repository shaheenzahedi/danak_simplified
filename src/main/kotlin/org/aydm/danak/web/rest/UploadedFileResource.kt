package org.aydm.danak.web.rest

import org.aydm.danak.repository.UploadedFileRepository
import org.aydm.danak.service.UploadedFileQueryService
import org.aydm.danak.service.UploadedFileService
import org.aydm.danak.service.criteria.UploadedFileCriteria
import org.aydm.danak.service.dto.UploadedFileDTO
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

private const val ENTITY_NAME = "uploadedFile"

/**
 * REST controller for managing [org.aydm.danak.domain.UploadedFile].
 */
@RestController
@RequestMapping("/api")
class UploadedFileResource(
    private val uploadedFileService: UploadedFileService,
    private val uploadedFileRepository: UploadedFileRepository,
    private val uploadedFileQueryService: UploadedFileQueryService,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    companion object {
        const val ENTITY_NAME = "uploadedFile"
    }

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /uploaded-files` : Create a new uploadedFile.
     *
     * @param uploadedFileDTO the uploadedFileDTO to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new uploadedFileDTO, or with status `400 (Bad Request)` if the uploadedFile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/uploaded-files")
    fun createUploadedFile(@RequestBody uploadedFileDTO: UploadedFileDTO): ResponseEntity<UploadedFileDTO> {
        log.debug("REST request to save UploadedFile : $uploadedFileDTO")
        if (uploadedFileDTO.id != null) {
            throw BadRequestAlertException(
                "A new uploadedFile cannot already have an ID",
                ENTITY_NAME, "idexists"
            )
        }
        val result = uploadedFileService.save(uploadedFileDTO)
        return ResponseEntity.created(URI("/api/uploaded-files/${result.id}"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * {@code PUT  /uploaded-files/:id} : Updates an existing uploadedFile.
     *
     * @param id the id of the uploadedFileDTO to save.
     * @param uploadedFileDTO the uploadedFileDTO to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated uploadedFileDTO,
     * or with status `400 (Bad Request)` if the uploadedFileDTO is not valid,
     * or with status `500 (Internal Server Error)` if the uploadedFileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/uploaded-files/{id}")
    fun updateUploadedFile(
        @PathVariable(value = "id", required = false) id: Long,
        @RequestBody uploadedFileDTO: UploadedFileDTO
    ): ResponseEntity<UploadedFileDTO> {
        log.debug("REST request to update UploadedFile : {}, {}", id, uploadedFileDTO)
        if (uploadedFileDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }

        if (!Objects.equals(id, uploadedFileDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!uploadedFileRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = uploadedFileService.update(uploadedFileDTO)
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName, true, ENTITY_NAME,
                    uploadedFileDTO.id.toString()
                )
            )
            .body(result)
    }

    /**
     * {@code PATCH  /uploaded-files/:id} : Partial updates given fields of an existing uploadedFile, field will ignore if it is null
     *
     * @param id the id of the uploadedFileDTO to save.
     * @param uploadedFileDTO the uploadedFileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uploadedFileDTO,
     * or with status {@code 400 (Bad Request)} if the uploadedFileDTO is not valid,
     * or with status {@code 404 (Not Found)} if the uploadedFileDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the uploadedFileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = ["/uploaded-files/{id}"], consumes = ["application/json", "application/merge-patch+json"])
    @Throws(URISyntaxException::class)
    fun partialUpdateUploadedFile(
        @PathVariable(value = "id", required = false) id: Long,
        @RequestBody uploadedFileDTO: UploadedFileDTO
    ): ResponseEntity<UploadedFileDTO> {
        log.debug("REST request to partial update UploadedFile partially : {}, {}", id, uploadedFileDTO)
        if (uploadedFileDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        if (!Objects.equals(id, uploadedFileDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!uploadedFileRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = uploadedFileService.partialUpdate(uploadedFileDTO)

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, uploadedFileDTO.id.toString())
        )
    }

    /**
     * `GET  /uploaded-files` : get all the uploadedFiles.
     *
     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the [ResponseEntity] with status `200 (OK)` and the list of uploadedFiles in body.
     */
    @GetMapping("/uploaded-files")
    fun getAllUploadedFiles(
        criteria: UploadedFileCriteria,
        @org.springdoc.api.annotations.ParameterObject pageable: Pageable

    ): ResponseEntity<MutableList<UploadedFileDTO>> {
        log.debug("REST request to get UploadedFiles by criteria: $criteria")
        val page = uploadedFileQueryService.findByCriteria(criteria, pageable)
        val headers =
            PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page)
        return ResponseEntity.ok().headers(headers).body(page.content)
    }

    /**
     * `GET  /uploaded-files/count}` : count all the uploadedFiles.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the [ResponseEntity] with status `200 (OK)` and the count in body.
     */
    @GetMapping("/uploaded-files/count")
    fun countUploadedFiles(criteria: UploadedFileCriteria): ResponseEntity<Long> {
        log.debug("REST request to count UploadedFiles by criteria: $criteria")
        return ResponseEntity.ok().body(uploadedFileQueryService.countByCriteria(criteria))
    }

    /**
     * `GET  /uploaded-files/:id` : get the "id" uploadedFile.
     *
     * @param id the id of the uploadedFileDTO to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the uploadedFileDTO, or with status `404 (Not Found)`.
     */
    @GetMapping("/uploaded-files/{id}")
    fun getUploadedFile(@PathVariable id: Long): ResponseEntity<UploadedFileDTO> {
        log.debug("REST request to get UploadedFile : $id")
        val uploadedFileDTO = uploadedFileService.findOne(id)
        return ResponseUtil.wrapOrNotFound(uploadedFileDTO)
    }

    /**
     *  `DELETE  /uploaded-files/:id` : delete the "id" uploadedFile.
     *
     * @param id the id of the uploadedFileDTO to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/uploaded-files/{id}")
    fun deleteUploadedFile(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete UploadedFile : $id")

        uploadedFileService.delete(id)
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build()
    }
}
