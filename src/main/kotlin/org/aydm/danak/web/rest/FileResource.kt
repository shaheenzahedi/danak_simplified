package org.aydm.danak.web.rest

import org.aydm.danak.repository.FileRepository
import org.aydm.danak.service.FileService
import org.aydm.danak.service.dto.FileDTO
import org.aydm.danak.service.facade.AssetFacade
import org.aydm.danak.service.facade.FileAddress
import org.aydm.danak.service.facade.UpdateResonse
import org.aydm.danak.web.rest.errors.BadRequestAlertException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import tech.jhipster.web.util.HeaderUtil
import tech.jhipster.web.util.ResponseUtil
import java.net.URI
import java.net.URISyntaxException
import java.util.Objects

private const val ENTITY_NAME = "file"
/**
 * REST controller for managing [org.aydm.danak.domain.File].
 */
@RestController
@RequestMapping("/api")
class FileResource(
    private val fileService: FileService,
    private val fileRepository: FileRepository,
    private val assetFacade: AssetFacade
) {

    private val log = LoggerFactory.getLogger(javaClass)

    companion object {
        const val ENTITY_NAME = "file"
    }

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /files` : Create a new file.
     *
     * @param fileDTO the fileDTO to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new fileDTO, or with status `400 (Bad Request)` if the file has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/files")
    fun createFile(@RequestBody fileDTO: FileDTO): ResponseEntity<FileDTO> {
        log.debug("REST request to save File : $fileDTO")
        if (fileDTO.id != null) {
            throw BadRequestAlertException(
                "A new file cannot already have an ID",
                ENTITY_NAME, "idexists"
            )
        }
        val result = fileService.save(fileDTO)
        return ResponseEntity.created(URI("/api/files/${result.id}"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * {@code PUT  /files/:id} : Updates an existing file.
     *
     * @param id the id of the fileDTO to save.
     * @param fileDTO the fileDTO to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated fileDTO,
     * or with status `400 (Bad Request)` if the fileDTO is not valid,
     * or with status `500 (Internal Server Error)` if the fileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/files/{id}")
    fun updateFile(
        @PathVariable(value = "id", required = false) id: Long,
        @RequestBody fileDTO: FileDTO
    ): ResponseEntity<FileDTO> {
        log.debug("REST request to update File : {}, {}", id, fileDTO)
        if (fileDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }

        if (!Objects.equals(id, fileDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!fileRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = fileService.update(fileDTO)
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName, true, ENTITY_NAME,
                    fileDTO.id.toString()
                )
            )
            .body(result)
    }
    @GetMapping("version-assets")
    fun versionAsset(@RequestParam version:Int) {
        assetFacade.versionAsset(version);
    }
    @GetMapping("download-assets")
    fun downloadAssets(@RequestParam version: Int): List<FileAddress>? {
        return assetFacade.download(version);
    }
    @GetMapping("update-assets")
    fun versionAsset(@RequestParam fromVersion:Int,@RequestParam toVersion:Int): UpdateResonse? {
        return assetFacade.updateAssets(fromVersion, toVersion);
    }
    /**
     * {@code PATCH  /files/:id} : Partial updates given fields of an existing file, field will ignore if it is null
     *
     * @param id the id of the fileDTO to save.
     * @param fileDTO the fileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fileDTO,
     * or with status {@code 400 (Bad Request)} if the fileDTO is not valid,
     * or with status {@code 404 (Not Found)} if the fileDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the fileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = ["/files/{id}"], consumes = ["application/json", "application/merge-patch+json"])
    @Throws(URISyntaxException::class)
    fun partialUpdateFile(
        @PathVariable(value = "id", required = false) id: Long,
        @RequestBody fileDTO: FileDTO
    ): ResponseEntity<FileDTO> {
        log.debug("REST request to partial update File partially : {}, {}", id, fileDTO)
        if (fileDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        if (!Objects.equals(id, fileDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!fileRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = fileService.partialUpdate(fileDTO)

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fileDTO.id.toString())
        )
    }

    /**
     * `GET  /files` : get all the files.
     *

     * @return the [ResponseEntity] with status `200 (OK)` and the list of files in body.
     */
    @GetMapping("/files") fun getAllFiles(): MutableList<FileDTO> {

        log.debug("REST request to get all Files")

        return fileService.findAll()
    }

    /**
     * `GET  /files/:id` : get the "id" file.
     *
     * @param id the id of the fileDTO to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the fileDTO, or with status `404 (Not Found)`.
     */
    @GetMapping("/files/{id}")
    fun getFile(@PathVariable id: Long): ResponseEntity<FileDTO> {
        log.debug("REST request to get File : $id")
        val fileDTO = fileService.findOne(id)
        return ResponseUtil.wrapOrNotFound(fileDTO)
    }
    /**
     *  `DELETE  /files/:id` : delete the "id" file.
     *
     * @param id the id of the fileDTO to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/files/{id}")
    fun deleteFile(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete File : $id")

        fileService.delete(id)
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build()
    }
}
