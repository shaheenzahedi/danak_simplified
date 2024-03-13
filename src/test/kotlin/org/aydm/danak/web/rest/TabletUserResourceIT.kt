package org.aydm.danak.web.rest

import org.assertj.core.api.Assertions.assertThat
import org.aydm.danak.IntegrationTest
import org.aydm.danak.domain.Tablet
import org.aydm.danak.domain.TabletUser
import org.aydm.danak.domain.UserActivity
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
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
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

        assertThat(testTabletUser.createTimeStamp).isEqualTo(DEFAULT_CREATE_TIME_STAMP)
        assertThat(testTabletUser.updateTimeStamp).isEqualTo(DEFAULT_UPDATE_TIME_STAMP)
        assertThat(testTabletUser.firstName).isEqualTo(DEFAULT_FIRST_NAME)
        assertThat(testTabletUser.lastName).isEqualTo(DEFAULT_LAST_NAME)
        assertThat(testTabletUser.email).isEqualTo(DEFAULT_EMAIL)
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
            .andExpect(jsonPath("$.[*].createTimeStamp").value(hasItem(DEFAULT_CREATE_TIME_STAMP.toString())))
            .andExpect(jsonPath("$.[*].updateTimeStamp").value(hasItem(DEFAULT_UPDATE_TIME_STAMP.toString())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
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
            .andExpect(jsonPath("$.createTimeStamp").value(DEFAULT_CREATE_TIME_STAMP.toString()))
            .andExpect(jsonPath("$.updateTimeStamp").value(DEFAULT_UPDATE_TIME_STAMP.toString()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getTabletUsersByIdFiltering() {
        // Initialize the database
        tabletUserRepository.saveAndFlush(tabletUser)
        val id = tabletUser.id

        defaultTabletUserShouldBeFound("id.equals=$id")
        defaultTabletUserShouldNotBeFound("id.notEquals=$id")
        defaultTabletUserShouldBeFound("id.greaterThanOrEqual=$id")
        defaultTabletUserShouldNotBeFound("id.greaterThan=$id")

        defaultTabletUserShouldBeFound("id.lessThanOrEqual=$id")
        defaultTabletUserShouldNotBeFound("id.lessThan=$id")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllTabletUsersByCreateTimeStampIsEqualToSomething() {
        // Initialize the database
        tabletUserRepository.saveAndFlush(tabletUser)

        // Get all the tabletUserList where createTimeStamp equals to DEFAULT_CREATE_TIME_STAMP
        defaultTabletUserShouldBeFound("createTimeStamp.equals=$DEFAULT_CREATE_TIME_STAMP")

        // Get all the tabletUserList where createTimeStamp equals to UPDATED_CREATE_TIME_STAMP
        defaultTabletUserShouldNotBeFound("createTimeStamp.equals=$UPDATED_CREATE_TIME_STAMP")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllTabletUsersByCreateTimeStampIsNotEqualToSomething() {
        // Initialize the database
        tabletUserRepository.saveAndFlush(tabletUser)

        // Get all the tabletUserList where createTimeStamp not equals to DEFAULT_CREATE_TIME_STAMP
        defaultTabletUserShouldNotBeFound("createTimeStamp.notEquals=$DEFAULT_CREATE_TIME_STAMP")

        // Get all the tabletUserList where createTimeStamp not equals to UPDATED_CREATE_TIME_STAMP
        defaultTabletUserShouldBeFound("createTimeStamp.notEquals=$UPDATED_CREATE_TIME_STAMP")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllTabletUsersByCreateTimeStampIsInShouldWork() {
        // Initialize the database
        tabletUserRepository.saveAndFlush(tabletUser)

        // Get all the tabletUserList where createTimeStamp in DEFAULT_CREATE_TIME_STAMP or UPDATED_CREATE_TIME_STAMP
        defaultTabletUserShouldBeFound("createTimeStamp.in=$DEFAULT_CREATE_TIME_STAMP,$UPDATED_CREATE_TIME_STAMP")

        // Get all the tabletUserList where createTimeStamp equals to UPDATED_CREATE_TIME_STAMP
        defaultTabletUserShouldNotBeFound("createTimeStamp.in=$UPDATED_CREATE_TIME_STAMP")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllTabletUsersByCreateTimeStampIsNullOrNotNull() {
        // Initialize the database
        tabletUserRepository.saveAndFlush(tabletUser)

        // Get all the tabletUserList where createTimeStamp is not null
        defaultTabletUserShouldBeFound("createTimeStamp.specified=true")

        // Get all the tabletUserList where createTimeStamp is null
        defaultTabletUserShouldNotBeFound("createTimeStamp.specified=false")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllTabletUsersByUpdateTimeStampIsEqualToSomething() {
        // Initialize the database
        tabletUserRepository.saveAndFlush(tabletUser)

        // Get all the tabletUserList where updateTimeStamp equals to DEFAULT_UPDATE_TIME_STAMP
        defaultTabletUserShouldBeFound("updateTimeStamp.equals=$DEFAULT_UPDATE_TIME_STAMP")

        // Get all the tabletUserList where updateTimeStamp equals to UPDATED_UPDATE_TIME_STAMP
        defaultTabletUserShouldNotBeFound("updateTimeStamp.equals=$UPDATED_UPDATE_TIME_STAMP")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllTabletUsersByUpdateTimeStampIsNotEqualToSomething() {
        // Initialize the database
        tabletUserRepository.saveAndFlush(tabletUser)

        // Get all the tabletUserList where updateTimeStamp not equals to DEFAULT_UPDATE_TIME_STAMP
        defaultTabletUserShouldNotBeFound("updateTimeStamp.notEquals=$DEFAULT_UPDATE_TIME_STAMP")

        // Get all the tabletUserList where updateTimeStamp not equals to UPDATED_UPDATE_TIME_STAMP
        defaultTabletUserShouldBeFound("updateTimeStamp.notEquals=$UPDATED_UPDATE_TIME_STAMP")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllTabletUsersByUpdateTimeStampIsInShouldWork() {
        // Initialize the database
        tabletUserRepository.saveAndFlush(tabletUser)

        // Get all the tabletUserList where updateTimeStamp in DEFAULT_UPDATE_TIME_STAMP or UPDATED_UPDATE_TIME_STAMP
        defaultTabletUserShouldBeFound("updateTimeStamp.in=$DEFAULT_UPDATE_TIME_STAMP,$UPDATED_UPDATE_TIME_STAMP")

        // Get all the tabletUserList where updateTimeStamp equals to UPDATED_UPDATE_TIME_STAMP
        defaultTabletUserShouldNotBeFound("updateTimeStamp.in=$UPDATED_UPDATE_TIME_STAMP")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllTabletUsersByUpdateTimeStampIsNullOrNotNull() {
        // Initialize the database
        tabletUserRepository.saveAndFlush(tabletUser)

        // Get all the tabletUserList where updateTimeStamp is not null
        defaultTabletUserShouldBeFound("updateTimeStamp.specified=true")

        // Get all the tabletUserList where updateTimeStamp is null
        defaultTabletUserShouldNotBeFound("updateTimeStamp.specified=false")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllTabletUsersByFirstNameIsEqualToSomething() {
        // Initialize the database
        tabletUserRepository.saveAndFlush(tabletUser)

        // Get all the tabletUserList where firstName equals to DEFAULT_FIRST_NAME
        defaultTabletUserShouldBeFound("firstName.equals=$DEFAULT_FIRST_NAME")

        // Get all the tabletUserList where firstName equals to UPDATED_FIRST_NAME
        defaultTabletUserShouldNotBeFound("firstName.equals=$UPDATED_FIRST_NAME")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllTabletUsersByFirstNameIsNotEqualToSomething() {
        // Initialize the database
        tabletUserRepository.saveAndFlush(tabletUser)

        // Get all the tabletUserList where firstName not equals to DEFAULT_FIRST_NAME
        defaultTabletUserShouldNotBeFound("firstName.notEquals=$DEFAULT_FIRST_NAME")

        // Get all the tabletUserList where firstName not equals to UPDATED_FIRST_NAME
        defaultTabletUserShouldBeFound("firstName.notEquals=$UPDATED_FIRST_NAME")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllTabletUsersByFirstNameIsInShouldWork() {
        // Initialize the database
        tabletUserRepository.saveAndFlush(tabletUser)

        // Get all the tabletUserList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultTabletUserShouldBeFound("firstName.in=$DEFAULT_FIRST_NAME,$UPDATED_FIRST_NAME")

        // Get all the tabletUserList where firstName equals to UPDATED_FIRST_NAME
        defaultTabletUserShouldNotBeFound("firstName.in=$UPDATED_FIRST_NAME")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllTabletUsersByFirstNameIsNullOrNotNull() {
        // Initialize the database
        tabletUserRepository.saveAndFlush(tabletUser)

        // Get all the tabletUserList where firstName is not null
        defaultTabletUserShouldBeFound("firstName.specified=true")

        // Get all the tabletUserList where firstName is null
        defaultTabletUserShouldNotBeFound("firstName.specified=false")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllTabletUsersByFirstNameContainsSomething() {
        // Initialize the database
        tabletUserRepository.saveAndFlush(tabletUser)

        // Get all the tabletUserList where firstName contains DEFAULT_FIRST_NAME
        defaultTabletUserShouldBeFound("firstName.contains=$DEFAULT_FIRST_NAME")

        // Get all the tabletUserList where firstName contains UPDATED_FIRST_NAME
        defaultTabletUserShouldNotBeFound("firstName.contains=$UPDATED_FIRST_NAME")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllTabletUsersByFirstNameNotContainsSomething() {
        // Initialize the database
        tabletUserRepository.saveAndFlush(tabletUser)

        // Get all the tabletUserList where firstName does not contain DEFAULT_FIRST_NAME
        defaultTabletUserShouldNotBeFound("firstName.doesNotContain=$DEFAULT_FIRST_NAME")

        // Get all the tabletUserList where firstName does not contain UPDATED_FIRST_NAME
        defaultTabletUserShouldBeFound("firstName.doesNotContain=$UPDATED_FIRST_NAME")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllTabletUsersByLastNameIsEqualToSomething() {
        // Initialize the database
        tabletUserRepository.saveAndFlush(tabletUser)

        // Get all the tabletUserList where lastName equals to DEFAULT_LAST_NAME
        defaultTabletUserShouldBeFound("lastName.equals=$DEFAULT_LAST_NAME")

        // Get all the tabletUserList where lastName equals to UPDATED_LAST_NAME
        defaultTabletUserShouldNotBeFound("lastName.equals=$UPDATED_LAST_NAME")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllTabletUsersByLastNameIsNotEqualToSomething() {
        // Initialize the database
        tabletUserRepository.saveAndFlush(tabletUser)

        // Get all the tabletUserList where lastName not equals to DEFAULT_LAST_NAME
        defaultTabletUserShouldNotBeFound("lastName.notEquals=$DEFAULT_LAST_NAME")

        // Get all the tabletUserList where lastName not equals to UPDATED_LAST_NAME
        defaultTabletUserShouldBeFound("lastName.notEquals=$UPDATED_LAST_NAME")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllTabletUsersByLastNameIsInShouldWork() {
        // Initialize the database
        tabletUserRepository.saveAndFlush(tabletUser)

        // Get all the tabletUserList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultTabletUserShouldBeFound("lastName.in=$DEFAULT_LAST_NAME,$UPDATED_LAST_NAME")

        // Get all the tabletUserList where lastName equals to UPDATED_LAST_NAME
        defaultTabletUserShouldNotBeFound("lastName.in=$UPDATED_LAST_NAME")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllTabletUsersByLastNameIsNullOrNotNull() {
        // Initialize the database
        tabletUserRepository.saveAndFlush(tabletUser)

        // Get all the tabletUserList where lastName is not null
        defaultTabletUserShouldBeFound("lastName.specified=true")

        // Get all the tabletUserList where lastName is null
        defaultTabletUserShouldNotBeFound("lastName.specified=false")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllTabletUsersByLastNameContainsSomething() {
        // Initialize the database
        tabletUserRepository.saveAndFlush(tabletUser)

        // Get all the tabletUserList where lastName contains DEFAULT_LAST_NAME
        defaultTabletUserShouldBeFound("lastName.contains=$DEFAULT_LAST_NAME")

        // Get all the tabletUserList where lastName contains UPDATED_LAST_NAME
        defaultTabletUserShouldNotBeFound("lastName.contains=$UPDATED_LAST_NAME")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllTabletUsersByLastNameNotContainsSomething() {
        // Initialize the database
        tabletUserRepository.saveAndFlush(tabletUser)

        // Get all the tabletUserList where lastName does not contain DEFAULT_LAST_NAME
        defaultTabletUserShouldNotBeFound("lastName.doesNotContain=$DEFAULT_LAST_NAME")

        // Get all the tabletUserList where lastName does not contain UPDATED_LAST_NAME
        defaultTabletUserShouldBeFound("lastName.doesNotContain=$UPDATED_LAST_NAME")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllTabletUsersByEmailIsEqualToSomething() {
        // Initialize the database
        tabletUserRepository.saveAndFlush(tabletUser)

        // Get all the tabletUserList where email equals to DEFAULT_EMAIL
        defaultTabletUserShouldBeFound("email.equals=$DEFAULT_EMAIL")

        // Get all the tabletUserList where email equals to UPDATED_EMAIL
        defaultTabletUserShouldNotBeFound("email.equals=$UPDATED_EMAIL")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllTabletUsersByEmailIsNotEqualToSomething() {
        // Initialize the database
        tabletUserRepository.saveAndFlush(tabletUser)

        // Get all the tabletUserList where email not equals to DEFAULT_EMAIL
        defaultTabletUserShouldNotBeFound("email.notEquals=$DEFAULT_EMAIL")

        // Get all the tabletUserList where email not equals to UPDATED_EMAIL
        defaultTabletUserShouldBeFound("email.notEquals=$UPDATED_EMAIL")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllTabletUsersByEmailIsInShouldWork() {
        // Initialize the database
        tabletUserRepository.saveAndFlush(tabletUser)

        // Get all the tabletUserList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultTabletUserShouldBeFound("email.in=$DEFAULT_EMAIL,$UPDATED_EMAIL")

        // Get all the tabletUserList where email equals to UPDATED_EMAIL
        defaultTabletUserShouldNotBeFound("email.in=$UPDATED_EMAIL")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllTabletUsersByEmailIsNullOrNotNull() {
        // Initialize the database
        tabletUserRepository.saveAndFlush(tabletUser)

        // Get all the tabletUserList where email is not null
        defaultTabletUserShouldBeFound("email.specified=true")

        // Get all the tabletUserList where email is null
        defaultTabletUserShouldNotBeFound("email.specified=false")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllTabletUsersByEmailContainsSomething() {
        // Initialize the database
        tabletUserRepository.saveAndFlush(tabletUser)

        // Get all the tabletUserList where email contains DEFAULT_EMAIL
        defaultTabletUserShouldBeFound("email.contains=$DEFAULT_EMAIL")

        // Get all the tabletUserList where email contains UPDATED_EMAIL
        defaultTabletUserShouldNotBeFound("email.contains=$UPDATED_EMAIL")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllTabletUsersByEmailNotContainsSomething() {
        // Initialize the database
        tabletUserRepository.saveAndFlush(tabletUser)

        // Get all the tabletUserList where email does not contain DEFAULT_EMAIL
        defaultTabletUserShouldNotBeFound("email.doesNotContain=$DEFAULT_EMAIL")

        // Get all the tabletUserList where email does not contain UPDATED_EMAIL
        defaultTabletUserShouldBeFound("email.doesNotContain=$UPDATED_EMAIL")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllTabletUsersByUserActivityIsEqualToSomething() {
        // Initialize the database
        tabletUserRepository.saveAndFlush(tabletUser)
        var userActivity: UserActivity
        if (findAll(em, UserActivity::class).isEmpty()) {
            userActivity = UserActivityResourceIT.createEntity(em)
            em.persist(userActivity)
            em.flush()
        } else {
            userActivity = findAll(em, UserActivity::class)[0]
        }
        em.persist(userActivity)
        em.flush()
        tabletUser.addUserActivity(userActivity)
        tabletUserRepository.saveAndFlush(tabletUser)
        val userActivityId = userActivity?.id

        // Get all the tabletUserList where userActivity equals to userActivityId
        defaultTabletUserShouldBeFound("userActivityId.equals=$userActivityId")

        // Get all the tabletUserList where userActivity equals to (userActivityId?.plus(1))
        defaultTabletUserShouldNotBeFound("userActivityId.equals=${(userActivityId?.plus(1))}")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllTabletUsersByTabletIsEqualToSomething() {
        // Initialize the database
        tabletUserRepository.saveAndFlush(tabletUser)
        var tablet: Tablet
        if (findAll(em, Tablet::class).isEmpty()) {
            tablet = TabletResourceIT.createEntity(em)
            em.persist(tablet)
            em.flush()
        } else {
            tablet = findAll(em, Tablet::class)[0]
        }
        em.persist(tablet)
        em.flush()
        tabletUser.tablet = tablet
        tabletUserRepository.saveAndFlush(tabletUser)
        val tabletId = tablet?.id

        // Get all the tabletUserList where tablet equals to tabletId
        defaultTabletUserShouldBeFound("tabletId.equals=$tabletId")

        // Get all the tabletUserList where tablet equals to (tabletId?.plus(1))
        defaultTabletUserShouldNotBeFound("tabletId.equals=${(tabletId?.plus(1))}")
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */

    @Throws(Exception::class)
    private fun defaultTabletUserShouldBeFound(filter: String) {
        restTabletUserMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tabletUser.id?.toInt())))
            .andExpect(jsonPath("$.[*].createTimeStamp").value(hasItem(DEFAULT_CREATE_TIME_STAMP.toString())))
            .andExpect(jsonPath("$.[*].updateTimeStamp").value(hasItem(DEFAULT_UPDATE_TIME_STAMP.toString())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))

        // Check, that the count call also returns 1
        restTabletUserMockMvc.perform(get(ENTITY_API_URL + "/count?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"))
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    @Throws(Exception::class)
    private fun defaultTabletUserShouldNotBeFound(filter: String) {
        restTabletUserMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$").isEmpty)

        // Check, that the count call also returns 0
        restTabletUserMockMvc.perform(get(ENTITY_API_URL + "/count?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"))
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
        val updatedTabletUser = tabletUserRepository.findById(tabletUser.id).orElseThrow()
        // Disconnect from session so that the updates on updatedTabletUser are not directly saved in db
        em.detach(updatedTabletUser)
        updatedTabletUser.createTimeStamp = UPDATED_CREATE_TIME_STAMP
        updatedTabletUser.updateTimeStamp = UPDATED_UPDATE_TIME_STAMP
        updatedTabletUser.firstName = UPDATED_FIRST_NAME
        updatedTabletUser.lastName = UPDATED_LAST_NAME
        updatedTabletUser.email = UPDATED_EMAIL
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
        assertThat(testTabletUser.createTimeStamp).isEqualTo(UPDATED_CREATE_TIME_STAMP)
        assertThat(testTabletUser.updateTimeStamp).isEqualTo(UPDATED_UPDATE_TIME_STAMP)
        assertThat(testTabletUser.firstName).isEqualTo(UPDATED_FIRST_NAME)
        assertThat(testTabletUser.lastName).isEqualTo(UPDATED_LAST_NAME)
        assertThat(testTabletUser.email).isEqualTo(UPDATED_EMAIL)
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

            createTimeStamp = UPDATED_CREATE_TIME_STAMP
            firstName = UPDATED_FIRST_NAME
            email = UPDATED_EMAIL
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
        assertThat(testTabletUser.createTimeStamp).isEqualTo(UPDATED_CREATE_TIME_STAMP)
        assertThat(testTabletUser.updateTimeStamp).isEqualTo(DEFAULT_UPDATE_TIME_STAMP)
        assertThat(testTabletUser.firstName).isEqualTo(UPDATED_FIRST_NAME)
        assertThat(testTabletUser.lastName).isEqualTo(DEFAULT_LAST_NAME)
        assertThat(testTabletUser.email).isEqualTo(UPDATED_EMAIL)
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

            createTimeStamp = UPDATED_CREATE_TIME_STAMP
            updateTimeStamp = UPDATED_UPDATE_TIME_STAMP
            firstName = UPDATED_FIRST_NAME
            lastName = UPDATED_LAST_NAME
            email = UPDATED_EMAIL
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
        assertThat(testTabletUser.createTimeStamp).isEqualTo(UPDATED_CREATE_TIME_STAMP)
        assertThat(testTabletUser.updateTimeStamp).isEqualTo(UPDATED_UPDATE_TIME_STAMP)
        assertThat(testTabletUser.firstName).isEqualTo(UPDATED_FIRST_NAME)
        assertThat(testTabletUser.lastName).isEqualTo(UPDATED_LAST_NAME)
        assertThat(testTabletUser.email).isEqualTo(UPDATED_EMAIL)
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

        private val DEFAULT_CREATE_TIME_STAMP: Instant = Instant.ofEpochMilli(0L)
        private val UPDATED_CREATE_TIME_STAMP: Instant = Instant.now().truncatedTo(ChronoUnit.MILLIS)

        private val DEFAULT_UPDATE_TIME_STAMP: Instant = Instant.ofEpochMilli(0L)
        private val UPDATED_UPDATE_TIME_STAMP: Instant = Instant.now().truncatedTo(ChronoUnit.MILLIS)

        private const val DEFAULT_FIRST_NAME = "AAAAAAAAAA"
        private const val UPDATED_FIRST_NAME = "BBBBBBBBBB"

        private const val DEFAULT_LAST_NAME = "AAAAAAAAAA"
        private const val UPDATED_LAST_NAME = "BBBBBBBBBB"

        private const val DEFAULT_EMAIL = "AAAAAAAAAA"
        private const val UPDATED_EMAIL = "BBBBBBBBBB"

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
                createTimeStamp = DEFAULT_CREATE_TIME_STAMP,

                updateTimeStamp = DEFAULT_UPDATE_TIME_STAMP,

                firstName = DEFAULT_FIRST_NAME,

                lastName = DEFAULT_LAST_NAME,

                email = DEFAULT_EMAIL

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
                createTimeStamp = UPDATED_CREATE_TIME_STAMP,

                updateTimeStamp = UPDATED_UPDATE_TIME_STAMP,

                firstName = UPDATED_FIRST_NAME,

                lastName = UPDATED_LAST_NAME,

                email = UPDATED_EMAIL

            )

            return tabletUser
        }
    }
}
