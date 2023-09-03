package org.aydm.danak.web.rest

import org.assertj.core.api.Assertions.assertThat
import org.aydm.danak.IntegrationTest
import org.aydm.danak.domain.Center
import org.aydm.danak.domain.Donor
import org.aydm.danak.domain.Tablet
import org.aydm.danak.domain.TabletUser
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
import java.time.Instant
import java.time.temporal.ChronoUnit
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

        assertThat(testTablet.createTimeStamp).isEqualTo(DEFAULT_CREATE_TIME_STAMP)
        assertThat(testTablet.updateTimeStamp).isEqualTo(DEFAULT_UPDATE_TIME_STAMP)
        assertThat(testTablet.name).isEqualTo(DEFAULT_NAME)
        assertThat(testTablet.identifier).isEqualTo(DEFAULT_IDENTIFIER)
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
            .andExpect(jsonPath("$.[*].createTimeStamp").value(hasItem(DEFAULT_CREATE_TIME_STAMP.toString())))
            .andExpect(jsonPath("$.[*].updateTimeStamp").value(hasItem(DEFAULT_UPDATE_TIME_STAMP.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].identifier").value(hasItem(DEFAULT_IDENTIFIER)))
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
            .andExpect(jsonPath("$.createTimeStamp").value(DEFAULT_CREATE_TIME_STAMP.toString()))
            .andExpect(jsonPath("$.updateTimeStamp").value(DEFAULT_UPDATE_TIME_STAMP.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.identifier").value(DEFAULT_IDENTIFIER))
            .andExpect(jsonPath("$.model").value(DEFAULT_MODEL))
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getTabletsByIdFiltering() {
        // Initialize the database
        tabletRepository.saveAndFlush(tablet)
        val id = tablet.id

        defaultTabletShouldBeFound("id.equals=$id")
        defaultTabletShouldNotBeFound("id.notEquals=$id")
        defaultTabletShouldBeFound("id.greaterThanOrEqual=$id")
        defaultTabletShouldNotBeFound("id.greaterThan=$id")

        defaultTabletShouldBeFound("id.lessThanOrEqual=$id")
        defaultTabletShouldNotBeFound("id.lessThan=$id")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllTabletsByCreateTimeStampIsEqualToSomething() {
        // Initialize the database
        tabletRepository.saveAndFlush(tablet)

        // Get all the tabletList where createTimeStamp equals to DEFAULT_CREATE_TIME_STAMP
        defaultTabletShouldBeFound("createTimeStamp.equals=$DEFAULT_CREATE_TIME_STAMP")

        // Get all the tabletList where createTimeStamp equals to UPDATED_CREATE_TIME_STAMP
        defaultTabletShouldNotBeFound("createTimeStamp.equals=$UPDATED_CREATE_TIME_STAMP")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllTabletsByCreateTimeStampIsNotEqualToSomething() {
        // Initialize the database
        tabletRepository.saveAndFlush(tablet)

        // Get all the tabletList where createTimeStamp not equals to DEFAULT_CREATE_TIME_STAMP
        defaultTabletShouldNotBeFound("createTimeStamp.notEquals=$DEFAULT_CREATE_TIME_STAMP")

        // Get all the tabletList where createTimeStamp not equals to UPDATED_CREATE_TIME_STAMP
        defaultTabletShouldBeFound("createTimeStamp.notEquals=$UPDATED_CREATE_TIME_STAMP")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllTabletsByCreateTimeStampIsInShouldWork() {
        // Initialize the database
        tabletRepository.saveAndFlush(tablet)

        // Get all the tabletList where createTimeStamp in DEFAULT_CREATE_TIME_STAMP or UPDATED_CREATE_TIME_STAMP
        defaultTabletShouldBeFound("createTimeStamp.in=$DEFAULT_CREATE_TIME_STAMP,$UPDATED_CREATE_TIME_STAMP")

        // Get all the tabletList where createTimeStamp equals to UPDATED_CREATE_TIME_STAMP
        defaultTabletShouldNotBeFound("createTimeStamp.in=$UPDATED_CREATE_TIME_STAMP")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllTabletsByCreateTimeStampIsNullOrNotNull() {
        // Initialize the database
        tabletRepository.saveAndFlush(tablet)

        // Get all the tabletList where createTimeStamp is not null
        defaultTabletShouldBeFound("createTimeStamp.specified=true")

        // Get all the tabletList where createTimeStamp is null
        defaultTabletShouldNotBeFound("createTimeStamp.specified=false")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllTabletsByUpdateTimeStampIsEqualToSomething() {
        // Initialize the database
        tabletRepository.saveAndFlush(tablet)

        // Get all the tabletList where updateTimeStamp equals to DEFAULT_UPDATE_TIME_STAMP
        defaultTabletShouldBeFound("updateTimeStamp.equals=$DEFAULT_UPDATE_TIME_STAMP")

        // Get all the tabletList where updateTimeStamp equals to UPDATED_UPDATE_TIME_STAMP
        defaultTabletShouldNotBeFound("updateTimeStamp.equals=$UPDATED_UPDATE_TIME_STAMP")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllTabletsByUpdateTimeStampIsNotEqualToSomething() {
        // Initialize the database
        tabletRepository.saveAndFlush(tablet)

        // Get all the tabletList where updateTimeStamp not equals to DEFAULT_UPDATE_TIME_STAMP
        defaultTabletShouldNotBeFound("updateTimeStamp.notEquals=$DEFAULT_UPDATE_TIME_STAMP")

        // Get all the tabletList where updateTimeStamp not equals to UPDATED_UPDATE_TIME_STAMP
        defaultTabletShouldBeFound("updateTimeStamp.notEquals=$UPDATED_UPDATE_TIME_STAMP")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllTabletsByUpdateTimeStampIsInShouldWork() {
        // Initialize the database
        tabletRepository.saveAndFlush(tablet)

        // Get all the tabletList where updateTimeStamp in DEFAULT_UPDATE_TIME_STAMP or UPDATED_UPDATE_TIME_STAMP
        defaultTabletShouldBeFound("updateTimeStamp.in=$DEFAULT_UPDATE_TIME_STAMP,$UPDATED_UPDATE_TIME_STAMP")

        // Get all the tabletList where updateTimeStamp equals to UPDATED_UPDATE_TIME_STAMP
        defaultTabletShouldNotBeFound("updateTimeStamp.in=$UPDATED_UPDATE_TIME_STAMP")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllTabletsByUpdateTimeStampIsNullOrNotNull() {
        // Initialize the database
        tabletRepository.saveAndFlush(tablet)

        // Get all the tabletList where updateTimeStamp is not null
        defaultTabletShouldBeFound("updateTimeStamp.specified=true")

        // Get all the tabletList where updateTimeStamp is null
        defaultTabletShouldNotBeFound("updateTimeStamp.specified=false")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllTabletsByNameIsEqualToSomething() {
        // Initialize the database
        tabletRepository.saveAndFlush(tablet)

        // Get all the tabletList where name equals to DEFAULT_NAME
        defaultTabletShouldBeFound("name.equals=$DEFAULT_NAME")

        // Get all the tabletList where name equals to UPDATED_NAME
        defaultTabletShouldNotBeFound("name.equals=$UPDATED_NAME")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllTabletsByNameIsNotEqualToSomething() {
        // Initialize the database
        tabletRepository.saveAndFlush(tablet)

        // Get all the tabletList where name not equals to DEFAULT_NAME
        defaultTabletShouldNotBeFound("name.notEquals=$DEFAULT_NAME")

        // Get all the tabletList where name not equals to UPDATED_NAME
        defaultTabletShouldBeFound("name.notEquals=$UPDATED_NAME")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllTabletsByNameIsInShouldWork() {
        // Initialize the database
        tabletRepository.saveAndFlush(tablet)

        // Get all the tabletList where name in DEFAULT_NAME or UPDATED_NAME
        defaultTabletShouldBeFound("name.in=$DEFAULT_NAME,$UPDATED_NAME")

        // Get all the tabletList where name equals to UPDATED_NAME
        defaultTabletShouldNotBeFound("name.in=$UPDATED_NAME")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllTabletsByNameIsNullOrNotNull() {
        // Initialize the database
        tabletRepository.saveAndFlush(tablet)

        // Get all the tabletList where name is not null
        defaultTabletShouldBeFound("name.specified=true")

        // Get all the tabletList where name is null
        defaultTabletShouldNotBeFound("name.specified=false")
    }
    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllTabletsByNameContainsSomething() {
        // Initialize the database
        tabletRepository.saveAndFlush(tablet)

        // Get all the tabletList where name contains DEFAULT_NAME
        defaultTabletShouldBeFound("name.contains=$DEFAULT_NAME")

        // Get all the tabletList where name contains UPDATED_NAME
        defaultTabletShouldNotBeFound("name.contains=$UPDATED_NAME")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllTabletsByNameNotContainsSomething() {
        // Initialize the database
        tabletRepository.saveAndFlush(tablet)

        // Get all the tabletList where name does not contain DEFAULT_NAME
        defaultTabletShouldNotBeFound("name.doesNotContain=$DEFAULT_NAME")

        // Get all the tabletList where name does not contain UPDATED_NAME
        defaultTabletShouldBeFound("name.doesNotContain=$UPDATED_NAME")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllTabletsByIdentifierIsEqualToSomething() {
        // Initialize the database
        tabletRepository.saveAndFlush(tablet)

        // Get all the tabletList where identifier equals to DEFAULT_IDENTIFIER
        defaultTabletShouldBeFound("identifier.equals=$DEFAULT_IDENTIFIER")

        // Get all the tabletList where identifier equals to UPDATED_IDENTIFIER
        defaultTabletShouldNotBeFound("identifier.equals=$UPDATED_IDENTIFIER")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllTabletsByIdentifierIsNotEqualToSomething() {
        // Initialize the database
        tabletRepository.saveAndFlush(tablet)

        // Get all the tabletList where identifier not equals to DEFAULT_IDENTIFIER
        defaultTabletShouldNotBeFound("identifier.notEquals=$DEFAULT_IDENTIFIER")

        // Get all the tabletList where identifier not equals to UPDATED_IDENTIFIER
        defaultTabletShouldBeFound("identifier.notEquals=$UPDATED_IDENTIFIER")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllTabletsByIdentifierIsInShouldWork() {
        // Initialize the database
        tabletRepository.saveAndFlush(tablet)

        // Get all the tabletList where identifier in DEFAULT_IDENTIFIER or UPDATED_IDENTIFIER
        defaultTabletShouldBeFound("identifier.in=$DEFAULT_IDENTIFIER,$UPDATED_IDENTIFIER")

        // Get all the tabletList where identifier equals to UPDATED_IDENTIFIER
        defaultTabletShouldNotBeFound("identifier.in=$UPDATED_IDENTIFIER")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllTabletsByIdentifierIsNullOrNotNull() {
        // Initialize the database
        tabletRepository.saveAndFlush(tablet)

        // Get all the tabletList where identifier is not null
        defaultTabletShouldBeFound("identifier.specified=true")

        // Get all the tabletList where identifier is null
        defaultTabletShouldNotBeFound("identifier.specified=false")
    }
    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllTabletsByIdentifierContainsSomething() {
        // Initialize the database
        tabletRepository.saveAndFlush(tablet)

        // Get all the tabletList where identifier contains DEFAULT_IDENTIFIER
        defaultTabletShouldBeFound("identifier.contains=$DEFAULT_IDENTIFIER")

        // Get all the tabletList where identifier contains UPDATED_IDENTIFIER
        defaultTabletShouldNotBeFound("identifier.contains=$UPDATED_IDENTIFIER")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllTabletsByIdentifierNotContainsSomething() {
        // Initialize the database
        tabletRepository.saveAndFlush(tablet)

        // Get all the tabletList where identifier does not contain DEFAULT_IDENTIFIER
        defaultTabletShouldNotBeFound("identifier.doesNotContain=$DEFAULT_IDENTIFIER")

        // Get all the tabletList where identifier does not contain UPDATED_IDENTIFIER
        defaultTabletShouldBeFound("identifier.doesNotContain=$UPDATED_IDENTIFIER")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllTabletsByModelIsEqualToSomething() {
        // Initialize the database
        tabletRepository.saveAndFlush(tablet)

        // Get all the tabletList where model equals to DEFAULT_MODEL
        defaultTabletShouldBeFound("model.equals=$DEFAULT_MODEL")

        // Get all the tabletList where model equals to UPDATED_MODEL
        defaultTabletShouldNotBeFound("model.equals=$UPDATED_MODEL")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllTabletsByModelIsNotEqualToSomething() {
        // Initialize the database
        tabletRepository.saveAndFlush(tablet)

        // Get all the tabletList where model not equals to DEFAULT_MODEL
        defaultTabletShouldNotBeFound("model.notEquals=$DEFAULT_MODEL")

        // Get all the tabletList where model not equals to UPDATED_MODEL
        defaultTabletShouldBeFound("model.notEquals=$UPDATED_MODEL")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllTabletsByModelIsInShouldWork() {
        // Initialize the database
        tabletRepository.saveAndFlush(tablet)

        // Get all the tabletList where model in DEFAULT_MODEL or UPDATED_MODEL
        defaultTabletShouldBeFound("model.in=$DEFAULT_MODEL,$UPDATED_MODEL")

        // Get all the tabletList where model equals to UPDATED_MODEL
        defaultTabletShouldNotBeFound("model.in=$UPDATED_MODEL")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllTabletsByModelIsNullOrNotNull() {
        // Initialize the database
        tabletRepository.saveAndFlush(tablet)

        // Get all the tabletList where model is not null
        defaultTabletShouldBeFound("model.specified=true")

        // Get all the tabletList where model is null
        defaultTabletShouldNotBeFound("model.specified=false")
    }
    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllTabletsByModelContainsSomething() {
        // Initialize the database
        tabletRepository.saveAndFlush(tablet)

        // Get all the tabletList where model contains DEFAULT_MODEL
        defaultTabletShouldBeFound("model.contains=$DEFAULT_MODEL")

        // Get all the tabletList where model contains UPDATED_MODEL
        defaultTabletShouldNotBeFound("model.contains=$UPDATED_MODEL")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllTabletsByModelNotContainsSomething() {
        // Initialize the database
        tabletRepository.saveAndFlush(tablet)

        // Get all the tabletList where model does not contain DEFAULT_MODEL
        defaultTabletShouldNotBeFound("model.doesNotContain=$DEFAULT_MODEL")

        // Get all the tabletList where model does not contain UPDATED_MODEL
        defaultTabletShouldBeFound("model.doesNotContain=$UPDATED_MODEL")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllTabletsByTabletUserIsEqualToSomething() {
        // Initialize the database
        tabletRepository.saveAndFlush(tablet)
        var tabletUser: TabletUser
        if (findAll(em, TabletUser::class).isEmpty()) {
            tabletUser = TabletUserResourceIT.createEntity(em)
            em.persist(tabletUser)
            em.flush()
        } else {
            tabletUser = findAll(em, TabletUser::class)[0]
        }
        em.persist(tabletUser)
        em.flush()
        tablet.addTabletUser(tabletUser)
        tabletRepository.saveAndFlush(tablet)
        val tabletUserId = tabletUser?.id

        // Get all the tabletList where tabletUser equals to tabletUserId
        defaultTabletShouldBeFound("tabletUserId.equals=$tabletUserId")

        // Get all the tabletList where tabletUser equals to (tabletUserId?.plus(1))
        defaultTabletShouldNotBeFound("tabletUserId.equals=${(tabletUserId?.plus(1))}")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllTabletsByCenterIsEqualToSomething() {
        // Initialize the database
        tabletRepository.saveAndFlush(tablet)
        var center: Center
        if (findAll(em, Center::class).isEmpty()) {
            center = CenterResourceIT.createEntity(em)
            em.persist(center)
            em.flush()
        } else {
            center = findAll(em, Center::class)[0]
        }
        em.persist(center)
        em.flush()
        tablet.center = center
        tabletRepository.saveAndFlush(tablet)
        val centerId = center?.id

        // Get all the tabletList where center equals to centerId
        defaultTabletShouldBeFound("centerId.equals=$centerId")

        // Get all the tabletList where center equals to (centerId?.plus(1))
        defaultTabletShouldNotBeFound("centerId.equals=${(centerId?.plus(1))}")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllTabletsByDonorIsEqualToSomething() {
        // Initialize the database
        tabletRepository.saveAndFlush(tablet)
        var donor: Donor
        if (findAll(em, Donor::class).isEmpty()) {
            donor = DonorResourceIT.createEntity(em)
            em.persist(donor)
            em.flush()
        } else {
            donor = findAll(em, Donor::class)[0]
        }
        em.persist(donor)
        em.flush()
        tablet.donor = donor
        tabletRepository.saveAndFlush(tablet)
        val donorId = donor?.id

        // Get all the tabletList where donor equals to donorId
        defaultTabletShouldBeFound("donorId.equals=$donorId")

        // Get all the tabletList where donor equals to (donorId?.plus(1))
        defaultTabletShouldNotBeFound("donorId.equals=${(donorId?.plus(1))}")
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */

    @Throws(Exception::class)
    private fun defaultTabletShouldBeFound(filter: String) {
        restTabletMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tablet.id?.toInt())))
            .andExpect(jsonPath("$.[*].createTimeStamp").value(hasItem(DEFAULT_CREATE_TIME_STAMP.toString())))
            .andExpect(jsonPath("$.[*].updateTimeStamp").value(hasItem(DEFAULT_UPDATE_TIME_STAMP.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].identifier").value(hasItem(DEFAULT_IDENTIFIER)))
            .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL)))

        // Check, that the count call also returns 1
        restTabletMockMvc.perform(get(ENTITY_API_URL + "/count?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"))
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    @Throws(Exception::class)
    private fun defaultTabletShouldNotBeFound(filter: String) {
        restTabletMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$").isEmpty)

        // Check, that the count call also returns 0
        restTabletMockMvc.perform(get(ENTITY_API_URL + "/count?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"))
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
        val updatedTablet = tabletRepository.findById(tablet.id).orElseThrow()
        // Disconnect from session so that the updates on updatedTablet are not directly saved in db
        em.detach(updatedTablet)
        updatedTablet.createTimeStamp = UPDATED_CREATE_TIME_STAMP
        updatedTablet.updateTimeStamp = UPDATED_UPDATE_TIME_STAMP
        updatedTablet.name = UPDATED_NAME
        updatedTablet.identifier = UPDATED_IDENTIFIER
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
        assertThat(testTablet.createTimeStamp).isEqualTo(UPDATED_CREATE_TIME_STAMP)
        assertThat(testTablet.updateTimeStamp).isEqualTo(UPDATED_UPDATE_TIME_STAMP)
        assertThat(testTablet.name).isEqualTo(UPDATED_NAME)
        assertThat(testTablet.identifier).isEqualTo(UPDATED_IDENTIFIER)
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

            createTimeStamp = UPDATED_CREATE_TIME_STAMP
            updateTimeStamp = UPDATED_UPDATE_TIME_STAMP
            name = UPDATED_NAME
            identifier = UPDATED_IDENTIFIER
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
        assertThat(testTablet.createTimeStamp).isEqualTo(UPDATED_CREATE_TIME_STAMP)
        assertThat(testTablet.updateTimeStamp).isEqualTo(UPDATED_UPDATE_TIME_STAMP)
        assertThat(testTablet.name).isEqualTo(UPDATED_NAME)
        assertThat(testTablet.identifier).isEqualTo(UPDATED_IDENTIFIER)
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

            createTimeStamp = UPDATED_CREATE_TIME_STAMP
            updateTimeStamp = UPDATED_UPDATE_TIME_STAMP
            name = UPDATED_NAME
            identifier = UPDATED_IDENTIFIER
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
        assertThat(testTablet.createTimeStamp).isEqualTo(UPDATED_CREATE_TIME_STAMP)
        assertThat(testTablet.updateTimeStamp).isEqualTo(UPDATED_UPDATE_TIME_STAMP)
        assertThat(testTablet.name).isEqualTo(UPDATED_NAME)
        assertThat(testTablet.identifier).isEqualTo(UPDATED_IDENTIFIER)
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

        private val DEFAULT_CREATE_TIME_STAMP: Instant = Instant.ofEpochMilli(0L)
        private val UPDATED_CREATE_TIME_STAMP: Instant = Instant.now().truncatedTo(ChronoUnit.MILLIS)

        private val DEFAULT_UPDATE_TIME_STAMP: Instant = Instant.ofEpochMilli(0L)
        private val UPDATED_UPDATE_TIME_STAMP: Instant = Instant.now().truncatedTo(ChronoUnit.MILLIS)

        private const val DEFAULT_NAME = "AAAAAAAAAA"
        private const val UPDATED_NAME = "BBBBBBBBBB"

        private const val DEFAULT_IDENTIFIER = "AAAAAAAAAA"
        private const val UPDATED_IDENTIFIER = "BBBBBBBBBB"

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
                createTimeStamp = DEFAULT_CREATE_TIME_STAMP,

                updateTimeStamp = DEFAULT_UPDATE_TIME_STAMP,

                name = DEFAULT_NAME,

                identifier = DEFAULT_IDENTIFIER,

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
                createTimeStamp = UPDATED_CREATE_TIME_STAMP,

                updateTimeStamp = UPDATED_UPDATE_TIME_STAMP,

                name = UPDATED_NAME,

                identifier = UPDATED_IDENTIFIER,

                model = UPDATED_MODEL

            )

            return tablet
        }
    }
}
