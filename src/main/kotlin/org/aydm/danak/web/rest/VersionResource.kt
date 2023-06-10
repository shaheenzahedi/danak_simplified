package org.aydm.danak.web.rest

import org.aydm.danak.repository.VersionRepository
import org.aydm.danak.service.VersionService
import org.aydm.danak.service.dto.VersionDTO
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

private const val ENTITY_NAME = "version"
/**
 * REST controller for managing [org.aydm.danak.domain.Version].
 */
@RestController
@RequestMapping("/api")
class VersionResource(
    private val versionService: VersionService,
    private val versionRepository: VersionRepository,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    companion object {
        const val ENTITY_NAME = "version"
    }

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /versions` : Create a new version.
     *
     * @param versionDTO the versionDTO to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new versionDTO, or with status `400 (Bad Request)` if the version has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/versions")
    fun createVersion(@RequestBody versionDTO: VersionDTO): ResponseEntity<VersionDTO> {
        log.debug("REST request to save Version : $versionDTO")
        if (versionDTO.id != null) {
            throw BadRequestAlertException(
                "A new version cannot already have an ID",
                ENTITY_NAME, "idexists"
            )
        }
        val result = versionService.save(versionDTO)
        return ResponseEntity.created(URI("/api/versions/${result.id}"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * {@code PUT  /versions/:id} : Updates an existing version.
     *
     * @param id the id of the versionDTO to save.
     * @param versionDTO the versionDTO to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated versionDTO,
     * or with status `400 (Bad Request)` if the versionDTO is not valid,
     * or with status `500 (Internal Server Error)` if the versionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/versions/{id}")
    fun updateVersion(
        @PathVariable(value = "id", required = false) id: Long,
        @RequestBody versionDTO: VersionDTO
    ): ResponseEntity<VersionDTO> {
        log.debug("REST request to update Version : {}, {}", id, versionDTO)
        if (versionDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }

        if (!Objects.equals(id, versionDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!versionRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = versionService.update(versionDTO)
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName, true, ENTITY_NAME,
                    versionDTO.id.toString()
                )
            )
            .body(result)
    }

    /**
     * {@code PATCH  /versions/:id} : Partial updates given fields of an existing version, field will ignore if it is null
     *
     * @param id the id of the versionDTO to save.
     * @param versionDTO the versionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated versionDTO,
     * or with status {@code 400 (Bad Request)} if the versionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the versionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the versionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = ["/versions/{id}"], consumes = ["application/json", "application/merge-patch+json"])
    @Throws(URISyntaxException::class)
    fun partialUpdateVersion(
        @PathVariable(value = "id", required = false) id: Long,
        @RequestBody versionDTO: VersionDTO
    ): ResponseEntity<VersionDTO> {
        log.debug("REST request to partial update Version partially : {}, {}", id, versionDTO)
        if (versionDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        if (!Objects.equals(id, versionDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!versionRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = versionService.partialUpdate(versionDTO)

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, versionDTO.id.toString())
        )
    }

    /**
     * `GET  /versions` : get all the versions.
     *

     * @return the [ResponseEntity] with status `200 (OK)` and the list of versions in body.
     */
    @GetMapping("/versions") fun getAllVersions(): MutableList<VersionDTO> {

        log.debug("REST request to get all Versions")

        return versionService.findAll()
    }

    /**
     * `GET  /versions/:id` : get the "id" version.
     *
     * @param id the id of the versionDTO to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the versionDTO, or with status `404 (Not Found)`.
     */
    @GetMapping("/versions/{id}")
    fun getVersion(@PathVariable id: Long): ResponseEntity<VersionDTO> {
        log.debug("REST request to get Version : $id")
        val versionDTO = versionService.findOne(id)
        return ResponseUtil.wrapOrNotFound(versionDTO)
    }
    @GetMapping("/versions/last")
    fun getLastVersion(): ResponseEntity<VersionDTO?> {
        log.debug("REST request to get last Version")
        val versionDTO = versionService.findLastVersion()
        return ResponseUtil.wrapOrNotFound(versionDTO)
    }
    /**
     *  `DELETE  /versions/:id` : delete the "id" version.
     *
     * @param id the id of the versionDTO to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/versions/{id}")
    fun deleteVersion(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete Version : $id")

        versionService.delete(id)
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build()
    }
}
