package org.aydm.danak.web.rest

import org.assertj.core.api.Assertions.assertThat
import org.aydm.danak.IntegrationTest
import org.aydm.danak.domain.File
import org.aydm.danak.domain.FileBelongings
import org.aydm.danak.domain.Version
import org.aydm.danak.repository.FileBelongingsRepository
import org.aydm.danak.service.mapper.FileBelongingsMapper
import org.hamcrest.Matchers.hasItem
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.Validator
import java.util.Random
import java.util.concurrent.atomic.AtomicLong
import javax.persistence.EntityManager
import kotlin.test.assertNotNull

/**
 * Integration tests for the [FileBelongingsResource] REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FileBelongingsResourceIT {
    @Autowired
    private lateinit var fileBelongingsRepository: FileBelongingsRepository

    @Autowired
    private lateinit var fileBelongingsMapper: FileBelongingsMapper

    @Autowired
    private lateinit var jacksonMessageConverter: MappingJackson2HttpMessageConverter

    @Autowired
    private lateinit var pageableArgumentResolver: PageableHandlerMethodArgumentResolver

    @Autowired
    private lateinit var validator: Validator

    @Autowired
    private lateinit var em: EntityManager

    @Autowired
    private lateinit var restFileBelongingsMockMvc: MockMvc

    private lateinit var fileBelongings: FileBelongings

    @BeforeEach
    fun initTest() {
        fileBelongings = createEntity(em)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createFileBelongings() {
        val databaseSizeBeforeCreate = fileBelongingsRepository.findAll().size
        // Create the FileBelongings
        val fileBelongingsDTO = fileBelongingsMapper.toDto(fileBelongings)
        restFileBelongingsMockMvc.perform(
            post(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(fileBelongingsDTO))
        ).andExpect(status().isCreated)

        // Validate the FileBelongings in the database
        val fileBelongingsList = fileBelongingsRepository.findAll()
        assertThat(fileBelongingsList).hasSize(databaseSizeBeforeCreate + 1)
        val testFileBelongings = fileBelongingsList[fileBelongingsList.size - 1]
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createFileBelongingsWithExistingId() {
        // Create the FileBelongings with an existing ID
        fileBelongings.id = 1L
        val fileBelongingsDTO = fileBelongingsMapper.toDto(fileBelongings)

        val databaseSizeBeforeCreate = fileBelongingsRepository.findAll().size

        // An entity with an existing ID cannot be created, so this API call must fail
        restFileBelongingsMockMvc.perform(
            post(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(fileBelongingsDTO))
        ).andExpect(status().isBadRequest)

        // Validate the FileBelongings in the database
        val fileBelongingsList = fileBelongingsRepository.findAll()
        assertThat(fileBelongingsList).hasSize(databaseSizeBeforeCreate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFileBelongings() {
        // Initialize the database
        fileBelongingsRepository.saveAndFlush(fileBelongings)

        // Get all the fileBelongingsList
        restFileBelongingsMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fileBelongings.id?.toInt())))
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getFileBelongings() {
        // Initialize the database
        fileBelongingsRepository.saveAndFlush(fileBelongings)

        val id = fileBelongings.id
        assertNotNull(id)

        // Get the fileBelongings
        restFileBelongingsMockMvc.perform(get(ENTITY_API_URL_ID, fileBelongings.id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fileBelongings.id?.toInt()))
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getFileBelongingsByIdFiltering() {
        // Initialize the database
        fileBelongingsRepository.saveAndFlush(fileBelongings)
        val id = fileBelongings.id

        defaultFileBelongingsShouldBeFound("id.equals=$id")
        defaultFileBelongingsShouldNotBeFound("id.notEquals=$id")
        defaultFileBelongingsShouldBeFound("id.greaterThanOrEqual=$id")
        defaultFileBelongingsShouldNotBeFound("id.greaterThan=$id")

        defaultFileBelongingsShouldBeFound("id.lessThanOrEqual=$id")
        defaultFileBelongingsShouldNotBeFound("id.lessThan=$id")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFileBelongingsByFileIsEqualToSomething() {
        // Initialize the database
        fileBelongingsRepository.saveAndFlush(fileBelongings)
        var file: File
        if (findAll(em, File::class).isEmpty()) {
            file = FileResourceIT.createEntity(em)
            em.persist(file)
            em.flush()
        } else {
            file = findAll(em, File::class)[0]
        }
        em.persist(file)
        em.flush()
        fileBelongings.file = file
        fileBelongingsRepository.saveAndFlush(fileBelongings)
        val fileId = file?.id

        // Get all the fileBelongingsList where file equals to fileId
        defaultFileBelongingsShouldBeFound("fileId.equals=$fileId")

        // Get all the fileBelongingsList where file equals to (fileId?.plus(1))
        defaultFileBelongingsShouldNotBeFound("fileId.equals=${(fileId?.plus(1))}")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFileBelongingsByVersionIsEqualToSomething() {
        // Initialize the database
        fileBelongingsRepository.saveAndFlush(fileBelongings)
        var version: Version
        if (findAll(em, Version::class).isEmpty()) {
            version = VersionResourceIT.createEntity(em)
            em.persist(version)
            em.flush()
        } else {
            version = findAll(em, Version::class)[0]
        }
        em.persist(version)
        em.flush()
        fileBelongings.version = version
        fileBelongingsRepository.saveAndFlush(fileBelongings)
        val versionId = version?.id

        // Get all the fileBelongingsList where version equals to versionId
        defaultFileBelongingsShouldBeFound("versionId.equals=$versionId")

        // Get all the fileBelongingsList where version equals to (versionId?.plus(1))
        defaultFileBelongingsShouldNotBeFound("versionId.equals=${(versionId?.plus(1))}")
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */

    @Throws(Exception::class)
    private fun defaultFileBelongingsShouldBeFound(filter: String) {
        restFileBelongingsMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fileBelongings.id?.toInt())))

        // Check, that the count call also returns 1
        restFileBelongingsMockMvc.perform(get(ENTITY_API_URL + "/count?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"))
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    @Throws(Exception::class)
    private fun defaultFileBelongingsShouldNotBeFound(filter: String) {
        restFileBelongingsMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$").isEmpty)

        // Check, that the count call also returns 0
        restFileBelongingsMockMvc.perform(get(ENTITY_API_URL + "/count?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"))
    }
    @Test
    @Transactional
    @Throws(Exception::class)
    fun getNonExistingFileBelongings() {
        // Get the fileBelongings
        restFileBelongingsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }
    @Test
    @Transactional
    fun putNewFileBelongings() {
        // Initialize the database
        fileBelongingsRepository.saveAndFlush(fileBelongings)

        val databaseSizeBeforeUpdate = fileBelongingsRepository.findAll().size

        // Update the fileBelongings
        val updatedFileBelongings = fileBelongingsRepository.findById(fileBelongings.id).get()
        // Disconnect from session so that the updates on updatedFileBelongings are not directly saved in db
        em.detach(updatedFileBelongings)
        val fileBelongingsDTO = fileBelongingsMapper.toDto(updatedFileBelongings)

        restFileBelongingsMockMvc.perform(
            put(ENTITY_API_URL_ID, fileBelongingsDTO.id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(fileBelongingsDTO))
        ).andExpect(status().isOk)

        // Validate the FileBelongings in the database
        val fileBelongingsList = fileBelongingsRepository.findAll()
        assertThat(fileBelongingsList).hasSize(databaseSizeBeforeUpdate)
        val testFileBelongings = fileBelongingsList[fileBelongingsList.size - 1]
    }

    @Test
    @Transactional
    fun putNonExistingFileBelongings() {
        val databaseSizeBeforeUpdate = fileBelongingsRepository.findAll().size
        fileBelongings.id = count.incrementAndGet()

        // Create the FileBelongings
        val fileBelongingsDTO = fileBelongingsMapper.toDto(fileBelongings)

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFileBelongingsMockMvc.perform(
            put(ENTITY_API_URL_ID, fileBelongingsDTO.id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(fileBelongingsDTO))
        )
            .andExpect(status().isBadRequest)

        // Validate the FileBelongings in the database
        val fileBelongingsList = fileBelongingsRepository.findAll()
        assertThat(fileBelongingsList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun putWithIdMismatchFileBelongings() {
        val databaseSizeBeforeUpdate = fileBelongingsRepository.findAll().size
        fileBelongings.id = count.incrementAndGet()

        // Create the FileBelongings
        val fileBelongingsDTO = fileBelongingsMapper.toDto(fileBelongings)

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileBelongingsMockMvc.perform(
            put(ENTITY_API_URL_ID, count.incrementAndGet())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(fileBelongingsDTO))
        ).andExpect(status().isBadRequest)

        // Validate the FileBelongings in the database
        val fileBelongingsList = fileBelongingsRepository.findAll()
        assertThat(fileBelongingsList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun putWithMissingIdPathParamFileBelongings() {
        val databaseSizeBeforeUpdate = fileBelongingsRepository.findAll().size
        fileBelongings.id = count.incrementAndGet()

        // Create the FileBelongings
        val fileBelongingsDTO = fileBelongingsMapper.toDto(fileBelongings)

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileBelongingsMockMvc.perform(
            put(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(fileBelongingsDTO))
        )
            .andExpect(status().isMethodNotAllowed)

        // Validate the FileBelongings in the database
        val fileBelongingsList = fileBelongingsRepository.findAll()
        assertThat(fileBelongingsList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun partialUpdateFileBelongingsWithPatch() {
        fileBelongingsRepository.saveAndFlush(fileBelongings)

        val databaseSizeBeforeUpdate = fileBelongingsRepository.findAll().size

// Update the fileBelongings using partial update
        val partialUpdatedFileBelongings = FileBelongings().apply {
            id = fileBelongings.id
        }

        restFileBelongingsMockMvc.perform(
            patch(ENTITY_API_URL_ID, partialUpdatedFileBelongings.id)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(partialUpdatedFileBelongings))
        )
            .andExpect(status().isOk)

// Validate the FileBelongings in the database
        val fileBelongingsList = fileBelongingsRepository.findAll()
        assertThat(fileBelongingsList).hasSize(databaseSizeBeforeUpdate)
        val testFileBelongings = fileBelongingsList.last()
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun fullUpdateFileBelongingsWithPatch() {
        fileBelongingsRepository.saveAndFlush(fileBelongings)

        val databaseSizeBeforeUpdate = fileBelongingsRepository.findAll().size

// Update the fileBelongings using partial update
        val partialUpdatedFileBelongings = FileBelongings().apply {
            id = fileBelongings.id
        }

        restFileBelongingsMockMvc.perform(
            patch(ENTITY_API_URL_ID, partialUpdatedFileBelongings.id)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(partialUpdatedFileBelongings))
        )
            .andExpect(status().isOk)

// Validate the FileBelongings in the database
        val fileBelongingsList = fileBelongingsRepository.findAll()
        assertThat(fileBelongingsList).hasSize(databaseSizeBeforeUpdate)
        val testFileBelongings = fileBelongingsList.last()
    }

    @Throws(Exception::class)
    fun patchNonExistingFileBelongings() {
        val databaseSizeBeforeUpdate = fileBelongingsRepository.findAll().size
        fileBelongings.id = count.incrementAndGet()

        // Create the FileBelongings
        val fileBelongingsDTO = fileBelongingsMapper.toDto(fileBelongings)

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFileBelongingsMockMvc.perform(
            patch(ENTITY_API_URL_ID, fileBelongingsDTO.id)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(fileBelongingsDTO))
        )
            .andExpect(status().isBadRequest)

        // Validate the FileBelongings in the database
        val fileBelongingsList = fileBelongingsRepository.findAll()
        assertThat(fileBelongingsList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun patchWithIdMismatchFileBelongings() {
        val databaseSizeBeforeUpdate = fileBelongingsRepository.findAll().size
        fileBelongings.id = count.incrementAndGet()

        // Create the FileBelongings
        val fileBelongingsDTO = fileBelongingsMapper.toDto(fileBelongings)

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileBelongingsMockMvc.perform(
            patch(ENTITY_API_URL_ID, count.incrementAndGet())
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(fileBelongingsDTO))
        )
            .andExpect(status().isBadRequest)

        // Validate the FileBelongings in the database
        val fileBelongingsList = fileBelongingsRepository.findAll()
        assertThat(fileBelongingsList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun patchWithMissingIdPathParamFileBelongings() {
        val databaseSizeBeforeUpdate = fileBelongingsRepository.findAll().size
        fileBelongings.id = count.incrementAndGet()

        // Create the FileBelongings
        val fileBelongingsDTO = fileBelongingsMapper.toDto(fileBelongings)

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileBelongingsMockMvc.perform(
            patch(ENTITY_API_URL)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(fileBelongingsDTO))
        )
            .andExpect(status().isMethodNotAllowed)

        // Validate the FileBelongings in the database
        val fileBelongingsList = fileBelongingsRepository.findAll()
        assertThat(fileBelongingsList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun deleteFileBelongings() {
        // Initialize the database
        fileBelongingsRepository.saveAndFlush(fileBelongings)

        val databaseSizeBeforeDelete = fileBelongingsRepository.findAll().size

        // Delete the fileBelongings
        restFileBelongingsMockMvc.perform(
            delete(ENTITY_API_URL_ID, fileBelongings.id)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val fileBelongingsList = fileBelongingsRepository.findAll()
        assertThat(fileBelongingsList).hasSize(databaseSizeBeforeDelete - 1)
    }

    companion object {

        private val ENTITY_API_URL: String = "/api/file-belongings"
        private val ENTITY_API_URL_ID: String = ENTITY_API_URL + "/{id}"

        private val random: Random = Random()
        private val count: AtomicLong = AtomicLong(random.nextInt().toLong() + (2 * Integer.MAX_VALUE))

        /**
         * Create an entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createEntity(em: EntityManager): FileBelongings {
            val fileBelongings = FileBelongings()

            return fileBelongings
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(em: EntityManager): FileBelongings {
            val fileBelongings = FileBelongings()

            return fileBelongings
        }
    }
}
