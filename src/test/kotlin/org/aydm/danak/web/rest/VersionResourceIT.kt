package org.aydm.danak.web.rest

import org.assertj.core.api.Assertions.assertThat
import org.aydm.danak.IntegrationTest
import org.aydm.danak.domain.File
import org.aydm.danak.domain.FileBelongings
import org.aydm.danak.domain.Version
import org.aydm.danak.repository.VersionRepository
import org.aydm.danak.service.mapper.VersionMapper
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
        assertThat(testVersion.tag).isEqualTo(DEFAULT_TAG)
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
        restVersionMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(version.id?.toInt())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].tag").value(hasItem(DEFAULT_TAG)))
    }

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
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION))
            .andExpect(jsonPath("$.tag").value(DEFAULT_TAG))
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getVersionsByIdFiltering() {
        // Initialize the database
        versionRepository.saveAndFlush(version)
        val id = version.id

        defaultVersionShouldBeFound("id.equals=$id")
        defaultVersionShouldNotBeFound("id.notEquals=$id")
        defaultVersionShouldBeFound("id.greaterThanOrEqual=$id")
        defaultVersionShouldNotBeFound("id.greaterThan=$id")

        defaultVersionShouldBeFound("id.lessThanOrEqual=$id")
        defaultVersionShouldNotBeFound("id.lessThan=$id")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllVersionsByVersionIsEqualToSomething() {
        // Initialize the database
        versionRepository.saveAndFlush(version)

        // Get all the versionList where version equals to DEFAULT_VERSION
        defaultVersionShouldBeFound("version.equals=$DEFAULT_VERSION")

        // Get all the versionList where version equals to UPDATED_VERSION
        defaultVersionShouldNotBeFound("version.equals=$UPDATED_VERSION")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllVersionsByVersionIsNotEqualToSomething() {
        // Initialize the database
        versionRepository.saveAndFlush(version)

        // Get all the versionList where version not equals to DEFAULT_VERSION
        defaultVersionShouldNotBeFound("version.notEquals=$DEFAULT_VERSION")

        // Get all the versionList where version not equals to UPDATED_VERSION
        defaultVersionShouldBeFound("version.notEquals=$UPDATED_VERSION")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllVersionsByVersionIsInShouldWork() {
        // Initialize the database
        versionRepository.saveAndFlush(version)

        // Get all the versionList where version in DEFAULT_VERSION or UPDATED_VERSION
        defaultVersionShouldBeFound("version.in=$DEFAULT_VERSION,$UPDATED_VERSION")

        // Get all the versionList where version equals to UPDATED_VERSION
        defaultVersionShouldNotBeFound("version.in=$UPDATED_VERSION")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllVersionsByVersionIsNullOrNotNull() {
        // Initialize the database
        versionRepository.saveAndFlush(version)

        // Get all the versionList where version is not null
        defaultVersionShouldBeFound("version.specified=true")

        // Get all the versionList where version is null
        defaultVersionShouldNotBeFound("version.specified=false")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllVersionsByVersionIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        versionRepository.saveAndFlush(version)

        // Get all the versionList where version is greater than or equal to DEFAULT_VERSION
        defaultVersionShouldBeFound("version.greaterThanOrEqual=$DEFAULT_VERSION")

        // Get all the versionList where version is greater than or equal to UPDATED_VERSION
        defaultVersionShouldNotBeFound("version.greaterThanOrEqual=$UPDATED_VERSION")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllVersionsByVersionIsLessThanOrEqualToSomething() {
        // Initialize the database
        versionRepository.saveAndFlush(version)

        // Get all the versionList where version is less than or equal to DEFAULT_VERSION
        defaultVersionShouldBeFound("version.lessThanOrEqual=$DEFAULT_VERSION")

        // Get all the versionList where version is less than or equal to SMALLER_VERSION
        defaultVersionShouldNotBeFound("version.lessThanOrEqual=$SMALLER_VERSION")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllVersionsByVersionIsLessThanSomething() {
        // Initialize the database
        versionRepository.saveAndFlush(version)

        // Get all the versionList where version is less than DEFAULT_VERSION
        defaultVersionShouldNotBeFound("version.lessThan=$DEFAULT_VERSION")

        // Get all the versionList where version is less than UPDATED_VERSION
        defaultVersionShouldBeFound("version.lessThan=$UPDATED_VERSION")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllVersionsByVersionIsGreaterThanSomething() {
        // Initialize the database
        versionRepository.saveAndFlush(version)

        // Get all the versionList where version is greater than DEFAULT_VERSION
        defaultVersionShouldNotBeFound("version.greaterThan=$DEFAULT_VERSION")

        // Get all the versionList where version is greater than SMALLER_VERSION
        defaultVersionShouldBeFound("version.greaterThan=$SMALLER_VERSION")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllVersionsByTagIsEqualToSomething() {
        // Initialize the database
        versionRepository.saveAndFlush(version)

        // Get all the versionList where tag equals to DEFAULT_TAG
        defaultVersionShouldBeFound("tag.equals=$DEFAULT_TAG")

        // Get all the versionList where tag equals to UPDATED_TAG
        defaultVersionShouldNotBeFound("tag.equals=$UPDATED_TAG")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllVersionsByTagIsNotEqualToSomething() {
        // Initialize the database
        versionRepository.saveAndFlush(version)

        // Get all the versionList where tag not equals to DEFAULT_TAG
        defaultVersionShouldNotBeFound("tag.notEquals=$DEFAULT_TAG")

        // Get all the versionList where tag not equals to UPDATED_TAG
        defaultVersionShouldBeFound("tag.notEquals=$UPDATED_TAG")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllVersionsByTagIsInShouldWork() {
        // Initialize the database
        versionRepository.saveAndFlush(version)

        // Get all the versionList where tag in DEFAULT_TAG or UPDATED_TAG
        defaultVersionShouldBeFound("tag.in=$DEFAULT_TAG,$UPDATED_TAG")

        // Get all the versionList where tag equals to UPDATED_TAG
        defaultVersionShouldNotBeFound("tag.in=$UPDATED_TAG")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllVersionsByTagIsNullOrNotNull() {
        // Initialize the database
        versionRepository.saveAndFlush(version)

        // Get all the versionList where tag is not null
        defaultVersionShouldBeFound("tag.specified=true")

        // Get all the versionList where tag is null
        defaultVersionShouldNotBeFound("tag.specified=false")
    }
    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllVersionsByTagContainsSomething() {
        // Initialize the database
        versionRepository.saveAndFlush(version)

        // Get all the versionList where tag contains DEFAULT_TAG
        defaultVersionShouldBeFound("tag.contains=$DEFAULT_TAG")

        // Get all the versionList where tag contains UPDATED_TAG
        defaultVersionShouldNotBeFound("tag.contains=$UPDATED_TAG")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllVersionsByTagNotContainsSomething() {
        // Initialize the database
        versionRepository.saveAndFlush(version)

        // Get all the versionList where tag does not contain DEFAULT_TAG
        defaultVersionShouldNotBeFound("tag.doesNotContain=$DEFAULT_TAG")

        // Get all the versionList where tag does not contain UPDATED_TAG
        defaultVersionShouldBeFound("tag.doesNotContain=$UPDATED_TAG")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllVersionsByFileBelongingsIsEqualToSomething() {
        // Initialize the database
        versionRepository.saveAndFlush(version)
        var fileBelongings: FileBelongings
        if (findAll(em, FileBelongings::class).isEmpty()) {
            fileBelongings = FileBelongingsResourceIT.createEntity(em)
            em.persist(fileBelongings)
            em.flush()
        } else {
            fileBelongings = findAll(em, FileBelongings::class)[0]
        }
        em.persist(fileBelongings)
        em.flush()
        version.addFileBelongings(fileBelongings)
        versionRepository.saveAndFlush(version)
        val fileBelongingsId = fileBelongings?.id

        // Get all the versionList where fileBelongings equals to fileBelongingsId
        defaultVersionShouldBeFound("fileBelongingsId.equals=$fileBelongingsId")

        // Get all the versionList where fileBelongings equals to (fileBelongingsId?.plus(1))
        defaultVersionShouldNotBeFound("fileBelongingsId.equals=${(fileBelongingsId?.plus(1))}")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllVersionsByFileIsEqualToSomething() {
        // Initialize the database
        versionRepository.saveAndFlush(version)
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
        version.addFile(file)
        versionRepository.saveAndFlush(version)
        val fileId = file?.id

        // Get all the versionList where file equals to fileId
        defaultVersionShouldBeFound("fileId.equals=$fileId")

        // Get all the versionList where file equals to (fileId?.plus(1))
        defaultVersionShouldNotBeFound("fileId.equals=${(fileId?.plus(1))}")
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */

    @Throws(Exception::class)
    private fun defaultVersionShouldBeFound(filter: String) {
        restVersionMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(version.id?.toInt())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].tag").value(hasItem(DEFAULT_TAG)))

        // Check, that the count call also returns 1
        restVersionMockMvc.perform(get(ENTITY_API_URL + "/count?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"))
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    @Throws(Exception::class)
    private fun defaultVersionShouldNotBeFound(filter: String) {
        restVersionMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$").isEmpty)

        // Check, that the count call also returns 0
        restVersionMockMvc.perform(get(ENTITY_API_URL + "/count?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"))
    }
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
        updatedVersion.tag = UPDATED_TAG
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
        assertThat(testVersion.tag).isEqualTo(UPDATED_TAG)
    }

    @Test
    @Transactional
    fun putNonExistingVersion() {
        val databaseSizeBeforeUpdate = versionRepository.findAll().size
        version.id = count.incrementAndGet()

        // Create the Version
        val versionDTO = versionMapper.toDto(version)

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVersionMockMvc.perform(
            put(ENTITY_API_URL_ID, versionDTO.id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(versionDTO))
        )
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
        restVersionMockMvc.perform(
            put(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(versionDTO))
        )
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

            version = UPDATED_VERSION
        }

        restVersionMockMvc.perform(
            patch(ENTITY_API_URL_ID, partialUpdatedVersion.id)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(partialUpdatedVersion))
        )
            .andExpect(status().isOk)

// Validate the Version in the database
        val versionList = versionRepository.findAll()
        assertThat(versionList).hasSize(databaseSizeBeforeUpdate)
        val testVersion = versionList.last()
        assertThat(testVersion.version).isEqualTo(UPDATED_VERSION)
        assertThat(testVersion.tag).isEqualTo(DEFAULT_TAG)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun fullUpdateVersionWithPatch() {
        versionRepository.saveAndFlush(version)

        val databaseSizeBeforeUpdate = versionRepository.findAll().size

// Update the version using partial update
        val partialUpdatedVersion = Version().apply {

            version = UPDATED_VERSION
            tag = UPDATED_TAG
        }

        restVersionMockMvc.perform(
            patch(ENTITY_API_URL_ID, partialUpdatedVersion.id)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(partialUpdatedVersion))
        )
            .andExpect(status().isOk)

// Validate the Version in the database
        val versionList = versionRepository.findAll()
        assertThat(versionList).hasSize(databaseSizeBeforeUpdate)
        val testVersion = versionList.last()
        assertThat(testVersion.version).isEqualTo(UPDATED_VERSION)
        assertThat(testVersion.tag).isEqualTo(UPDATED_TAG)
    }

    @Throws(Exception::class)
    fun patchNonExistingVersion() {
        val databaseSizeBeforeUpdate = versionRepository.findAll().size
        version.id = count.incrementAndGet()

        // Create the Version
        val versionDTO = versionMapper.toDto(version)

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVersionMockMvc.perform(
            patch(ENTITY_API_URL_ID, versionDTO.id)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(versionDTO))
        )
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
        restVersionMockMvc.perform(
            patch(ENTITY_API_URL_ID, count.incrementAndGet())
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(versionDTO))
        )
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
        restVersionMockMvc.perform(
            patch(ENTITY_API_URL)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(versionDTO))
        )
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
        private const val SMALLER_VERSION: Int = 1 - 1

        private const val DEFAULT_TAG = "AAAAAAAAAA"
        private const val UPDATED_TAG = "BBBBBBBBBB"

        private val ENTITY_API_URL: String = "/api/versions"
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
        fun createEntity(em: EntityManager): Version {
            val version = Version(
                version = DEFAULT_VERSION,

                tag = DEFAULT_TAG

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
                version = UPDATED_VERSION,

                tag = UPDATED_TAG

            )

            return version
        }
    }
}
