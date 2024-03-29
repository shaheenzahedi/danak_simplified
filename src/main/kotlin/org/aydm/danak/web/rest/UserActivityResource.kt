package org.aydm.danak.web.rest

import org.aydm.danak.repository.UserActivityRepository
import org.aydm.danak.service.UserActivityService
import org.aydm.danak.service.dto.OverallUserActivities
import org.aydm.danak.service.dto.UserActivityDTO
import org.aydm.danak.web.rest.errors.BadRequestAlertException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.web.danak.service.dto.SubmitDTO
import tech.jhipster.web.util.HeaderUtil
import tech.jhipster.web.util.ResponseUtil
import java.net.URI
import java.net.URISyntaxException
import java.util.Objects

private const val ENTITY_NAME = "userActivity"
/**
 * REST controller for managing [org.aydm.danak.domain.UserActivity].
 */
@RestController
@RequestMapping("/api")
class UserActivityResource(
    private val userActivityService: UserActivityService,
    private val userActivityRepository: UserActivityRepository,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    companion object {
        const val ENTITY_NAME = "userActivity"
    }

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    @PostMapping("submit-activity")
    fun submitActivity(@RequestBody submitDTO: SubmitDTO): ResponseEntity<Void> {
        userActivityService.submit(submitDTO)
        return ResponseEntity.ok().build()
    }

    @GetMapping("all-activities-by-tablet")
    fun getAllActivityByTablet(): ResponseEntity<List<SubmitDTO>> {
        val allActivity = userActivityService.getAllActivityByTablet()
        return ResponseEntity.ok().body(allActivity)
    }
    @GetMapping("all-activities-by-user")
    fun getAllActivityByUser(): ResponseEntity<List<OverallUserActivities>> {
        val allActivity = userActivityService.getAllActivityByUser()
        return ResponseEntity.ok().body(allActivity)
    }

    /**
     * `POST  /user-activities` : Create a new userActivity.
     *
     * @param userActivityDTO the userActivityDTO to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new userActivityDTO, or with status `400 (Bad Request)` if the userActivity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-activities")
    fun createUserActivity(@RequestBody userActivityDTO: UserActivityDTO): ResponseEntity<UserActivityDTO> {
        log.debug("REST request to save UserActivity : $userActivityDTO")
        if (userActivityDTO.id != null) {
            throw BadRequestAlertException(
                "A new userActivity cannot already have an ID",
                ENTITY_NAME, "idexists"
            )
        }
        val result = userActivityService.save(userActivityDTO)
        return ResponseEntity.created(URI("/api/user-activities/${result.id}"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * {@code PUT  /user-activities/:id} : Updates an existing userActivity.
     *
     * @param id the id of the userActivityDTO to save.
     * @param userActivityDTO the userActivityDTO to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated userActivityDTO,
     * or with status `400 (Bad Request)` if the userActivityDTO is not valid,
     * or with status `500 (Internal Server Error)` if the userActivityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-activities/{id}")
    fun updateUserActivity(
        @PathVariable(value = "id", required = false) id: Long,
        @RequestBody userActivityDTO: UserActivityDTO
    ): ResponseEntity<UserActivityDTO> {
        log.debug("REST request to update UserActivity : {}, {}", id, userActivityDTO)
        if (userActivityDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }

        if (!Objects.equals(id, userActivityDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!userActivityRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = userActivityService.update(userActivityDTO)
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName, true, ENTITY_NAME,
                    userActivityDTO.id.toString()
                )
            )
            .body(result)
    }

    /**
     * {@code PATCH  /user-activities/:id} : Partial updates given fields of an existing userActivity, field will ignore if it is null
     *
     * @param id the id of the userActivityDTO to save.
     * @param userActivityDTO the userActivityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userActivityDTO,
     * or with status {@code 400 (Bad Request)} if the userActivityDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userActivityDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userActivityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = ["/user-activities/{id}"], consumes = ["application/json", "application/merge-patch+json"])
    @Throws(URISyntaxException::class)
    fun partialUpdateUserActivity(
        @PathVariable(value = "id", required = false) id: Long,
        @RequestBody userActivityDTO: UserActivityDTO
    ): ResponseEntity<UserActivityDTO> {
        log.debug("REST request to partial update UserActivity partially : {}, {}", id, userActivityDTO)
        if (userActivityDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        if (!Objects.equals(id, userActivityDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!userActivityRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = userActivityService.partialUpdate(userActivityDTO)

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userActivityDTO.id.toString())
        )
    }

    /**
     * `GET  /user-activities` : get all the userActivities.
     *

     * @return the [ResponseEntity] with status `200 (OK)` and the list of userActivities in body.
     */
    @GetMapping("/user-activities")
    fun getAllUserActivities(): MutableList<UserActivityDTO> {

        log.debug("REST request to get all UserActivities")

        return userActivityService.findAll()
    }

    /**
     * `GET  /user-activities/:id` : get the "id" userActivity.
     *
     * @param id the id of the userActivityDTO to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the userActivityDTO, or with status `404 (Not Found)`.
     */
    @GetMapping("/user-activities/{id}")
    fun getUserActivity(@PathVariable id: Long): ResponseEntity<UserActivityDTO> {
        log.debug("REST request to get UserActivity : $id")
        val userActivityDTO = userActivityService.findOne(id)
        return ResponseUtil.wrapOrNotFound(userActivityDTO)
    }
    /**
     *  `DELETE  /user-activities/:id` : delete the "id" userActivity.
     *
     * @param id the id of the userActivityDTO to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/user-activities/{id}")
    fun deleteUserActivity(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete UserActivity : $id")

        userActivityService.delete(id)
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build()
    }
}
