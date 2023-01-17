package org.aydm.danak.web.rest

import org.assertj.core.api.Assertions.assertThat
import org.aydm.danak.IntegrationTest
import org.aydm.danak.domain.TabletUser
import org.aydm.danak.repository.TabletUserRepository
import org.aydm.danak.service.mapper.TabletUserMapper
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
 * Integration tests for the [TabletUserResource] REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TabletUserResourceIT {
    @Autowired
    private lateinit var tabletUserRepository: TabletUserRepository

    @Autowired
    private lateinit var tabletUserMapper: TabletUserMapper

    @Autowired
    private lateinit var jacksonMessageConverter: MappingJackson2HttpMessageConverter

    @Autowired
    private lateinit var pageableArgumentResolver: PageableHandlerMethodArgumentResolver

    @Autowired
    private lateinit var validator: Validator

    @Autowired
    private lateinit var em: EntityManager

    @Autowired
    private lateinit var restTabletUserMockMvc: MockMvc

    private lateinit var tabletUser: TabletUser

    @BeforeEach
    fun initTest() {
        tabletUser = createEntity(em)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createTabletUser() {
        val databaseSizeBeforeCreate = tabletUserRepository.findAll().size
        // Create the TabletUser
        val tabletUserDTO = tabletUserMapper.toDto(tabletUser)
        restTabletUserMockMvc.perform(
            post(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(tabletUserDTO))
        ).andExpect(status().isCreated)

        // Validate the TabletUser in the database
        val tabletUserList = tabletUserRepository.findAll()
        assertThat(tabletUserList).hasSize(databaseSizeBeforeCreate + 1)
        val testTabletUser = tabletUserList[tabletUserList.size - 1]

        assertThat(testTabletUser.firstName).isEqualTo(DEFAULT_FIRST_NAME)
        assertThat(testTabletUser.lastName).isEqualTo(DEFAULT_LAST_NAME)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createTabletUserWithExistingId() {
        // Create the TabletUser with an existing ID
        tabletUser.id = 1L
        val tabletUserDTO = tabletUserMapper.toDto(tabletUser)

        val databaseSizeBeforeCreate = tabletUserRepository.findAll().size

        // An entity with an existing ID cannot be created, so this API call must fail
        restTabletUserMockMvc.perform(
            post(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(tabletUserDTO))
        ).andExpect(status().isBadRequest)

        // Validate the TabletUser in the database
        val tabletUserList = tabletUserRepository.findAll()
        assertThat(tabletUserList).hasSize(databaseSizeBeforeCreate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllTabletUsers() {
        // Initialize the database
        tabletUserRepository.saveAndFlush(tabletUser)

        // Get all the tabletUserList
        restTabletUserMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tabletUser.id?.toInt())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getTabletUser() {
        // Initialize the database
        tabletUserRepository.saveAndFlush(tabletUser)

        val id = tabletUser.id
        assertNotNull(id)

        // Get the tabletUser
        restTabletUserMockMvc.perform(get(ENTITY_API_URL_ID, tabletUser.id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tabletUser.id?.toInt()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
    }
    @Test
    @Transactional
    @Throws(Exception::class)
    fun getNonExistingTabletUser() {
        // Get the tabletUser
        restTabletUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }
    @Test
    @Transactional
    fun putNewTabletUser() {
        // Initialize the database
        tabletUserRepository.saveAndFlush(tabletUser)

        val databaseSizeBeforeUpdate = tabletUserRepository.findAll().size

        // Update the tabletUser
        val updatedTabletUser = tabletUserRepository.findById(tabletUser.id).get()
        // Disconnect from session so that the updates on updatedTabletUser are not directly saved in db
        em.detach(updatedTabletUser)
        updatedTabletUser.firstName = UPDATED_FIRST_NAME
        updatedTabletUser.lastName = UPDATED_LAST_NAME
        val tabletUserDTO = tabletUserMapper.toDto(updatedTabletUser)

        restTabletUserMockMvc.perform(
            put(ENTITY_API_URL_ID, tabletUserDTO.id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(tabletUserDTO))
        ).andExpect(status().isOk)

        // Validate the TabletUser in the database
        val tabletUserList = tabletUserRepository.findAll()
        assertThat(tabletUserList).hasSize(databaseSizeBeforeUpdate)
        val testTabletUser = tabletUserList[tabletUserList.size - 1]
        assertThat(testTabletUser.firstName).isEqualTo(UPDATED_FIRST_NAME)
        assertThat(testTabletUser.lastName).isEqualTo(UPDATED_LAST_NAME)
    }

    @Test
    @Transactional
    fun putNonExistingTabletUser() {
        val databaseSizeBeforeUpdate = tabletUserRepository.findAll().size
        tabletUser.id = count.incrementAndGet()

        // Create the TabletUser
        val tabletUserDTO = tabletUserMapper.toDto(tabletUser)

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTabletUserMockMvc.perform(
            put(ENTITY_API_URL_ID, tabletUserDTO.id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(tabletUserDTO))
        )
            .andExpect(status().isBadRequest)

        // Validate the TabletUser in the database
        val tabletUserList = tabletUserRepository.findAll()
        assertThat(tabletUserList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun putWithIdMismatchTabletUser() {
        val databaseSizeBeforeUpdate = tabletUserRepository.findAll().size
        tabletUser.id = count.incrementAndGet()

        // Create the TabletUser
        val tabletUserDTO = tabletUserMapper.toDto(tabletUser)

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTabletUserMockMvc.perform(
            put(ENTITY_API_URL_ID, count.incrementAndGet())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(tabletUserDTO))
        ).andExpect(status().isBadRequest)

        // Validate the TabletUser in the database
        val tabletUserList = tabletUserRepository.findAll()
        assertThat(tabletUserList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun putWithMissingIdPathParamTabletUser() {
        val databaseSizeBeforeUpdate = tabletUserRepository.findAll().size
        tabletUser.id = count.incrementAndGet()

        // Create the TabletUser
        val tabletUserDTO = tabletUserMapper.toDto(tabletUser)

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTabletUserMockMvc.perform(
            put(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(tabletUserDTO))
        )
            .andExpect(status().isMethodNotAllowed)

        // Validate the TabletUser in the database
        val tabletUserList = tabletUserRepository.findAll()
        assertThat(tabletUserList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun partialUpdateTabletUserWithPatch() {
        tabletUserRepository.saveAndFlush(tabletUser)

        val databaseSizeBeforeUpdate = tabletUserRepository.findAll().size

// Update the tabletUser using partial update
        val partialUpdatedTabletUser = TabletUser().apply {
            id = tabletUser.id

            firstName = UPDATED_FIRST_NAME
        }

        restTabletUserMockMvc.perform(
            patch(ENTITY_API_URL_ID, partialUpdatedTabletUser.id)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(partialUpdatedTabletUser))
        )
            .andExpect(status().isOk)

// Validate the TabletUser in the database
        val tabletUserList = tabletUserRepository.findAll()
        assertThat(tabletUserList).hasSize(databaseSizeBeforeUpdate)
        val testTabletUser = tabletUserList.last()
        assertThat(testTabletUser.firstName).isEqualTo(UPDATED_FIRST_NAME)
        assertThat(testTabletUser.lastName).isEqualTo(DEFAULT_LAST_NAME)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun fullUpdateTabletUserWithPatch() {
        tabletUserRepository.saveAndFlush(tabletUser)

        val databaseSizeBeforeUpdate = tabletUserRepository.findAll().size

// Update the tabletUser using partial update
        val partialUpdatedTabletUser = TabletUser().apply {
            id = tabletUser.id

            firstName = UPDATED_FIRST_NAME
            lastName = UPDATED_LAST_NAME
        }

        restTabletUserMockMvc.perform(
            patch(ENTITY_API_URL_ID, partialUpdatedTabletUser.id)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(partialUpdatedTabletUser))
        )
            .andExpect(status().isOk)

// Validate the TabletUser in the database
        val tabletUserList = tabletUserRepository.findAll()
        assertThat(tabletUserList).hasSize(databaseSizeBeforeUpdate)
        val testTabletUser = tabletUserList.last()
        assertThat(testTabletUser.firstName).isEqualTo(UPDATED_FIRST_NAME)
        assertThat(testTabletUser.lastName).isEqualTo(UPDATED_LAST_NAME)
    }

    @Throws(Exception::class)
    fun patchNonExistingTabletUser() {
        val databaseSizeBeforeUpdate = tabletUserRepository.findAll().size
        tabletUser.id = count.incrementAndGet()

        // Create the TabletUser
        val tabletUserDTO = tabletUserMapper.toDto(tabletUser)

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTabletUserMockMvc.perform(
            patch(ENTITY_API_URL_ID, tabletUserDTO.id)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(tabletUserDTO))
        )
            .andExpect(status().isBadRequest)

        // Validate the TabletUser in the database
        val tabletUserList = tabletUserRepository.findAll()
        assertThat(tabletUserList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun patchWithIdMismatchTabletUser() {
        val databaseSizeBeforeUpdate = tabletUserRepository.findAll().size
        tabletUser.id = count.incrementAndGet()

        // Create the TabletUser
        val tabletUserDTO = tabletUserMapper.toDto(tabletUser)

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTabletUserMockMvc.perform(
            patch(ENTITY_API_URL_ID, count.incrementAndGet())
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(tabletUserDTO))
        )
            .andExpect(status().isBadRequest)

        // Validate the TabletUser in the database
        val tabletUserList = tabletUserRepository.findAll()
        assertThat(tabletUserList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun patchWithMissingIdPathParamTabletUser() {
        val databaseSizeBeforeUpdate = tabletUserRepository.findAll().size
        tabletUser.id = count.incrementAndGet()

        // Create the TabletUser
        val tabletUserDTO = tabletUserMapper.toDto(tabletUser)

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTabletUserMockMvc.perform(
            patch(ENTITY_API_URL)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(tabletUserDTO))
        )
            .andExpect(status().isMethodNotAllowed)

        // Validate the TabletUser in the database
        val tabletUserList = tabletUserRepository.findAll()
        assertThat(tabletUserList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun deleteTabletUser() {
        // Initialize the database
        tabletUserRepository.saveAndFlush(tabletUser)

        val databaseSizeBeforeDelete = tabletUserRepository.findAll().size

        // Delete the tabletUser
        restTabletUserMockMvc.perform(
            delete(ENTITY_API_URL_ID, tabletUser.id)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val tabletUserList = tabletUserRepository.findAll()
        assertThat(tabletUserList).hasSize(databaseSizeBeforeDelete - 1)
    }

    companion object {

        private const val DEFAULT_FIRST_NAME = "AAAAAAAAAA"
        private const val UPDATED_FIRST_NAME = "BBBBBBBBBB"

        private const val DEFAULT_LAST_NAME = "AAAAAAAAAA"
        private const val UPDATED_LAST_NAME = "BBBBBBBBBB"

        private val ENTITY_API_URL: String = "/api/tablet-users"
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
        fun createEntity(em: EntityManager): TabletUser {
            val tabletUser = TabletUser(
                firstName = DEFAULT_FIRST_NAME,

                lastName = DEFAULT_LAST_NAME

            )

            return tabletUser
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(em: EntityManager): TabletUser {
            val tabletUser = TabletUser(
                firstName = UPDATED_FIRST_NAME,

                lastName = UPDATED_LAST_NAME

            )

            return tabletUser
        }
    }
}
