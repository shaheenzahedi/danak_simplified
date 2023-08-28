package org.aydm.danak.web.rest


import org.aydm.danak.IntegrationTest
import org.aydm.danak.domain.UserActivity
import org.aydm.danak.repository.UserActivityRepository
import org.aydm.danak.service.dto.UserActivityDTO
import org.aydm.danak.service.mapper.UserActivityMapper

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
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Random
import java.util.concurrent.atomic.AtomicLong
import java.util.stream.Stream

import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers.hasItem
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*


/**
 * Integration tests for the [UserActivityResource] REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserActivityResourceIT {
    @Autowired
    private lateinit var userActivityRepository: UserActivityRepository

    @Autowired
    private lateinit var userActivityMapper: UserActivityMapper

    @Autowired
    private lateinit var jacksonMessageConverter: MappingJackson2HttpMessageConverter

    @Autowired
    private lateinit var pageableArgumentResolver: PageableHandlerMethodArgumentResolver

    @Autowired
    private lateinit var validator: Validator


    @Autowired
    private lateinit var em: EntityManager


    @Autowired
    private lateinit var restUserActivityMockMvc: MockMvc

    private lateinit var userActivity: UserActivity


    @BeforeEach
    fun initTest() {
        userActivity = createEntity(em)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createUserActivity() {
        val databaseSizeBeforeCreate = userActivityRepository.findAll().size
        // Create the UserActivity
        val userActivityDTO = userActivityMapper.toDto(userActivity)
        restUserActivityMockMvc.perform(
            post(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(userActivityDTO))
        ).andExpect(status().isCreated)

        // Validate the UserActivity in the database
        val userActivityList = userActivityRepository.findAll()
        assertThat(userActivityList).hasSize(databaseSizeBeforeCreate + 1)
        val testUserActivity = userActivityList[userActivityList.size - 1]

        assertThat(testUserActivity.createTimeStamp).isEqualTo(DEFAULT_CREATE_TIME_STAMP)
        assertThat(testUserActivity.updateTimeStamp).isEqualTo(DEFAULT_UPDATE_TIME_STAMP)
        assertThat(testUserActivity.deviceTimeStamp).isEqualTo(DEFAULT_DEVICE_TIME_STAMP)
        assertThat(testUserActivity.listName).isEqualTo(DEFAULT_LIST_NAME)
        assertThat(testUserActivity.total).isEqualTo(DEFAULT_TOTAL)
        assertThat(testUserActivity.completed).isEqualTo(DEFAULT_COMPLETED)
        assertThat(testUserActivity.uniqueName).isEqualTo(DEFAULT_UNIQUE_NAME)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createUserActivityWithExistingId() {
        // Create the UserActivity with an existing ID
        userActivity.id = 1L
        val userActivityDTO = userActivityMapper.toDto(userActivity)

        val databaseSizeBeforeCreate = userActivityRepository.findAll().size

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserActivityMockMvc.perform(
            post(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(userActivityDTO))
        ).andExpect(status().isBadRequest)

        // Validate the UserActivity in the database
        val userActivityList = userActivityRepository.findAll()
        assertThat(userActivityList).hasSize(databaseSizeBeforeCreate)
    }


    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllUserActivities() {
        // Initialize the database
        userActivityRepository.saveAndFlush(userActivity)

        // Get all the userActivityList
        restUserActivityMockMvc.perform(get(ENTITY_API_URL+ "?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userActivity.id?.toInt())))
            .andExpect(jsonPath("$.[*].createTimeStamp").value(hasItem(DEFAULT_CREATE_TIME_STAMP.toString())))
            .andExpect(jsonPath("$.[*].updateTimeStamp").value(hasItem(DEFAULT_UPDATE_TIME_STAMP.toString())))
            .andExpect(jsonPath("$.[*].deviceTimeStamp").value(hasItem(DEFAULT_DEVICE_TIME_STAMP.toString())))
            .andExpect(jsonPath("$.[*].listName").value(hasItem(DEFAULT_LIST_NAME)))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL?.toInt())))
            .andExpect(jsonPath("$.[*].completed").value(hasItem(DEFAULT_COMPLETED?.toInt())))
            .andExpect(jsonPath("$.[*].uniqueName").value(hasItem(DEFAULT_UNIQUE_NAME)))    }
    
    @Test
    @Transactional
    @Throws(Exception::class)
    fun getUserActivity() {
        // Initialize the database
        userActivityRepository.saveAndFlush(userActivity)

        val id = userActivity.id
        assertNotNull(id)

        // Get the userActivity
        restUserActivityMockMvc.perform(get(ENTITY_API_URL_ID, userActivity.id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userActivity.id?.toInt()))
            .andExpect(jsonPath("$.createTimeStamp").value(DEFAULT_CREATE_TIME_STAMP.toString()))
            .andExpect(jsonPath("$.updateTimeStamp").value(DEFAULT_UPDATE_TIME_STAMP.toString()))
            .andExpect(jsonPath("$.deviceTimeStamp").value(DEFAULT_DEVICE_TIME_STAMP.toString()))
            .andExpect(jsonPath("$.listName").value(DEFAULT_LIST_NAME))
            .andExpect(jsonPath("$.total").value(DEFAULT_TOTAL?.toInt()))
            .andExpect(jsonPath("$.completed").value(DEFAULT_COMPLETED?.toInt()))
            .andExpect(jsonPath("$.uniqueName").value(DEFAULT_UNIQUE_NAME))    }
    @Test
    @Transactional
    @Throws(Exception::class)
    fun getNonExistingUserActivity() {
        // Get the userActivity
        restUserActivityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }
    @Test
    @Transactional
    fun putNewUserActivity() {
        // Initialize the database
        userActivityRepository.saveAndFlush(userActivity)

        val databaseSizeBeforeUpdate = userActivityRepository.findAll().size

        // Update the userActivity
        val updatedUserActivity = userActivityRepository.findById(userActivity.id).get()
        // Disconnect from session so that the updates on updatedUserActivity are not directly saved in db
        em.detach(updatedUserActivity)
        updatedUserActivity.createTimeStamp = UPDATED_CREATE_TIME_STAMP
        updatedUserActivity.updateTimeStamp = UPDATED_UPDATE_TIME_STAMP
        updatedUserActivity.deviceTimeStamp = UPDATED_DEVICE_TIME_STAMP
        updatedUserActivity.listName = UPDATED_LIST_NAME
        updatedUserActivity.total = UPDATED_TOTAL
        updatedUserActivity.completed = UPDATED_COMPLETED
        updatedUserActivity.uniqueName = UPDATED_UNIQUE_NAME
        val userActivityDTO = userActivityMapper.toDto(updatedUserActivity)

        restUserActivityMockMvc.perform(
            put(ENTITY_API_URL_ID, userActivityDTO.id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(userActivityDTO))
        ).andExpect(status().isOk)

        // Validate the UserActivity in the database
        val userActivityList = userActivityRepository.findAll()
        assertThat(userActivityList).hasSize(databaseSizeBeforeUpdate)
        val testUserActivity = userActivityList[userActivityList.size - 1]
        assertThat(testUserActivity.createTimeStamp).isEqualTo(UPDATED_CREATE_TIME_STAMP)
        assertThat(testUserActivity.updateTimeStamp).isEqualTo(UPDATED_UPDATE_TIME_STAMP)
        assertThat(testUserActivity.deviceTimeStamp).isEqualTo(UPDATED_DEVICE_TIME_STAMP)
        assertThat(testUserActivity.listName).isEqualTo(UPDATED_LIST_NAME)
        assertThat(testUserActivity.total).isEqualTo(UPDATED_TOTAL)
        assertThat(testUserActivity.completed).isEqualTo(UPDATED_COMPLETED)
        assertThat(testUserActivity.uniqueName).isEqualTo(UPDATED_UNIQUE_NAME)
    }

    @Test
    @Transactional
    fun putNonExistingUserActivity() {
        val databaseSizeBeforeUpdate = userActivityRepository.findAll().size
        userActivity.id = count.incrementAndGet()

        // Create the UserActivity
        val userActivityDTO = userActivityMapper.toDto(userActivity)

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserActivityMockMvc.perform(put(ENTITY_API_URL_ID, userActivityDTO.id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(convertObjectToJsonBytes(userActivityDTO)))
            .andExpect(status().isBadRequest)

        // Validate the UserActivity in the database
        val userActivityList = userActivityRepository.findAll()
        assertThat(userActivityList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun putWithIdMismatchUserActivity() {
        val databaseSizeBeforeUpdate = userActivityRepository.findAll().size
        userActivity.id = count.incrementAndGet()

        // Create the UserActivity
        val userActivityDTO = userActivityMapper.toDto(userActivity)

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserActivityMockMvc.perform(
            put(ENTITY_API_URL_ID, count.incrementAndGet())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(userActivityDTO))
        ).andExpect(status().isBadRequest)

        // Validate the UserActivity in the database
        val userActivityList = userActivityRepository.findAll()
        assertThat(userActivityList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun putWithMissingIdPathParamUserActivity() {
        val databaseSizeBeforeUpdate = userActivityRepository.findAll().size
        userActivity.id = count.incrementAndGet()

        // Create the UserActivity
        val userActivityDTO = userActivityMapper.toDto(userActivity)

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserActivityMockMvc.perform(put(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(convertObjectToJsonBytes(userActivityDTO)))
            .andExpect(status().isMethodNotAllowed)

        // Validate the UserActivity in the database
        val userActivityList = userActivityRepository.findAll()
        assertThat(userActivityList).hasSize(databaseSizeBeforeUpdate)
    }

    
    @Test
    @Transactional
    @Throws(Exception::class)
    fun partialUpdateUserActivityWithPatch() {
        userActivityRepository.saveAndFlush(userActivity)
        
        
val databaseSizeBeforeUpdate = userActivityRepository.findAll().size

// Update the userActivity using partial update
val partialUpdatedUserActivity = UserActivity().apply {
    id = userActivity.id

    
        deviceTimeStamp = UPDATED_DEVICE_TIME_STAMP
        listName = UPDATED_LIST_NAME
}


restUserActivityMockMvc.perform(patch(ENTITY_API_URL_ID, partialUpdatedUserActivity.id)
.contentType("application/merge-patch+json")
.content(convertObjectToJsonBytes(partialUpdatedUserActivity)))
.andExpect(status().isOk)

// Validate the UserActivity in the database
val userActivityList = userActivityRepository.findAll()
assertThat(userActivityList).hasSize(databaseSizeBeforeUpdate)
val testUserActivity = userActivityList.last()
    assertThat(testUserActivity.createTimeStamp).isEqualTo(DEFAULT_CREATE_TIME_STAMP)
    assertThat(testUserActivity.updateTimeStamp).isEqualTo(DEFAULT_UPDATE_TIME_STAMP)
    assertThat(testUserActivity.deviceTimeStamp).isEqualTo(UPDATED_DEVICE_TIME_STAMP)
    assertThat(testUserActivity.listName).isEqualTo(UPDATED_LIST_NAME)
    assertThat(testUserActivity.total).isEqualTo(DEFAULT_TOTAL)
    assertThat(testUserActivity.completed).isEqualTo(DEFAULT_COMPLETED)
    assertThat(testUserActivity.uniqueName).isEqualTo(DEFAULT_UNIQUE_NAME)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun fullUpdateUserActivityWithPatch() {
        userActivityRepository.saveAndFlush(userActivity)
        
        
val databaseSizeBeforeUpdate = userActivityRepository.findAll().size

// Update the userActivity using partial update
val partialUpdatedUserActivity = UserActivity().apply {
    id = userActivity.id

    
        createTimeStamp = UPDATED_CREATE_TIME_STAMP
        updateTimeStamp = UPDATED_UPDATE_TIME_STAMP
        deviceTimeStamp = UPDATED_DEVICE_TIME_STAMP
        listName = UPDATED_LIST_NAME
        total = UPDATED_TOTAL
        completed = UPDATED_COMPLETED
        uniqueName = UPDATED_UNIQUE_NAME
}


restUserActivityMockMvc.perform(patch(ENTITY_API_URL_ID, partialUpdatedUserActivity.id)
.contentType("application/merge-patch+json")
.content(convertObjectToJsonBytes(partialUpdatedUserActivity)))
.andExpect(status().isOk)

// Validate the UserActivity in the database
val userActivityList = userActivityRepository.findAll()
assertThat(userActivityList).hasSize(databaseSizeBeforeUpdate)
val testUserActivity = userActivityList.last()
    assertThat(testUserActivity.createTimeStamp).isEqualTo(UPDATED_CREATE_TIME_STAMP)
    assertThat(testUserActivity.updateTimeStamp).isEqualTo(UPDATED_UPDATE_TIME_STAMP)
    assertThat(testUserActivity.deviceTimeStamp).isEqualTo(UPDATED_DEVICE_TIME_STAMP)
    assertThat(testUserActivity.listName).isEqualTo(UPDATED_LIST_NAME)
    assertThat(testUserActivity.total).isEqualTo(UPDATED_TOTAL)
    assertThat(testUserActivity.completed).isEqualTo(UPDATED_COMPLETED)
    assertThat(testUserActivity.uniqueName).isEqualTo(UPDATED_UNIQUE_NAME)
    }

    @Throws(Exception::class)
    fun patchNonExistingUserActivity() {
        val databaseSizeBeforeUpdate = userActivityRepository.findAll().size
        userActivity.id = count.incrementAndGet()

        // Create the UserActivity
        val userActivityDTO = userActivityMapper.toDto(userActivity)

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserActivityMockMvc.perform(patch(ENTITY_API_URL_ID, userActivityDTO.id)
            .contentType("application/merge-patch+json")
            .content(convertObjectToJsonBytes(userActivityDTO)))
            .andExpect(status().isBadRequest)

        // Validate the UserActivity in the database
        val userActivityList = userActivityRepository.findAll()
        assertThat(userActivityList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun patchWithIdMismatchUserActivity() {
        val databaseSizeBeforeUpdate = userActivityRepository.findAll().size
        userActivity.id = count.incrementAndGet()

        // Create the UserActivity
        val userActivityDTO = userActivityMapper.toDto(userActivity)

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserActivityMockMvc.perform(patch(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType("application/merge-patch+json")
            .content(convertObjectToJsonBytes(userActivityDTO)))
            .andExpect(status().isBadRequest)

        // Validate the UserActivity in the database
        val userActivityList = userActivityRepository.findAll()
        assertThat(userActivityList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun patchWithMissingIdPathParamUserActivity() {
        val databaseSizeBeforeUpdate = userActivityRepository.findAll().size
        userActivity.id = count.incrementAndGet()

        // Create the UserActivity
        val userActivityDTO = userActivityMapper.toDto(userActivity)

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserActivityMockMvc.perform(patch(ENTITY_API_URL)
            .contentType("application/merge-patch+json")
            .content(convertObjectToJsonBytes(userActivityDTO)))
            .andExpect(status().isMethodNotAllowed)

        // Validate the UserActivity in the database
        val userActivityList = userActivityRepository.findAll()
        assertThat(userActivityList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun deleteUserActivity() {
        // Initialize the database
        userActivityRepository.saveAndFlush(userActivity)

        val databaseSizeBeforeDelete = userActivityRepository.findAll().size

        // Delete the userActivity
        restUserActivityMockMvc.perform(
            delete(ENTITY_API_URL_ID, userActivity.id)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val userActivityList = userActivityRepository.findAll()
        assertThat(userActivityList).hasSize(databaseSizeBeforeDelete - 1)
    }


    companion object {

        private val DEFAULT_CREATE_TIME_STAMP: Instant = Instant.ofEpochMilli(0L)
        private val UPDATED_CREATE_TIME_STAMP: Instant = Instant.now().truncatedTo(ChronoUnit.MILLIS)

        private val DEFAULT_UPDATE_TIME_STAMP: Instant = Instant.ofEpochMilli(0L)
        private val UPDATED_UPDATE_TIME_STAMP: Instant = Instant.now().truncatedTo(ChronoUnit.MILLIS)

        private val DEFAULT_DEVICE_TIME_STAMP: Instant = Instant.ofEpochMilli(0L)
        private val UPDATED_DEVICE_TIME_STAMP: Instant = Instant.now().truncatedTo(ChronoUnit.MILLIS)

        private const val DEFAULT_LIST_NAME = "AAAAAAAAAA"
        private const val UPDATED_LIST_NAME = "BBBBBBBBBB"

        private const val DEFAULT_TOTAL: Long = 1L
        private const val UPDATED_TOTAL: Long = 2L

        private const val DEFAULT_COMPLETED: Long = 1L
        private const val UPDATED_COMPLETED: Long = 2L

        private const val DEFAULT_UNIQUE_NAME = "AAAAAAAAAA"
        private const val UPDATED_UNIQUE_NAME = "BBBBBBBBBB"


        private val ENTITY_API_URL: String = "/api/user-activities"
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
        fun createEntity(em: EntityManager): UserActivity {
            val userActivity = UserActivity(
                createTimeStamp = DEFAULT_CREATE_TIME_STAMP,

                updateTimeStamp = DEFAULT_UPDATE_TIME_STAMP,

                deviceTimeStamp = DEFAULT_DEVICE_TIME_STAMP,

                listName = DEFAULT_LIST_NAME,

                total = DEFAULT_TOTAL,

                completed = DEFAULT_COMPLETED,

                uniqueName = DEFAULT_UNIQUE_NAME

            )


            return userActivity
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(em: EntityManager): UserActivity {
            val userActivity = UserActivity(
                createTimeStamp = UPDATED_CREATE_TIME_STAMP,

                updateTimeStamp = UPDATED_UPDATE_TIME_STAMP,

                deviceTimeStamp = UPDATED_DEVICE_TIME_STAMP,

                listName = UPDATED_LIST_NAME,

                total = UPDATED_TOTAL,

                completed = UPDATED_COMPLETED,

                uniqueName = UPDATED_UNIQUE_NAME

            )


            return userActivity
        }

    }
}
