package org.aydm.danak.web.rest

import org.assertj.core.api.Assertions.assertThat
import org.aydm.danak.IntegrationTest
import org.aydm.danak.domain.File
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

                path = DEFAULT_PATH

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

                path = UPDATED_PATH

            )

            return file
        }
    }
}
