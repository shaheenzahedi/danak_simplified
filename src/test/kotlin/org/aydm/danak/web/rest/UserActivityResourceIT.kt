package org.aydm.danak.web.rest

import org.assertj.core.api.Assertions.assertThat
import org.aydm.danak.IntegrationTest
import org.aydm.danak.domain.TabletUser
import org.aydm.danak.domain.UserActivity
import org.aydm.danak.repository.UserActivityRepository
import org.aydm.danak.service.mapper.UserActivityMapper
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
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.concurrent.atomic.AtomicLong
import javax.persistence.EntityManager
import kotlin.test.assertNotNull

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
        restUserActivityMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userActivity.id?.toInt())))
            .andExpect(jsonPath("$.[*].createTimeStamp").value(hasItem(DEFAULT_CREATE_TIME_STAMP.toString())))
            .andExpect(jsonPath("$.[*].updateTimeStamp").value(hasItem(DEFAULT_UPDATE_TIME_STAMP.toString())))
            .andExpect(jsonPath("$.[*].deviceTimeStamp").value(hasItem(DEFAULT_DEVICE_TIME_STAMP.toString())))
            .andExpect(jsonPath("$.[*].listName").value(hasItem(DEFAULT_LIST_NAME)))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL?.toInt())))
            .andExpect(jsonPath("$.[*].completed").value(hasItem(DEFAULT_COMPLETED?.toInt())))
            .andExpect(jsonPath("$.[*].uniqueName").value(hasItem(DEFAULT_UNIQUE_NAME)))
    }

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
            .andExpect(jsonPath("$.uniqueName").value(DEFAULT_UNIQUE_NAME))
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getUserActivitiesByIdFiltering() {
        // Initialize the database
        userActivityRepository.saveAndFlush(userActivity)
        val id = userActivity.id

        defaultUserActivityShouldBeFound("id.equals=$id")
        defaultUserActivityShouldNotBeFound("id.notEquals=$id")
        defaultUserActivityShouldBeFound("id.greaterThanOrEqual=$id")
        defaultUserActivityShouldNotBeFound("id.greaterThan=$id")

        defaultUserActivityShouldBeFound("id.lessThanOrEqual=$id")
        defaultUserActivityShouldNotBeFound("id.lessThan=$id")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllUserActivitiesByCreateTimeStampIsEqualToSomething() {
        // Initialize the database
        userActivityRepository.saveAndFlush(userActivity)

        // Get all the userActivityList where createTimeStamp equals to DEFAULT_CREATE_TIME_STAMP
        defaultUserActivityShouldBeFound("createTimeStamp.equals=$DEFAULT_CREATE_TIME_STAMP")

        // Get all the userActivityList where createTimeStamp equals to UPDATED_CREATE_TIME_STAMP
        defaultUserActivityShouldNotBeFound("createTimeStamp.equals=$UPDATED_CREATE_TIME_STAMP")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllUserActivitiesByCreateTimeStampIsNotEqualToSomething() {
        // Initialize the database
        userActivityRepository.saveAndFlush(userActivity)

        // Get all the userActivityList where createTimeStamp not equals to DEFAULT_CREATE_TIME_STAMP
        defaultUserActivityShouldNotBeFound("createTimeStamp.notEquals=$DEFAULT_CREATE_TIME_STAMP")

        // Get all the userActivityList where createTimeStamp not equals to UPDATED_CREATE_TIME_STAMP
        defaultUserActivityShouldBeFound("createTimeStamp.notEquals=$UPDATED_CREATE_TIME_STAMP")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllUserActivitiesByCreateTimeStampIsInShouldWork() {
        // Initialize the database
        userActivityRepository.saveAndFlush(userActivity)

        // Get all the userActivityList where createTimeStamp in DEFAULT_CREATE_TIME_STAMP or UPDATED_CREATE_TIME_STAMP
        defaultUserActivityShouldBeFound("createTimeStamp.in=$DEFAULT_CREATE_TIME_STAMP,$UPDATED_CREATE_TIME_STAMP")

        // Get all the userActivityList where createTimeStamp equals to UPDATED_CREATE_TIME_STAMP
        defaultUserActivityShouldNotBeFound("createTimeStamp.in=$UPDATED_CREATE_TIME_STAMP")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllUserActivitiesByCreateTimeStampIsNullOrNotNull() {
        // Initialize the database
        userActivityRepository.saveAndFlush(userActivity)

        // Get all the userActivityList where createTimeStamp is not null
        defaultUserActivityShouldBeFound("createTimeStamp.specified=true")

        // Get all the userActivityList where createTimeStamp is null
        defaultUserActivityShouldNotBeFound("createTimeStamp.specified=false")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllUserActivitiesByUpdateTimeStampIsEqualToSomething() {
        // Initialize the database
        userActivityRepository.saveAndFlush(userActivity)

        // Get all the userActivityList where updateTimeStamp equals to DEFAULT_UPDATE_TIME_STAMP
        defaultUserActivityShouldBeFound("updateTimeStamp.equals=$DEFAULT_UPDATE_TIME_STAMP")

        // Get all the userActivityList where updateTimeStamp equals to UPDATED_UPDATE_TIME_STAMP
        defaultUserActivityShouldNotBeFound("updateTimeStamp.equals=$UPDATED_UPDATE_TIME_STAMP")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllUserActivitiesByUpdateTimeStampIsNotEqualToSomething() {
        // Initialize the database
        userActivityRepository.saveAndFlush(userActivity)

        // Get all the userActivityList where updateTimeStamp not equals to DEFAULT_UPDATE_TIME_STAMP
        defaultUserActivityShouldNotBeFound("updateTimeStamp.notEquals=$DEFAULT_UPDATE_TIME_STAMP")

        // Get all the userActivityList where updateTimeStamp not equals to UPDATED_UPDATE_TIME_STAMP
        defaultUserActivityShouldBeFound("updateTimeStamp.notEquals=$UPDATED_UPDATE_TIME_STAMP")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllUserActivitiesByUpdateTimeStampIsInShouldWork() {
        // Initialize the database
        userActivityRepository.saveAndFlush(userActivity)

        // Get all the userActivityList where updateTimeStamp in DEFAULT_UPDATE_TIME_STAMP or UPDATED_UPDATE_TIME_STAMP
        defaultUserActivityShouldBeFound("updateTimeStamp.in=$DEFAULT_UPDATE_TIME_STAMP,$UPDATED_UPDATE_TIME_STAMP")

        // Get all the userActivityList where updateTimeStamp equals to UPDATED_UPDATE_TIME_STAMP
        defaultUserActivityShouldNotBeFound("updateTimeStamp.in=$UPDATED_UPDATE_TIME_STAMP")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllUserActivitiesByUpdateTimeStampIsNullOrNotNull() {
        // Initialize the database
        userActivityRepository.saveAndFlush(userActivity)

        // Get all the userActivityList where updateTimeStamp is not null
        defaultUserActivityShouldBeFound("updateTimeStamp.specified=true")

        // Get all the userActivityList where updateTimeStamp is null
        defaultUserActivityShouldNotBeFound("updateTimeStamp.specified=false")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllUserActivitiesByDeviceTimeStampIsEqualToSomething() {
        // Initialize the database
        userActivityRepository.saveAndFlush(userActivity)

        // Get all the userActivityList where deviceTimeStamp equals to DEFAULT_DEVICE_TIME_STAMP
        defaultUserActivityShouldBeFound("deviceTimeStamp.equals=$DEFAULT_DEVICE_TIME_STAMP")

        // Get all the userActivityList where deviceTimeStamp equals to UPDATED_DEVICE_TIME_STAMP
        defaultUserActivityShouldNotBeFound("deviceTimeStamp.equals=$UPDATED_DEVICE_TIME_STAMP")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllUserActivitiesByDeviceTimeStampIsNotEqualToSomething() {
        // Initialize the database
        userActivityRepository.saveAndFlush(userActivity)

        // Get all the userActivityList where deviceTimeStamp not equals to DEFAULT_DEVICE_TIME_STAMP
        defaultUserActivityShouldNotBeFound("deviceTimeStamp.notEquals=$DEFAULT_DEVICE_TIME_STAMP")

        // Get all the userActivityList where deviceTimeStamp not equals to UPDATED_DEVICE_TIME_STAMP
        defaultUserActivityShouldBeFound("deviceTimeStamp.notEquals=$UPDATED_DEVICE_TIME_STAMP")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllUserActivitiesByDeviceTimeStampIsInShouldWork() {
        // Initialize the database
        userActivityRepository.saveAndFlush(userActivity)

        // Get all the userActivityList where deviceTimeStamp in DEFAULT_DEVICE_TIME_STAMP or UPDATED_DEVICE_TIME_STAMP
        defaultUserActivityShouldBeFound("deviceTimeStamp.in=$DEFAULT_DEVICE_TIME_STAMP,$UPDATED_DEVICE_TIME_STAMP")

        // Get all the userActivityList where deviceTimeStamp equals to UPDATED_DEVICE_TIME_STAMP
        defaultUserActivityShouldNotBeFound("deviceTimeStamp.in=$UPDATED_DEVICE_TIME_STAMP")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllUserActivitiesByDeviceTimeStampIsNullOrNotNull() {
        // Initialize the database
        userActivityRepository.saveAndFlush(userActivity)

        // Get all the userActivityList where deviceTimeStamp is not null
        defaultUserActivityShouldBeFound("deviceTimeStamp.specified=true")

        // Get all the userActivityList where deviceTimeStamp is null
        defaultUserActivityShouldNotBeFound("deviceTimeStamp.specified=false")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllUserActivitiesByListNameIsEqualToSomething() {
        // Initialize the database
        userActivityRepository.saveAndFlush(userActivity)

        // Get all the userActivityList where listName equals to DEFAULT_LIST_NAME
        defaultUserActivityShouldBeFound("listName.equals=$DEFAULT_LIST_NAME")

        // Get all the userActivityList where listName equals to UPDATED_LIST_NAME
        defaultUserActivityShouldNotBeFound("listName.equals=$UPDATED_LIST_NAME")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllUserActivitiesByListNameIsNotEqualToSomething() {
        // Initialize the database
        userActivityRepository.saveAndFlush(userActivity)

        // Get all the userActivityList where listName not equals to DEFAULT_LIST_NAME
        defaultUserActivityShouldNotBeFound("listName.notEquals=$DEFAULT_LIST_NAME")

        // Get all the userActivityList where listName not equals to UPDATED_LIST_NAME
        defaultUserActivityShouldBeFound("listName.notEquals=$UPDATED_LIST_NAME")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllUserActivitiesByListNameIsInShouldWork() {
        // Initialize the database
        userActivityRepository.saveAndFlush(userActivity)

        // Get all the userActivityList where listName in DEFAULT_LIST_NAME or UPDATED_LIST_NAME
        defaultUserActivityShouldBeFound("listName.in=$DEFAULT_LIST_NAME,$UPDATED_LIST_NAME")

        // Get all the userActivityList where listName equals to UPDATED_LIST_NAME
        defaultUserActivityShouldNotBeFound("listName.in=$UPDATED_LIST_NAME")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllUserActivitiesByListNameIsNullOrNotNull() {
        // Initialize the database
        userActivityRepository.saveAndFlush(userActivity)

        // Get all the userActivityList where listName is not null
        defaultUserActivityShouldBeFound("listName.specified=true")

        // Get all the userActivityList where listName is null
        defaultUserActivityShouldNotBeFound("listName.specified=false")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllUserActivitiesByListNameContainsSomething() {
        // Initialize the database
        userActivityRepository.saveAndFlush(userActivity)

        // Get all the userActivityList where listName contains DEFAULT_LIST_NAME
        defaultUserActivityShouldBeFound("listName.contains=$DEFAULT_LIST_NAME")

        // Get all the userActivityList where listName contains UPDATED_LIST_NAME
        defaultUserActivityShouldNotBeFound("listName.contains=$UPDATED_LIST_NAME")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllUserActivitiesByListNameNotContainsSomething() {
        // Initialize the database
        userActivityRepository.saveAndFlush(userActivity)

        // Get all the userActivityList where listName does not contain DEFAULT_LIST_NAME
        defaultUserActivityShouldNotBeFound("listName.doesNotContain=$DEFAULT_LIST_NAME")

        // Get all the userActivityList where listName does not contain UPDATED_LIST_NAME
        defaultUserActivityShouldBeFound("listName.doesNotContain=$UPDATED_LIST_NAME")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllUserActivitiesByTotalIsEqualToSomething() {
        // Initialize the database
        userActivityRepository.saveAndFlush(userActivity)

        // Get all the userActivityList where total equals to DEFAULT_TOTAL
        defaultUserActivityShouldBeFound("total.equals=$DEFAULT_TOTAL")

        // Get all the userActivityList where total equals to UPDATED_TOTAL
        defaultUserActivityShouldNotBeFound("total.equals=$UPDATED_TOTAL")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllUserActivitiesByTotalIsNotEqualToSomething() {
        // Initialize the database
        userActivityRepository.saveAndFlush(userActivity)

        // Get all the userActivityList where total not equals to DEFAULT_TOTAL
        defaultUserActivityShouldNotBeFound("total.notEquals=$DEFAULT_TOTAL")

        // Get all the userActivityList where total not equals to UPDATED_TOTAL
        defaultUserActivityShouldBeFound("total.notEquals=$UPDATED_TOTAL")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllUserActivitiesByTotalIsInShouldWork() {
        // Initialize the database
        userActivityRepository.saveAndFlush(userActivity)

        // Get all the userActivityList where total in DEFAULT_TOTAL or UPDATED_TOTAL
        defaultUserActivityShouldBeFound("total.in=$DEFAULT_TOTAL,$UPDATED_TOTAL")

        // Get all the userActivityList where total equals to UPDATED_TOTAL
        defaultUserActivityShouldNotBeFound("total.in=$UPDATED_TOTAL")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllUserActivitiesByTotalIsNullOrNotNull() {
        // Initialize the database
        userActivityRepository.saveAndFlush(userActivity)

        // Get all the userActivityList where total is not null
        defaultUserActivityShouldBeFound("total.specified=true")

        // Get all the userActivityList where total is null
        defaultUserActivityShouldNotBeFound("total.specified=false")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllUserActivitiesByTotalIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        userActivityRepository.saveAndFlush(userActivity)

        // Get all the userActivityList where total is greater than or equal to DEFAULT_TOTAL
        defaultUserActivityShouldBeFound("total.greaterThanOrEqual=$DEFAULT_TOTAL")

        // Get all the userActivityList where total is greater than or equal to UPDATED_TOTAL
        defaultUserActivityShouldNotBeFound("total.greaterThanOrEqual=$UPDATED_TOTAL")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllUserActivitiesByTotalIsLessThanOrEqualToSomething() {
        // Initialize the database
        userActivityRepository.saveAndFlush(userActivity)

        // Get all the userActivityList where total is less than or equal to DEFAULT_TOTAL
        defaultUserActivityShouldBeFound("total.lessThanOrEqual=$DEFAULT_TOTAL")

        // Get all the userActivityList where total is less than or equal to SMALLER_TOTAL
        defaultUserActivityShouldNotBeFound("total.lessThanOrEqual=$SMALLER_TOTAL")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllUserActivitiesByTotalIsLessThanSomething() {
        // Initialize the database
        userActivityRepository.saveAndFlush(userActivity)

        // Get all the userActivityList where total is less than DEFAULT_TOTAL
        defaultUserActivityShouldNotBeFound("total.lessThan=$DEFAULT_TOTAL")

        // Get all the userActivityList where total is less than UPDATED_TOTAL
        defaultUserActivityShouldBeFound("total.lessThan=$UPDATED_TOTAL")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllUserActivitiesByTotalIsGreaterThanSomething() {
        // Initialize the database
        userActivityRepository.saveAndFlush(userActivity)

        // Get all the userActivityList where total is greater than DEFAULT_TOTAL
        defaultUserActivityShouldNotBeFound("total.greaterThan=$DEFAULT_TOTAL")

        // Get all the userActivityList where total is greater than SMALLER_TOTAL
        defaultUserActivityShouldBeFound("total.greaterThan=$SMALLER_TOTAL")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllUserActivitiesByCompletedIsEqualToSomething() {
        // Initialize the database
        userActivityRepository.saveAndFlush(userActivity)

        // Get all the userActivityList where completed equals to DEFAULT_COMPLETED
        defaultUserActivityShouldBeFound("completed.equals=$DEFAULT_COMPLETED")

        // Get all the userActivityList where completed equals to UPDATED_COMPLETED
        defaultUserActivityShouldNotBeFound("completed.equals=$UPDATED_COMPLETED")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllUserActivitiesByCompletedIsNotEqualToSomething() {
        // Initialize the database
        userActivityRepository.saveAndFlush(userActivity)

        // Get all the userActivityList where completed not equals to DEFAULT_COMPLETED
        defaultUserActivityShouldNotBeFound("completed.notEquals=$DEFAULT_COMPLETED")

        // Get all the userActivityList where completed not equals to UPDATED_COMPLETED
        defaultUserActivityShouldBeFound("completed.notEquals=$UPDATED_COMPLETED")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllUserActivitiesByCompletedIsInShouldWork() {
        // Initialize the database
        userActivityRepository.saveAndFlush(userActivity)

        // Get all the userActivityList where completed in DEFAULT_COMPLETED or UPDATED_COMPLETED
        defaultUserActivityShouldBeFound("completed.in=$DEFAULT_COMPLETED,$UPDATED_COMPLETED")

        // Get all the userActivityList where completed equals to UPDATED_COMPLETED
        defaultUserActivityShouldNotBeFound("completed.in=$UPDATED_COMPLETED")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllUserActivitiesByCompletedIsNullOrNotNull() {
        // Initialize the database
        userActivityRepository.saveAndFlush(userActivity)

        // Get all the userActivityList where completed is not null
        defaultUserActivityShouldBeFound("completed.specified=true")

        // Get all the userActivityList where completed is null
        defaultUserActivityShouldNotBeFound("completed.specified=false")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllUserActivitiesByCompletedIsGreaterThanOrEqualToSomething() {
        // Initialize the database
        userActivityRepository.saveAndFlush(userActivity)

        // Get all the userActivityList where completed is greater than or equal to DEFAULT_COMPLETED
        defaultUserActivityShouldBeFound("completed.greaterThanOrEqual=$DEFAULT_COMPLETED")

        // Get all the userActivityList where completed is greater than or equal to UPDATED_COMPLETED
        defaultUserActivityShouldNotBeFound("completed.greaterThanOrEqual=$UPDATED_COMPLETED")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllUserActivitiesByCompletedIsLessThanOrEqualToSomething() {
        // Initialize the database
        userActivityRepository.saveAndFlush(userActivity)

        // Get all the userActivityList where completed is less than or equal to DEFAULT_COMPLETED
        defaultUserActivityShouldBeFound("completed.lessThanOrEqual=$DEFAULT_COMPLETED")

        // Get all the userActivityList where completed is less than or equal to SMALLER_COMPLETED
        defaultUserActivityShouldNotBeFound("completed.lessThanOrEqual=$SMALLER_COMPLETED")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllUserActivitiesByCompletedIsLessThanSomething() {
        // Initialize the database
        userActivityRepository.saveAndFlush(userActivity)

        // Get all the userActivityList where completed is less than DEFAULT_COMPLETED
        defaultUserActivityShouldNotBeFound("completed.lessThan=$DEFAULT_COMPLETED")

        // Get all the userActivityList where completed is less than UPDATED_COMPLETED
        defaultUserActivityShouldBeFound("completed.lessThan=$UPDATED_COMPLETED")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllUserActivitiesByCompletedIsGreaterThanSomething() {
        // Initialize the database
        userActivityRepository.saveAndFlush(userActivity)

        // Get all the userActivityList where completed is greater than DEFAULT_COMPLETED
        defaultUserActivityShouldNotBeFound("completed.greaterThan=$DEFAULT_COMPLETED")

        // Get all the userActivityList where completed is greater than SMALLER_COMPLETED
        defaultUserActivityShouldBeFound("completed.greaterThan=$SMALLER_COMPLETED")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllUserActivitiesByUniqueNameIsEqualToSomething() {
        // Initialize the database
        userActivityRepository.saveAndFlush(userActivity)

        // Get all the userActivityList where uniqueName equals to DEFAULT_UNIQUE_NAME
        defaultUserActivityShouldBeFound("uniqueName.equals=$DEFAULT_UNIQUE_NAME")

        // Get all the userActivityList where uniqueName equals to UPDATED_UNIQUE_NAME
        defaultUserActivityShouldNotBeFound("uniqueName.equals=$UPDATED_UNIQUE_NAME")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllUserActivitiesByUniqueNameIsNotEqualToSomething() {
        // Initialize the database
        userActivityRepository.saveAndFlush(userActivity)

        // Get all the userActivityList where uniqueName not equals to DEFAULT_UNIQUE_NAME
        defaultUserActivityShouldNotBeFound("uniqueName.notEquals=$DEFAULT_UNIQUE_NAME")

        // Get all the userActivityList where uniqueName not equals to UPDATED_UNIQUE_NAME
        defaultUserActivityShouldBeFound("uniqueName.notEquals=$UPDATED_UNIQUE_NAME")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllUserActivitiesByUniqueNameIsInShouldWork() {
        // Initialize the database
        userActivityRepository.saveAndFlush(userActivity)

        // Get all the userActivityList where uniqueName in DEFAULT_UNIQUE_NAME or UPDATED_UNIQUE_NAME
        defaultUserActivityShouldBeFound("uniqueName.in=$DEFAULT_UNIQUE_NAME,$UPDATED_UNIQUE_NAME")

        // Get all the userActivityList where uniqueName equals to UPDATED_UNIQUE_NAME
        defaultUserActivityShouldNotBeFound("uniqueName.in=$UPDATED_UNIQUE_NAME")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllUserActivitiesByUniqueNameIsNullOrNotNull() {
        // Initialize the database
        userActivityRepository.saveAndFlush(userActivity)

        // Get all the userActivityList where uniqueName is not null
        defaultUserActivityShouldBeFound("uniqueName.specified=true")

        // Get all the userActivityList where uniqueName is null
        defaultUserActivityShouldNotBeFound("uniqueName.specified=false")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllUserActivitiesByUniqueNameContainsSomething() {
        // Initialize the database
        userActivityRepository.saveAndFlush(userActivity)

        // Get all the userActivityList where uniqueName contains DEFAULT_UNIQUE_NAME
        defaultUserActivityShouldBeFound("uniqueName.contains=$DEFAULT_UNIQUE_NAME")

        // Get all the userActivityList where uniqueName contains UPDATED_UNIQUE_NAME
        defaultUserActivityShouldNotBeFound("uniqueName.contains=$UPDATED_UNIQUE_NAME")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllUserActivitiesByUniqueNameNotContainsSomething() {
        // Initialize the database
        userActivityRepository.saveAndFlush(userActivity)

        // Get all the userActivityList where uniqueName does not contain DEFAULT_UNIQUE_NAME
        defaultUserActivityShouldNotBeFound("uniqueName.doesNotContain=$DEFAULT_UNIQUE_NAME")

        // Get all the userActivityList where uniqueName does not contain UPDATED_UNIQUE_NAME
        defaultUserActivityShouldBeFound("uniqueName.doesNotContain=$UPDATED_UNIQUE_NAME")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllUserActivitiesByActivityIsEqualToSomething() {
        // Initialize the database
        userActivityRepository.saveAndFlush(userActivity)
        var activity: TabletUser
        if (findAll(em, TabletUser::class).isEmpty()) {
            activity = TabletUserResourceIT.createEntity(em)
            em.persist(activity)
            em.flush()
        } else {
            activity = findAll(em, TabletUser::class)[0]
        }
        em.persist(activity)
        em.flush()
        userActivity.activity = activity
        userActivityRepository.saveAndFlush(userActivity)
        val activityId = activity?.id

        // Get all the userActivityList where activity equals to activityId
        defaultUserActivityShouldBeFound("activityId.equals=$activityId")

        // Get all the userActivityList where activity equals to (activityId?.plus(1))
        defaultUserActivityShouldNotBeFound("activityId.equals=${(activityId?.plus(1))}")
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */

    @Throws(Exception::class)
    private fun defaultUserActivityShouldBeFound(filter: String) {
        restUserActivityMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userActivity.id?.toInt())))
            .andExpect(jsonPath("$.[*].createTimeStamp").value(hasItem(DEFAULT_CREATE_TIME_STAMP.toString())))
            .andExpect(jsonPath("$.[*].updateTimeStamp").value(hasItem(DEFAULT_UPDATE_TIME_STAMP.toString())))
            .andExpect(jsonPath("$.[*].deviceTimeStamp").value(hasItem(DEFAULT_DEVICE_TIME_STAMP.toString())))
            .andExpect(jsonPath("$.[*].listName").value(hasItem(DEFAULT_LIST_NAME)))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL?.toInt())))
            .andExpect(jsonPath("$.[*].completed").value(hasItem(DEFAULT_COMPLETED?.toInt())))
            .andExpect(jsonPath("$.[*].uniqueName").value(hasItem(DEFAULT_UNIQUE_NAME)))

        // Check, that the count call also returns 1
        restUserActivityMockMvc.perform(get(ENTITY_API_URL + "/count?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"))
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    @Throws(Exception::class)
    private fun defaultUserActivityShouldNotBeFound(filter: String) {
        restUserActivityMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$").isEmpty)

        // Check, that the count call also returns 0
        restUserActivityMockMvc.perform(get(ENTITY_API_URL + "/count?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"))
    }

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
        val updatedUserActivity = userActivityRepository.findById(userActivity.id).orElseThrow()
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
        restUserActivityMockMvc.perform(
            put(ENTITY_API_URL_ID, userActivityDTO.id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(userActivityDTO))
        )
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
        restUserActivityMockMvc.perform(
            put(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(userActivityDTO))
        )
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

        restUserActivityMockMvc.perform(
            patch(ENTITY_API_URL_ID, partialUpdatedUserActivity.id)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(partialUpdatedUserActivity))
        )
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

        restUserActivityMockMvc.perform(
            patch(ENTITY_API_URL_ID, partialUpdatedUserActivity.id)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(partialUpdatedUserActivity))
        )
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
        restUserActivityMockMvc.perform(
            patch(ENTITY_API_URL_ID, userActivityDTO.id)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(userActivityDTO))
        )
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
        restUserActivityMockMvc.perform(
            patch(ENTITY_API_URL_ID, count.incrementAndGet())
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(userActivityDTO))
        )
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
        restUserActivityMockMvc.perform(
            patch(ENTITY_API_URL)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(userActivityDTO))
        )
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
        private const val SMALLER_TOTAL: Long = 1L - 1L

        private const val DEFAULT_COMPLETED: Long = 1L
        private const val UPDATED_COMPLETED: Long = 2L
        private const val SMALLER_COMPLETED: Long = 1L - 1L

        private const val DEFAULT_UNIQUE_NAME = "AAAAAAAAAA"
        private const val UPDATED_UNIQUE_NAME = "BBBBBBBBBB"

        private val ENTITY_API_URL: String = "/api/user-activities"
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
