package org.aydm.danak.web.rest


import org.aydm.danak.IntegrationTest
import org.aydm.danak.domain.Version
import org.aydm.danak.repository.VersionRepository
import org.aydm.danak.service.dto.VersionDTO
import org.aydm.danak.service.mapper.VersionMapper

import kotlin.test.assertNotNull

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.Validator
import javax.persistence.EntityManager
import java.util.Random
import java.util.concurrent.atomic.AtomicLong
import java.util.stream.Stream

import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers.hasItem
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*


/**
 * Integration tests for the [VersionResource] REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VersionResourceIT {
    @Autowired
    private lateinit var versionRepository: VersionRepository

    @Autowired
    private lateinit var versionMapper: VersionMapper

    @Autowired
    private lateinit var jacksonMessageConverter: MappingJackson2HttpMessageConverter

    @Autowired
    private lateinit var pageableArgumentResolver: PageableHandlerMethodArgumentResolver

    @Autowired
    private lateinit var validator: Validator


    @Autowired
    private lateinit var em: EntityManager


    @Autowired
    private lateinit var restVersionMockMvc: MockMvc

    private lateinit var version: Version


    @BeforeEach
    fun initTest() {
        version = createEntity(em)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createVersion() {
        val databaseSizeBeforeCreate = versionRepository.findAll().size
        // Create the Version
        val versionDTO = versionMapper.toDto(version)
        restVersionMockMvc.perform(
            post(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(versionDTO))
        ).andExpect(status().isCreated)

        // Validate the Version in the database
        val versionList = versionRepository.findAll()
        assertThat(versionList).hasSize(databaseSizeBeforeCreate + 1)
        val testVersion = versionList[versionList.size - 1]

        assertThat(testVersion.version).isEqualTo(DEFAULT_VERSION)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createVersionWithExistingId() {
        // Create the Version with an existing ID
        version.id = 1L
        val versionDTO = versionMapper.toDto(version)

        val databaseSizeBeforeCreate = versionRepository.findAll().size

        // An entity with an existing ID cannot be created, so this API call must fail
        restVersionMockMvc.perform(
            post(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(versionDTO))
        ).andExpect(status().isBadRequest)

        // Validate the Version in the database
        val versionList = versionRepository.findAll()
        assertThat(versionList).hasSize(databaseSizeBeforeCreate)
    }


    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllVersions() {
        // Initialize the database
        versionRepository.saveAndFlush(version)

        // Get all the versionList
        restVersionMockMvc.perform(get(ENTITY_API_URL+ "?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(version.id?.toInt())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getVersion() {
        // Initialize the database
        versionRepository.saveAndFlush(version)

        val id = version.id
        assertNotNull(id)

        // Get the version
        restVersionMockMvc.perform(get(ENTITY_API_URL_ID, version.id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(version.id?.toInt()))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION))    }
    @Test
    @Transactional
    @Throws(Exception::class)
    fun getNonExistingVersion() {
        // Get the version
        restVersionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }
    @Test
    @Transactional
    fun putNewVersion() {
        // Initialize the database
        versionRepository.saveAndFlush(version)

        val databaseSizeBeforeUpdate = versionRepository.findAll().size

        // Update the version
        val updatedVersion = versionRepository.findById(version.id).get()
        // Disconnect from session so that the updates on updatedVersion are not directly saved in db
        em.detach(updatedVersion)
        updatedVersion.version = UPDATED_VERSION
        val versionDTO = versionMapper.toDto(updatedVersion)

        restVersionMockMvc.perform(
            put(ENTITY_API_URL_ID, versionDTO.id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(versionDTO))
        ).andExpect(status().isOk)

        // Validate the Version in the database
        val versionList = versionRepository.findAll()
        assertThat(versionList).hasSize(databaseSizeBeforeUpdate)
        val testVersion = versionList[versionList.size - 1]
        assertThat(testVersion.version).isEqualTo(UPDATED_VERSION)
    }

    @Test
    @Transactional
    fun putNonExistingVersion() {
        val databaseSizeBeforeUpdate = versionRepository.findAll().size
        version.id = count.incrementAndGet()

        // Create the Version
        val versionDTO = versionMapper.toDto(version)

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVersionMockMvc.perform(put(ENTITY_API_URL_ID, versionDTO.id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(convertObjectToJsonBytes(versionDTO)))
            .andExpect(status().isBadRequest)

        // Validate the Version in the database
        val versionList = versionRepository.findAll()
        assertThat(versionList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun putWithIdMismatchVersion() {
        val databaseSizeBeforeUpdate = versionRepository.findAll().size
        version.id = count.incrementAndGet()

        // Create the Version
        val versionDTO = versionMapper.toDto(version)

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVersionMockMvc.perform(
            put(ENTITY_API_URL_ID, count.incrementAndGet())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(versionDTO))
        ).andExpect(status().isBadRequest)

        // Validate the Version in the database
        val versionList = versionRepository.findAll()
        assertThat(versionList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun putWithMissingIdPathParamVersion() {
        val databaseSizeBeforeUpdate = versionRepository.findAll().size
        version.id = count.incrementAndGet()

        // Create the Version
        val versionDTO = versionMapper.toDto(version)

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVersionMockMvc.perform(put(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(convertObjectToJsonBytes(versionDTO)))
            .andExpect(status().isMethodNotAllowed)

        // Validate the Version in the database
        val versionList = versionRepository.findAll()
        assertThat(versionList).hasSize(databaseSizeBeforeUpdate)
    }


    @Test
    @Transactional
    @Throws(Exception::class)
    fun partialUpdateVersionWithPatch() {
        versionRepository.saveAndFlush(version)


val databaseSizeBeforeUpdate = versionRepository.findAll().size

// Update the version using partial update
val partialUpdatedVersion = Version().apply {
//    id = version.id


        version = UPDATED_VERSION
}


restVersionMockMvc.perform(patch(ENTITY_API_URL_ID, partialUpdatedVersion.id)
.contentType("application/merge-patch+json")
.content(convertObjectToJsonBytes(partialUpdatedVersion)))
.andExpect(status().isOk)

// Validate the Version in the database
val versionList = versionRepository.findAll()
assertThat(versionList).hasSize(databaseSizeBeforeUpdate)
val testVersion = versionList.last()
    assertThat(testVersion.version).isEqualTo(UPDATED_VERSION)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun fullUpdateVersionWithPatch() {
        versionRepository.saveAndFlush(version)


val databaseSizeBeforeUpdate = versionRepository.findAll().size

// Update the version using partial update
val partialUpdatedVersion = Version().apply {
//    id = version.id


        version = UPDATED_VERSION
}


restVersionMockMvc.perform(patch(ENTITY_API_URL_ID, partialUpdatedVersion.id)
.contentType("application/merge-patch+json")
.content(convertObjectToJsonBytes(partialUpdatedVersion)))
.andExpect(status().isOk)

// Validate the Version in the database
val versionList = versionRepository.findAll()
assertThat(versionList).hasSize(databaseSizeBeforeUpdate)
val testVersion = versionList.last()
    assertThat(testVersion.version).isEqualTo(UPDATED_VERSION)
    }

    @Throws(Exception::class)
    fun patchNonExistingVersion() {
        val databaseSizeBeforeUpdate = versionRepository.findAll().size
        version.id = count.incrementAndGet()

        // Create the Version
        val versionDTO = versionMapper.toDto(version)

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVersionMockMvc.perform(patch(ENTITY_API_URL_ID, versionDTO.id)
            .contentType("application/merge-patch+json")
            .content(convertObjectToJsonBytes(versionDTO)))
            .andExpect(status().isBadRequest)

        // Validate the Version in the database
        val versionList = versionRepository.findAll()
        assertThat(versionList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun patchWithIdMismatchVersion() {
        val databaseSizeBeforeUpdate = versionRepository.findAll().size
        version.id = count.incrementAndGet()

        // Create the Version
        val versionDTO = versionMapper.toDto(version)

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVersionMockMvc.perform(patch(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType("application/merge-patch+json")
            .content(convertObjectToJsonBytes(versionDTO)))
            .andExpect(status().isBadRequest)

        // Validate the Version in the database
        val versionList = versionRepository.findAll()
        assertThat(versionList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun patchWithMissingIdPathParamVersion() {
        val databaseSizeBeforeUpdate = versionRepository.findAll().size
        version.id = count.incrementAndGet()

        // Create the Version
        val versionDTO = versionMapper.toDto(version)

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVersionMockMvc.perform(patch(ENTITY_API_URL)
            .contentType("application/merge-patch+json")
            .content(convertObjectToJsonBytes(versionDTO)))
            .andExpect(status().isMethodNotAllowed)

        // Validate the Version in the database
        val versionList = versionRepository.findAll()
        assertThat(versionList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun deleteVersion() {
        // Initialize the database
        versionRepository.saveAndFlush(version)

        val databaseSizeBeforeDelete = versionRepository.findAll().size

        // Delete the version
        restVersionMockMvc.perform(
            delete(ENTITY_API_URL_ID, version.id)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val versionList = versionRepository.findAll()
        assertThat(versionList).hasSize(databaseSizeBeforeDelete - 1)
    }


    companion object {

        private const val DEFAULT_VERSION: Int = 1
        private const val UPDATED_VERSION: Int = 2


        private val ENTITY_API_URL: String = "/api/versions"
        private val ENTITY_API_URL_ID: String = ENTITY_API_URL + "/{id}"

        private val random: Random = Random()
        private val count: AtomicLong = AtomicLong(random.nextInt().toLong() + ( 2 * Integer.MAX_VALUE ))




        /**
         * Create an entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createEntity(em: EntityManager): Version {
            val version = Version(
                version = DEFAULT_VERSION

            )


            return version
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(em: EntityManager): Version {
            val version = Version(
                version = UPDATED_VERSION

            )


            return version
        }

    }
}
