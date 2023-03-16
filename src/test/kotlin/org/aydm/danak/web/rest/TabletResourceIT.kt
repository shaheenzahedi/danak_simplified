package org.aydm.danak.web.rest

import org.assertj.core.api.Assertions.assertThat
import org.aydm.danak.IntegrationTest
import org.aydm.danak.domain.Tablet
import org.aydm.danak.repository.TabletRepository
import org.aydm.danak.service.mapper.TabletMapper
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
 * Integration tests for the [TabletResource] REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TabletResourceIT {
    @Autowired
    private lateinit var tabletRepository: TabletRepository

    @Autowired
    private lateinit var tabletMapper: TabletMapper

    @Autowired
    private lateinit var jacksonMessageConverter: MappingJackson2HttpMessageConverter

    @Autowired
    private lateinit var pageableArgumentResolver: PageableHandlerMethodArgumentResolver

    @Autowired
    private lateinit var validator: Validator

    @Autowired
    private lateinit var em: EntityManager

    @Autowired
    private lateinit var restTabletMockMvc: MockMvc

    private lateinit var tablet: Tablet

    @BeforeEach
    fun initTest() {
        tablet = createEntity(em)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createTablet() {
        val databaseSizeBeforeCreate = tabletRepository.findAll().size
        // Create the Tablet
        val tabletDTO = tabletMapper.toDto(tablet)
        restTabletMockMvc.perform(
            post(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(tabletDTO))
        ).andExpect(status().isCreated)

        // Validate the Tablet in the database
        val tabletList = tabletRepository.findAll()
        assertThat(tabletList).hasSize(databaseSizeBeforeCreate + 1)
        val testTablet = tabletList[tabletList.size - 1]

        assertThat(testTablet.name).isEqualTo(DEFAULT_NAME)
        assertThat(testTablet.androidId).isEqualTo(DEFAULT_ANDROID_ID)
        assertThat(testTablet.macId).isEqualTo(DEFAULT_MAC_ID)
        assertThat(testTablet.model).isEqualTo(DEFAULT_MODEL)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createTabletWithExistingId() {
        // Create the Tablet with an existing ID
        tablet.id = 1L
        val tabletDTO = tabletMapper.toDto(tablet)

        val databaseSizeBeforeCreate = tabletRepository.findAll().size

        // An entity with an existing ID cannot be created, so this API call must fail
        restTabletMockMvc.perform(
            post(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(tabletDTO))
        ).andExpect(status().isBadRequest)

        // Validate the Tablet in the database
        val tabletList = tabletRepository.findAll()
        assertThat(tabletList).hasSize(databaseSizeBeforeCreate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllTablets() {
        // Initialize the database
        tabletRepository.saveAndFlush(tablet)

        // Get all the tabletList
        restTabletMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tablet.id?.toInt())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].androidId").value(hasItem(DEFAULT_ANDROID_ID)))
            .andExpect(jsonPath("$.[*].macId").value(hasItem(DEFAULT_MAC_ID)))
            .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL)))
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getTablet() {
        // Initialize the database
        tabletRepository.saveAndFlush(tablet)

        val id = tablet.id
        assertNotNull(id)

        // Get the tablet
        restTabletMockMvc.perform(get(ENTITY_API_URL_ID, tablet.id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tablet.id?.toInt()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.androidId").value(DEFAULT_ANDROID_ID))
            .andExpect(jsonPath("$.macId").value(DEFAULT_MAC_ID))
            .andExpect(jsonPath("$.model").value(DEFAULT_MODEL))
    }
    @Test
    @Transactional
    @Throws(Exception::class)
    fun getNonExistingTablet() {
        // Get the tablet
        restTabletMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }
    @Test
    @Transactional
    fun putNewTablet() {
        // Initialize the database
        tabletRepository.saveAndFlush(tablet)

        val databaseSizeBeforeUpdate = tabletRepository.findAll().size

        // Update the tablet
        val updatedTablet = tabletRepository.findById(tablet.id).get()
        // Disconnect from session so that the updates on updatedTablet are not directly saved in db
        em.detach(updatedTablet)
        updatedTablet.name = UPDATED_NAME
        updatedTablet.androidId = UPDATED_ANDROID_ID
        updatedTablet.macId = UPDATED_MAC_ID
        updatedTablet.model = UPDATED_MODEL
        val tabletDTO = tabletMapper.toDto(updatedTablet)

        restTabletMockMvc.perform(
            put(ENTITY_API_URL_ID, tabletDTO.id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(tabletDTO))
        ).andExpect(status().isOk)

        // Validate the Tablet in the database
        val tabletList = tabletRepository.findAll()
        assertThat(tabletList).hasSize(databaseSizeBeforeUpdate)
        val testTablet = tabletList[tabletList.size - 1]
        assertThat(testTablet.name).isEqualTo(UPDATED_NAME)
        assertThat(testTablet.androidId).isEqualTo(UPDATED_ANDROID_ID)
        assertThat(testTablet.macId).isEqualTo(UPDATED_MAC_ID)
        assertThat(testTablet.model).isEqualTo(UPDATED_MODEL)
    }

    @Test
    @Transactional
    fun putNonExistingTablet() {
        val databaseSizeBeforeUpdate = tabletRepository.findAll().size
        tablet.id = count.incrementAndGet()

        // Create the Tablet
        val tabletDTO = tabletMapper.toDto(tablet)

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTabletMockMvc.perform(
            put(ENTITY_API_URL_ID, tabletDTO.id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(tabletDTO))
        )
            .andExpect(status().isBadRequest)

        // Validate the Tablet in the database
        val tabletList = tabletRepository.findAll()
        assertThat(tabletList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun putWithIdMismatchTablet() {
        val databaseSizeBeforeUpdate = tabletRepository.findAll().size
        tablet.id = count.incrementAndGet()

        // Create the Tablet
        val tabletDTO = tabletMapper.toDto(tablet)

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTabletMockMvc.perform(
            put(ENTITY_API_URL_ID, count.incrementAndGet())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(tabletDTO))
        ).andExpect(status().isBadRequest)

        // Validate the Tablet in the database
        val tabletList = tabletRepository.findAll()
        assertThat(tabletList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun putWithMissingIdPathParamTablet() {
        val databaseSizeBeforeUpdate = tabletRepository.findAll().size
        tablet.id = count.incrementAndGet()

        // Create the Tablet
        val tabletDTO = tabletMapper.toDto(tablet)

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTabletMockMvc.perform(
            put(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(tabletDTO))
        )
            .andExpect(status().isMethodNotAllowed)

        // Validate the Tablet in the database
        val tabletList = tabletRepository.findAll()
        assertThat(tabletList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun partialUpdateTabletWithPatch() {
        tabletRepository.saveAndFlush(tablet)

        val databaseSizeBeforeUpdate = tabletRepository.findAll().size

// Update the tablet using partial update
        val partialUpdatedTablet = Tablet().apply {
            id = tablet.id

            name = UPDATED_NAME
            androidId = UPDATED_ANDROID_ID
            macId = UPDATED_MAC_ID
            model = UPDATED_MODEL
        }

        restTabletMockMvc.perform(
            patch(ENTITY_API_URL_ID, partialUpdatedTablet.id)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(partialUpdatedTablet))
        )
            .andExpect(status().isOk)

// Validate the Tablet in the database
        val tabletList = tabletRepository.findAll()
        assertThat(tabletList).hasSize(databaseSizeBeforeUpdate)
        val testTablet = tabletList.last()
        assertThat(testTablet.name).isEqualTo(UPDATED_NAME)
        assertThat(testTablet.androidId).isEqualTo(UPDATED_ANDROID_ID)
        assertThat(testTablet.macId).isEqualTo(UPDATED_MAC_ID)
        assertThat(testTablet.model).isEqualTo(UPDATED_MODEL)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun fullUpdateTabletWithPatch() {
        tabletRepository.saveAndFlush(tablet)

        val databaseSizeBeforeUpdate = tabletRepository.findAll().size

// Update the tablet using partial update
        val partialUpdatedTablet = Tablet().apply {
            id = tablet.id

            name = UPDATED_NAME
            androidId = UPDATED_ANDROID_ID
            macId = UPDATED_MAC_ID
            model = UPDATED_MODEL
        }

        restTabletMockMvc.perform(
            patch(ENTITY_API_URL_ID, partialUpdatedTablet.id)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(partialUpdatedTablet))
        )
            .andExpect(status().isOk)

// Validate the Tablet in the database
        val tabletList = tabletRepository.findAll()
        assertThat(tabletList).hasSize(databaseSizeBeforeUpdate)
        val testTablet = tabletList.last()
        assertThat(testTablet.name).isEqualTo(UPDATED_NAME)
        assertThat(testTablet.androidId).isEqualTo(UPDATED_ANDROID_ID)
        assertThat(testTablet.macId).isEqualTo(UPDATED_MAC_ID)
        assertThat(testTablet.model).isEqualTo(UPDATED_MODEL)
    }

    @Throws(Exception::class)
    fun patchNonExistingTablet() {
        val databaseSizeBeforeUpdate = tabletRepository.findAll().size
        tablet.id = count.incrementAndGet()

        // Create the Tablet
        val tabletDTO = tabletMapper.toDto(tablet)

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTabletMockMvc.perform(
            patch(ENTITY_API_URL_ID, tabletDTO.id)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(tabletDTO))
        )
            .andExpect(status().isBadRequest)

        // Validate the Tablet in the database
        val tabletList = tabletRepository.findAll()
        assertThat(tabletList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun patchWithIdMismatchTablet() {
        val databaseSizeBeforeUpdate = tabletRepository.findAll().size
        tablet.id = count.incrementAndGet()

        // Create the Tablet
        val tabletDTO = tabletMapper.toDto(tablet)

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTabletMockMvc.perform(
            patch(ENTITY_API_URL_ID, count.incrementAndGet())
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(tabletDTO))
        )
            .andExpect(status().isBadRequest)

        // Validate the Tablet in the database
        val tabletList = tabletRepository.findAll()
        assertThat(tabletList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun patchWithMissingIdPathParamTablet() {
        val databaseSizeBeforeUpdate = tabletRepository.findAll().size
        tablet.id = count.incrementAndGet()

        // Create the Tablet
        val tabletDTO = tabletMapper.toDto(tablet)

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTabletMockMvc.perform(
            patch(ENTITY_API_URL)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(tabletDTO))
        )
            .andExpect(status().isMethodNotAllowed)

        // Validate the Tablet in the database
        val tabletList = tabletRepository.findAll()
        assertThat(tabletList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun deleteTablet() {
        // Initialize the database
        tabletRepository.saveAndFlush(tablet)

        val databaseSizeBeforeDelete = tabletRepository.findAll().size

        // Delete the tablet
        restTabletMockMvc.perform(
            delete(ENTITY_API_URL_ID, tablet.id)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val tabletList = tabletRepository.findAll()
        assertThat(tabletList).hasSize(databaseSizeBeforeDelete - 1)
    }

    companion object {

        private const val DEFAULT_NAME = "AAAAAAAAAA"
        private const val UPDATED_NAME = "BBBBBBBBBB"

        private const val DEFAULT_ANDROID_ID = "AAAAAAAAAA"
        private const val UPDATED_ANDROID_ID = "BBBBBBBBBB"

        private const val DEFAULT_MAC_ID = "AAAAAAAAAA"
        private const val UPDATED_MAC_ID = "BBBBBBBBBB"

        private const val DEFAULT_MODEL = "AAAAAAAAAA"
        private const val UPDATED_MODEL = "BBBBBBBBBB"

        private val ENTITY_API_URL: String = "/api/tablets"
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
        fun createEntity(em: EntityManager): Tablet {
            val tablet = Tablet(
                name = DEFAULT_NAME,

                androidId = DEFAULT_ANDROID_ID,

                macId = DEFAULT_MAC_ID,

                model = DEFAULT_MODEL

            )

            return tablet
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(em: EntityManager): Tablet {
            val tablet = Tablet(
                name = UPDATED_NAME,

                androidId = UPDATED_ANDROID_ID,

                macId = UPDATED_MAC_ID,

                model = UPDATED_MODEL

            )

            return tablet
        }
    }
}
