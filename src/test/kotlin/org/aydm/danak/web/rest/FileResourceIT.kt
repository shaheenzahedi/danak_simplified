package org.aydm.danak.web.rest

import org.assertj.core.api.Assertions.assertThat
import org.aydm.danak.IntegrationTest
import org.aydm.danak.domain.File
import org.aydm.danak.domain.FileBelongings
import org.aydm.danak.domain.Version
import org.aydm.danak.repository.FileRepository
import org.aydm.danak.service.mapper.FileMapper
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
 * Integration tests for the [FileResource] REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FileResourceIT {
    @Autowired
    private lateinit var fileRepository: FileRepository

    @Autowired
    private lateinit var fileMapper: FileMapper

    @Autowired
    private lateinit var jacksonMessageConverter: MappingJackson2HttpMessageConverter

    @Autowired
    private lateinit var pageableArgumentResolver: PageableHandlerMethodArgumentResolver

    @Autowired
    private lateinit var validator: Validator

    @Autowired
    private lateinit var em: EntityManager

    @Autowired
    private lateinit var restFileMockMvc: MockMvc

    private lateinit var file: File

    @BeforeEach
    fun initTest() {
        file = createEntity(em)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createFile() {
        val databaseSizeBeforeCreate = fileRepository.findAll().size
        // Create the File
        val fileDTO = fileMapper.toDto(file)
        restFileMockMvc.perform(
            post(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(fileDTO))
        ).andExpect(status().isCreated)

        // Validate the File in the database
        val fileList = fileRepository.findAll()
        assertThat(fileList).hasSize(databaseSizeBeforeCreate + 1)
        val testFile = fileList[fileList.size - 1]

        assertThat(testFile.name).isEqualTo(DEFAULT_NAME)
        assertThat(testFile.checksum).isEqualTo(DEFAULT_CHECKSUM)
        assertThat(testFile.path).isEqualTo(DEFAULT_PATH)
        assertThat(testFile.size).isEqualTo(DEFAULT_SIZE)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createFileWithExistingId() {
        // Create the File with an existing ID
        file.id = 1L
        val fileDTO = fileMapper.toDto(file)

        val databaseSizeBeforeCreate = fileRepository.findAll().size

        // An entity with an existing ID cannot be created, so this API call must fail
        restFileMockMvc.perform(
            post(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(fileDTO))
        ).andExpect(status().isBadRequest)

        // Validate the File in the database
        val fileList = fileRepository.findAll()
        assertThat(fileList).hasSize(databaseSizeBeforeCreate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFiles() {
        // Initialize the database
        fileRepository.saveAndFlush(file)

        // Get all the fileList
        restFileMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(file.id?.toInt())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].checksum").value(hasItem(DEFAULT_CHECKSUM)))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH)))
            .andExpect(jsonPath("$.[*].size").value(hasItem(DEFAULT_SIZE)))
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getFile() {
        // Initialize the database
        fileRepository.saveAndFlush(file)

        val id = file.id
        assertNotNull(id)

        // Get the file
        restFileMockMvc.perform(get(ENTITY_API_URL_ID, file.id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(file.id?.toInt()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.checksum").value(DEFAULT_CHECKSUM))
            .andExpect(jsonPath("$.path").value(DEFAULT_PATH))
            .andExpect(jsonPath("$.size").value(DEFAULT_SIZE))
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getFilesByIdFiltering() {
        // Initialize the database
        fileRepository.saveAndFlush(file)
        val id = file.id

        defaultFileShouldBeFound("id.equals=$id")
        defaultFileShouldNotBeFound("id.notEquals=$id")
        defaultFileShouldBeFound("id.greaterThanOrEqual=$id")
        defaultFileShouldNotBeFound("id.greaterThan=$id")

        defaultFileShouldBeFound("id.lessThanOrEqual=$id")
        defaultFileShouldNotBeFound("id.lessThan=$id")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFilesByNameIsEqualToSomething() {
        // Initialize the database
        fileRepository.saveAndFlush(file)

        // Get all the fileList where name equals to DEFAULT_NAME
        defaultFileShouldBeFound("name.equals=$DEFAULT_NAME")

        // Get all the fileList where name equals to UPDATED_NAME
        defaultFileShouldNotBeFound("name.equals=$UPDATED_NAME")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFilesByNameIsNotEqualToSomething() {
        // Initialize the database
        fileRepository.saveAndFlush(file)

        // Get all the fileList where name not equals to DEFAULT_NAME
        defaultFileShouldNotBeFound("name.notEquals=$DEFAULT_NAME")

        // Get all the fileList where name not equals to UPDATED_NAME
        defaultFileShouldBeFound("name.notEquals=$UPDATED_NAME")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFilesByNameIsInShouldWork() {
        // Initialize the database
        fileRepository.saveAndFlush(file)

        // Get all the fileList where name in DEFAULT_NAME or UPDATED_NAME
        defaultFileShouldBeFound("name.in=$DEFAULT_NAME,$UPDATED_NAME")

        // Get all the fileList where name equals to UPDATED_NAME
        defaultFileShouldNotBeFound("name.in=$UPDATED_NAME")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFilesByNameIsNullOrNotNull() {
        // Initialize the database
        fileRepository.saveAndFlush(file)

        // Get all the fileList where name is not null
        defaultFileShouldBeFound("name.specified=true")

        // Get all the fileList where name is null
        defaultFileShouldNotBeFound("name.specified=false")
    }
    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFilesByNameContainsSomething() {
        // Initialize the database
        fileRepository.saveAndFlush(file)

        // Get all the fileList where name contains DEFAULT_NAME
        defaultFileShouldBeFound("name.contains=$DEFAULT_NAME")

        // Get all the fileList where name contains UPDATED_NAME
        defaultFileShouldNotBeFound("name.contains=$UPDATED_NAME")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFilesByNameNotContainsSomething() {
        // Initialize the database
        fileRepository.saveAndFlush(file)

        // Get all the fileList where name does not contain DEFAULT_NAME
        defaultFileShouldNotBeFound("name.doesNotContain=$DEFAULT_NAME")

        // Get all the fileList where name does not contain UPDATED_NAME
        defaultFileShouldBeFound("name.doesNotContain=$UPDATED_NAME")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFilesByChecksumIsEqualToSomething() {
        // Initialize the database
        fileRepository.saveAndFlush(file)

        // Get all the fileList where checksum equals to DEFAULT_CHECKSUM
        defaultFileShouldBeFound("checksum.equals=$DEFAULT_CHECKSUM")

        // Get all the fileList where checksum equals to UPDATED_CHECKSUM
        defaultFileShouldNotBeFound("checksum.equals=$UPDATED_CHECKSUM")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFilesByChecksumIsNotEqualToSomething() {
        // Initialize the database
        fileRepository.saveAndFlush(file)

        // Get all the fileList where checksum not equals to DEFAULT_CHECKSUM
        defaultFileShouldNotBeFound("checksum.notEquals=$DEFAULT_CHECKSUM")

        // Get all the fileList where checksum not equals to UPDATED_CHECKSUM
        defaultFileShouldBeFound("checksum.notEquals=$UPDATED_CHECKSUM")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFilesByChecksumIsInShouldWork() {
        // Initialize the database
        fileRepository.saveAndFlush(file)

        // Get all the fileList where checksum in DEFAULT_CHECKSUM or UPDATED_CHECKSUM
        defaultFileShouldBeFound("checksum.in=$DEFAULT_CHECKSUM,$UPDATED_CHECKSUM")

        // Get all the fileList where checksum equals to UPDATED_CHECKSUM
        defaultFileShouldNotBeFound("checksum.in=$UPDATED_CHECKSUM")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFilesByChecksumIsNullOrNotNull() {
        // Initialize the database
        fileRepository.saveAndFlush(file)

        // Get all the fileList where checksum is not null
        defaultFileShouldBeFound("checksum.specified=true")

        // Get all the fileList where checksum is null
        defaultFileShouldNotBeFound("checksum.specified=false")
    }
    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFilesByChecksumContainsSomething() {
        // Initialize the database
        fileRepository.saveAndFlush(file)

        // Get all the fileList where checksum contains DEFAULT_CHECKSUM
        defaultFileShouldBeFound("checksum.contains=$DEFAULT_CHECKSUM")

        // Get all the fileList where checksum contains UPDATED_CHECKSUM
        defaultFileShouldNotBeFound("checksum.contains=$UPDATED_CHECKSUM")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFilesByChecksumNotContainsSomething() {
        // Initialize the database
        fileRepository.saveAndFlush(file)

        // Get all the fileList where checksum does not contain DEFAULT_CHECKSUM
        defaultFileShouldNotBeFound("checksum.doesNotContain=$DEFAULT_CHECKSUM")

        // Get all the fileList where checksum does not contain UPDATED_CHECKSUM
        defaultFileShouldBeFound("checksum.doesNotContain=$UPDATED_CHECKSUM")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFilesByPathIsEqualToSomething() {
        // Initialize the database
        fileRepository.saveAndFlush(file)

        // Get all the fileList where path equals to DEFAULT_PATH
        defaultFileShouldBeFound("path.equals=$DEFAULT_PATH")

        // Get all the fileList where path equals to UPDATED_PATH
        defaultFileShouldNotBeFound("path.equals=$UPDATED_PATH")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFilesByPathIsNotEqualToSomething() {
        // Initialize the database
        fileRepository.saveAndFlush(file)

        // Get all the fileList where path not equals to DEFAULT_PATH
        defaultFileShouldNotBeFound("path.notEquals=$DEFAULT_PATH")

        // Get all the fileList where path not equals to UPDATED_PATH
        defaultFileShouldBeFound("path.notEquals=$UPDATED_PATH")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFilesByPathIsInShouldWork() {
        // Initialize the database
        fileRepository.saveAndFlush(file)

        // Get all the fileList where path in DEFAULT_PATH or UPDATED_PATH
        defaultFileShouldBeFound("path.in=$DEFAULT_PATH,$UPDATED_PATH")

        // Get all the fileList where path equals to UPDATED_PATH
        defaultFileShouldNotBeFound("path.in=$UPDATED_PATH")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFilesByPathIsNullOrNotNull() {
        // Initialize the database
        fileRepository.saveAndFlush(file)

        // Get all the fileList where path is not null
        defaultFileShouldBeFound("path.specified=true")

        // Get all the fileList where path is null
        defaultFileShouldNotBeFound("path.specified=false")
    }
    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFilesByPathContainsSomething() {
        // Initialize the database
        fileRepository.saveAndFlush(file)

        // Get all the fileList where path contains DEFAULT_PATH
        defaultFileShouldBeFound("path.contains=$DEFAULT_PATH")

        // Get all the fileList where path contains UPDATED_PATH
        defaultFileShouldNotBeFound("path.contains=$UPDATED_PATH")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFilesByPathNotContainsSomething() {
        // Initialize the database
        fileRepository.saveAndFlush(file)

        // Get all the fileList where path does not contain DEFAULT_PATH
        defaultFileShouldNotBeFound("path.doesNotContain=$DEFAULT_PATH")

        // Get all the fileList where path does not contain UPDATED_PATH
        defaultFileShouldBeFound("path.doesNotContain=$UPDATED_PATH")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFilesBySizeIsEqualToSomething() {
        // Initialize the database
        fileRepository.saveAndFlush(file)

        // Get all the fileList where size equals to DEFAULT_SIZE
        defaultFileShouldBeFound("size.equals=$DEFAULT_SIZE")

        // Get all the fileList where size equals to UPDATED_SIZE
        defaultFileShouldNotBeFound("size.equals=$UPDATED_SIZE")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFilesBySizeIsNotEqualToSomething() {
        // Initialize the database
        fileRepository.saveAndFlush(file)

        // Get all the fileList where size not equals to DEFAULT_SIZE
        defaultFileShouldNotBeFound("size.notEquals=$DEFAULT_SIZE")

        // Get all the fileList where size not equals to UPDATED_SIZE
        defaultFileShouldBeFound("size.notEquals=$UPDATED_SIZE")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFilesBySizeIsInShouldWork() {
        // Initialize the database
        fileRepository.saveAndFlush(file)

        // Get all the fileList where size in DEFAULT_SIZE or UPDATED_SIZE
        defaultFileShouldBeFound("size.in=$DEFAULT_SIZE,$UPDATED_SIZE")

        // Get all the fileList where size equals to UPDATED_SIZE
        defaultFileShouldNotBeFound("size.in=$UPDATED_SIZE")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFilesBySizeIsNullOrNotNull() {
        // Initialize the database
        fileRepository.saveAndFlush(file)

        // Get all the fileList where size is not null
        defaultFileShouldBeFound("size.specified=true")

        // Get all the fileList where size is null
        defaultFileShouldNotBeFound("size.specified=false")
    }
    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFilesBySizeContainsSomething() {
        // Initialize the database
        fileRepository.saveAndFlush(file)

        // Get all the fileList where size contains DEFAULT_SIZE
        defaultFileShouldBeFound("size.contains=$DEFAULT_SIZE")

        // Get all the fileList where size contains UPDATED_SIZE
        defaultFileShouldNotBeFound("size.contains=$UPDATED_SIZE")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFilesBySizeNotContainsSomething() {
        // Initialize the database
        fileRepository.saveAndFlush(file)

        // Get all the fileList where size does not contain DEFAULT_SIZE
        defaultFileShouldNotBeFound("size.doesNotContain=$DEFAULT_SIZE")

        // Get all the fileList where size does not contain UPDATED_SIZE
        defaultFileShouldBeFound("size.doesNotContain=$UPDATED_SIZE")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFilesByFileBelongingsIsEqualToSomething() {
        // Initialize the database
        fileRepository.saveAndFlush(file)
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
        file.addFileBelongings(fileBelongings)
        fileRepository.saveAndFlush(file)
        val fileBelongingsId = fileBelongings?.id

        // Get all the fileList where fileBelongings equals to fileBelongingsId
        defaultFileShouldBeFound("fileBelongingsId.equals=$fileBelongingsId")

        // Get all the fileList where fileBelongings equals to (fileBelongingsId?.plus(1))
        defaultFileShouldNotBeFound("fileBelongingsId.equals=${(fileBelongingsId?.plus(1))}")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllFilesByPlacementIsEqualToSomething() {
        // Initialize the database
        fileRepository.saveAndFlush(file)
        var placement: Version
        if (findAll(em, Version::class).isEmpty()) {
            placement = VersionResourceIT.createEntity(em)
            em.persist(placement)
            em.flush()
        } else {
            placement = findAll(em, Version::class)[0]
        }
        em.persist(placement)
        em.flush()
        file.placement = placement
        fileRepository.saveAndFlush(file)
        val placementId = placement?.id

        // Get all the fileList where placement equals to placementId
        defaultFileShouldBeFound("placementId.equals=$placementId")

        // Get all the fileList where placement equals to (placementId?.plus(1))
        defaultFileShouldNotBeFound("placementId.equals=${(placementId?.plus(1))}")
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */

    @Throws(Exception::class)
    private fun defaultFileShouldBeFound(filter: String) {
        restFileMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(file.id?.toInt())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].checksum").value(hasItem(DEFAULT_CHECKSUM)))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH)))
            .andExpect(jsonPath("$.[*].size").value(hasItem(DEFAULT_SIZE)))

        // Check, that the count call also returns 1
        restFileMockMvc.perform(get(ENTITY_API_URL + "/count?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"))
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    @Throws(Exception::class)
    private fun defaultFileShouldNotBeFound(filter: String) {
        restFileMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$").isEmpty)

        // Check, that the count call also returns 0
        restFileMockMvc.perform(get(ENTITY_API_URL + "/count?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"))
    }
    @Test
    @Transactional
    @Throws(Exception::class)
    fun getNonExistingFile() {
        // Get the file
        restFileMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }
    @Test
    @Transactional
    fun putNewFile() {
        // Initialize the database
        fileRepository.saveAndFlush(file)

        val databaseSizeBeforeUpdate = fileRepository.findAll().size

        // Update the file
        val updatedFile = fileRepository.findById(file.id).orElseThrow()
        // Disconnect from session so that the updates on updatedFile are not directly saved in db
        em.detach(updatedFile)
        updatedFile.name = UPDATED_NAME
        updatedFile.checksum = UPDATED_CHECKSUM
        updatedFile.path = UPDATED_PATH
        updatedFile.size = UPDATED_SIZE
        val fileDTO = fileMapper.toDto(updatedFile)

        restFileMockMvc.perform(
            put(ENTITY_API_URL_ID, fileDTO.id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(fileDTO))
        ).andExpect(status().isOk)

        // Validate the File in the database
        val fileList = fileRepository.findAll()
        assertThat(fileList).hasSize(databaseSizeBeforeUpdate)
        val testFile = fileList[fileList.size - 1]
        assertThat(testFile.name).isEqualTo(UPDATED_NAME)
        assertThat(testFile.checksum).isEqualTo(UPDATED_CHECKSUM)
        assertThat(testFile.path).isEqualTo(UPDATED_PATH)
        assertThat(testFile.size).isEqualTo(UPDATED_SIZE)
    }

    @Test
    @Transactional
    fun putNonExistingFile() {
        val databaseSizeBeforeUpdate = fileRepository.findAll().size
        file.id = count.incrementAndGet()

        // Create the File
        val fileDTO = fileMapper.toDto(file)

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFileMockMvc.perform(
            put(ENTITY_API_URL_ID, fileDTO.id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(fileDTO))
        )
            .andExpect(status().isBadRequest)

        // Validate the File in the database
        val fileList = fileRepository.findAll()
        assertThat(fileList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun putWithIdMismatchFile() {
        val databaseSizeBeforeUpdate = fileRepository.findAll().size
        file.id = count.incrementAndGet()

        // Create the File
        val fileDTO = fileMapper.toDto(file)

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileMockMvc.perform(
            put(ENTITY_API_URL_ID, count.incrementAndGet())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(fileDTO))
        ).andExpect(status().isBadRequest)

        // Validate the File in the database
        val fileList = fileRepository.findAll()
        assertThat(fileList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun putWithMissingIdPathParamFile() {
        val databaseSizeBeforeUpdate = fileRepository.findAll().size
        file.id = count.incrementAndGet()

        // Create the File
        val fileDTO = fileMapper.toDto(file)

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileMockMvc.perform(
            put(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(fileDTO))
        )
            .andExpect(status().isMethodNotAllowed)

        // Validate the File in the database
        val fileList = fileRepository.findAll()
        assertThat(fileList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun partialUpdateFileWithPatch() {
        fileRepository.saveAndFlush(file)

        val databaseSizeBeforeUpdate = fileRepository.findAll().size

// Update the file using partial update
        val partialUpdatedFile = File().apply {
            id = file.id

            path = UPDATED_PATH
            size = UPDATED_SIZE
        }

        restFileMockMvc.perform(
            patch(ENTITY_API_URL_ID, partialUpdatedFile.id)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(partialUpdatedFile))
        )
            .andExpect(status().isOk)

// Validate the File in the database
        val fileList = fileRepository.findAll()
        assertThat(fileList).hasSize(databaseSizeBeforeUpdate)
        val testFile = fileList.last()
        assertThat(testFile.name).isEqualTo(DEFAULT_NAME)
        assertThat(testFile.checksum).isEqualTo(DEFAULT_CHECKSUM)
        assertThat(testFile.path).isEqualTo(UPDATED_PATH)
        assertThat(testFile.size).isEqualTo(UPDATED_SIZE)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun fullUpdateFileWithPatch() {
        fileRepository.saveAndFlush(file)

        val databaseSizeBeforeUpdate = fileRepository.findAll().size

// Update the file using partial update
        val partialUpdatedFile = File().apply {
            id = file.id

            name = UPDATED_NAME
            checksum = UPDATED_CHECKSUM
            path = UPDATED_PATH
            size = UPDATED_SIZE
        }

        restFileMockMvc.perform(
            patch(ENTITY_API_URL_ID, partialUpdatedFile.id)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(partialUpdatedFile))
        )
            .andExpect(status().isOk)

// Validate the File in the database
        val fileList = fileRepository.findAll()
        assertThat(fileList).hasSize(databaseSizeBeforeUpdate)
        val testFile = fileList.last()
        assertThat(testFile.name).isEqualTo(UPDATED_NAME)
        assertThat(testFile.checksum).isEqualTo(UPDATED_CHECKSUM)
        assertThat(testFile.path).isEqualTo(UPDATED_PATH)
        assertThat(testFile.size).isEqualTo(UPDATED_SIZE)
    }

    @Throws(Exception::class)
    fun patchNonExistingFile() {
        val databaseSizeBeforeUpdate = fileRepository.findAll().size
        file.id = count.incrementAndGet()

        // Create the File
        val fileDTO = fileMapper.toDto(file)

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFileMockMvc.perform(
            patch(ENTITY_API_URL_ID, fileDTO.id)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(fileDTO))
        )
            .andExpect(status().isBadRequest)

        // Validate the File in the database
        val fileList = fileRepository.findAll()
        assertThat(fileList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun patchWithIdMismatchFile() {
        val databaseSizeBeforeUpdate = fileRepository.findAll().size
        file.id = count.incrementAndGet()

        // Create the File
        val fileDTO = fileMapper.toDto(file)

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileMockMvc.perform(
            patch(ENTITY_API_URL_ID, count.incrementAndGet())
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(fileDTO))
        )
            .andExpect(status().isBadRequest)

        // Validate the File in the database
        val fileList = fileRepository.findAll()
        assertThat(fileList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun patchWithMissingIdPathParamFile() {
        val databaseSizeBeforeUpdate = fileRepository.findAll().size
        file.id = count.incrementAndGet()

        // Create the File
        val fileDTO = fileMapper.toDto(file)

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileMockMvc.perform(
            patch(ENTITY_API_URL)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(fileDTO))
        )
            .andExpect(status().isMethodNotAllowed)

        // Validate the File in the database
        val fileList = fileRepository.findAll()
        assertThat(fileList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun deleteFile() {
        // Initialize the database
        fileRepository.saveAndFlush(file)

        val databaseSizeBeforeDelete = fileRepository.findAll().size

        // Delete the file
        restFileMockMvc.perform(
            delete(ENTITY_API_URL_ID, file.id)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val fileList = fileRepository.findAll()
        assertThat(fileList).hasSize(databaseSizeBeforeDelete - 1)
    }

    companion object {

        private const val DEFAULT_NAME = "AAAAAAAAAA"
        private const val UPDATED_NAME = "BBBBBBBBBB"

        private const val DEFAULT_CHECKSUM = "AAAAAAAAAA"
        private const val UPDATED_CHECKSUM = "BBBBBBBBBB"

        private const val DEFAULT_PATH = "AAAAAAAAAA"
        private const val UPDATED_PATH = "BBBBBBBBBB"

        private const val DEFAULT_SIZE = "AAAAAAAAAA"
        private const val UPDATED_SIZE = "BBBBBBBBBB"

        private val ENTITY_API_URL: String = "/api/files"
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
        fun createEntity(em: EntityManager): File {
            val file = File(
                name = DEFAULT_NAME,

                checksum = DEFAULT_CHECKSUM,

                path = DEFAULT_PATH,

                size = DEFAULT_SIZE

            )

            return file
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(em: EntityManager): File {
            val file = File(
                name = UPDATED_NAME,

                checksum = UPDATED_CHECKSUM,

                path = UPDATED_PATH,

                size = UPDATED_SIZE

            )

            return file
        }
    }
}
