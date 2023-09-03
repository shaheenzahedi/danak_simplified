package org.aydm.danak.web.rest

import org.assertj.core.api.Assertions.assertThat
import org.aydm.danak.IntegrationTest
import org.aydm.danak.domain.Center
import org.aydm.danak.domain.Tablet
import org.aydm.danak.repository.CenterRepository
import org.aydm.danak.service.mapper.CenterMapper
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
 * Integration tests for the [CenterResource] REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CenterResourceIT {
    @Autowired
    private lateinit var centerRepository: CenterRepository

    @Autowired
    private lateinit var centerMapper: CenterMapper

    @Autowired
    private lateinit var jacksonMessageConverter: MappingJackson2HttpMessageConverter

    @Autowired
    private lateinit var pageableArgumentResolver: PageableHandlerMethodArgumentResolver

    @Autowired
    private lateinit var validator: Validator

    @Autowired
    private lateinit var em: EntityManager

    @Autowired
    private lateinit var restCenterMockMvc: MockMvc

    private lateinit var center: Center

    @BeforeEach
    fun initTest() {
        center = createEntity(em)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createCenter() {
        val databaseSizeBeforeCreate = centerRepository.findAll().size
        // Create the Center
        val centerDTO = centerMapper.toDto(center)
        restCenterMockMvc.perform(
            post(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(centerDTO))
        ).andExpect(status().isCreated)

        // Validate the Center in the database
        val centerList = centerRepository.findAll()
        assertThat(centerList).hasSize(databaseSizeBeforeCreate + 1)
        val testCenter = centerList[centerList.size - 1]

        assertThat(testCenter.createTimeStamp).isEqualTo(DEFAULT_CREATE_TIME_STAMP)
        assertThat(testCenter.updateTimeStamp).isEqualTo(DEFAULT_UPDATE_TIME_STAMP)
        assertThat(testCenter.name).isEqualTo(DEFAULT_NAME)
        assertThat(testCenter.city).isEqualTo(DEFAULT_CITY)
        assertThat(testCenter.country).isEqualTo(DEFAULT_COUNTRY)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createCenterWithExistingId() {
        // Create the Center with an existing ID
        center.id = 1L
        val centerDTO = centerMapper.toDto(center)

        val databaseSizeBeforeCreate = centerRepository.findAll().size

        // An entity with an existing ID cannot be created, so this API call must fail
        restCenterMockMvc.perform(
            post(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(centerDTO))
        ).andExpect(status().isBadRequest)

        // Validate the Center in the database
        val centerList = centerRepository.findAll()
        assertThat(centerList).hasSize(databaseSizeBeforeCreate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllCenters() {
        // Initialize the database
        centerRepository.saveAndFlush(center)

        // Get all the centerList
        restCenterMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(center.id?.toInt())))
            .andExpect(jsonPath("$.[*].createTimeStamp").value(hasItem(DEFAULT_CREATE_TIME_STAMP.toString())))
            .andExpect(jsonPath("$.[*].updateTimeStamp").value(hasItem(DEFAULT_UPDATE_TIME_STAMP.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getCenter() {
        // Initialize the database
        centerRepository.saveAndFlush(center)

        val id = center.id
        assertNotNull(id)

        // Get the center
        restCenterMockMvc.perform(get(ENTITY_API_URL_ID, center.id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(center.id?.toInt()))
            .andExpect(jsonPath("$.createTimeStamp").value(DEFAULT_CREATE_TIME_STAMP.toString()))
            .andExpect(jsonPath("$.updateTimeStamp").value(DEFAULT_UPDATE_TIME_STAMP.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getCentersByIdFiltering() {
        // Initialize the database
        centerRepository.saveAndFlush(center)
        val id = center.id

        defaultCenterShouldBeFound("id.equals=$id")
        defaultCenterShouldNotBeFound("id.notEquals=$id")
        defaultCenterShouldBeFound("id.greaterThanOrEqual=$id")
        defaultCenterShouldNotBeFound("id.greaterThan=$id")

        defaultCenterShouldBeFound("id.lessThanOrEqual=$id")
        defaultCenterShouldNotBeFound("id.lessThan=$id")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllCentersByCreateTimeStampIsEqualToSomething() {
        // Initialize the database
        centerRepository.saveAndFlush(center)

        // Get all the centerList where createTimeStamp equals to DEFAULT_CREATE_TIME_STAMP
        defaultCenterShouldBeFound("createTimeStamp.equals=$DEFAULT_CREATE_TIME_STAMP")

        // Get all the centerList where createTimeStamp equals to UPDATED_CREATE_TIME_STAMP
        defaultCenterShouldNotBeFound("createTimeStamp.equals=$UPDATED_CREATE_TIME_STAMP")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllCentersByCreateTimeStampIsNotEqualToSomething() {
        // Initialize the database
        centerRepository.saveAndFlush(center)

        // Get all the centerList where createTimeStamp not equals to DEFAULT_CREATE_TIME_STAMP
        defaultCenterShouldNotBeFound("createTimeStamp.notEquals=$DEFAULT_CREATE_TIME_STAMP")

        // Get all the centerList where createTimeStamp not equals to UPDATED_CREATE_TIME_STAMP
        defaultCenterShouldBeFound("createTimeStamp.notEquals=$UPDATED_CREATE_TIME_STAMP")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllCentersByCreateTimeStampIsInShouldWork() {
        // Initialize the database
        centerRepository.saveAndFlush(center)

        // Get all the centerList where createTimeStamp in DEFAULT_CREATE_TIME_STAMP or UPDATED_CREATE_TIME_STAMP
        defaultCenterShouldBeFound("createTimeStamp.in=$DEFAULT_CREATE_TIME_STAMP,$UPDATED_CREATE_TIME_STAMP")

        // Get all the centerList where createTimeStamp equals to UPDATED_CREATE_TIME_STAMP
        defaultCenterShouldNotBeFound("createTimeStamp.in=$UPDATED_CREATE_TIME_STAMP")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllCentersByCreateTimeStampIsNullOrNotNull() {
        // Initialize the database
        centerRepository.saveAndFlush(center)

        // Get all the centerList where createTimeStamp is not null
        defaultCenterShouldBeFound("createTimeStamp.specified=true")

        // Get all the centerList where createTimeStamp is null
        defaultCenterShouldNotBeFound("createTimeStamp.specified=false")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllCentersByUpdateTimeStampIsEqualToSomething() {
        // Initialize the database
        centerRepository.saveAndFlush(center)

        // Get all the centerList where updateTimeStamp equals to DEFAULT_UPDATE_TIME_STAMP
        defaultCenterShouldBeFound("updateTimeStamp.equals=$DEFAULT_UPDATE_TIME_STAMP")

        // Get all the centerList where updateTimeStamp equals to UPDATED_UPDATE_TIME_STAMP
        defaultCenterShouldNotBeFound("updateTimeStamp.equals=$UPDATED_UPDATE_TIME_STAMP")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllCentersByUpdateTimeStampIsNotEqualToSomething() {
        // Initialize the database
        centerRepository.saveAndFlush(center)

        // Get all the centerList where updateTimeStamp not equals to DEFAULT_UPDATE_TIME_STAMP
        defaultCenterShouldNotBeFound("updateTimeStamp.notEquals=$DEFAULT_UPDATE_TIME_STAMP")

        // Get all the centerList where updateTimeStamp not equals to UPDATED_UPDATE_TIME_STAMP
        defaultCenterShouldBeFound("updateTimeStamp.notEquals=$UPDATED_UPDATE_TIME_STAMP")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllCentersByUpdateTimeStampIsInShouldWork() {
        // Initialize the database
        centerRepository.saveAndFlush(center)

        // Get all the centerList where updateTimeStamp in DEFAULT_UPDATE_TIME_STAMP or UPDATED_UPDATE_TIME_STAMP
        defaultCenterShouldBeFound("updateTimeStamp.in=$DEFAULT_UPDATE_TIME_STAMP,$UPDATED_UPDATE_TIME_STAMP")

        // Get all the centerList where updateTimeStamp equals to UPDATED_UPDATE_TIME_STAMP
        defaultCenterShouldNotBeFound("updateTimeStamp.in=$UPDATED_UPDATE_TIME_STAMP")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllCentersByUpdateTimeStampIsNullOrNotNull() {
        // Initialize the database
        centerRepository.saveAndFlush(center)

        // Get all the centerList where updateTimeStamp is not null
        defaultCenterShouldBeFound("updateTimeStamp.specified=true")

        // Get all the centerList where updateTimeStamp is null
        defaultCenterShouldNotBeFound("updateTimeStamp.specified=false")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllCentersByNameIsEqualToSomething() {
        // Initialize the database
        centerRepository.saveAndFlush(center)

        // Get all the centerList where name equals to DEFAULT_NAME
        defaultCenterShouldBeFound("name.equals=$DEFAULT_NAME")

        // Get all the centerList where name equals to UPDATED_NAME
        defaultCenterShouldNotBeFound("name.equals=$UPDATED_NAME")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllCentersByNameIsNotEqualToSomething() {
        // Initialize the database
        centerRepository.saveAndFlush(center)

        // Get all the centerList where name not equals to DEFAULT_NAME
        defaultCenterShouldNotBeFound("name.notEquals=$DEFAULT_NAME")

        // Get all the centerList where name not equals to UPDATED_NAME
        defaultCenterShouldBeFound("name.notEquals=$UPDATED_NAME")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllCentersByNameIsInShouldWork() {
        // Initialize the database
        centerRepository.saveAndFlush(center)

        // Get all the centerList where name in DEFAULT_NAME or UPDATED_NAME
        defaultCenterShouldBeFound("name.in=$DEFAULT_NAME,$UPDATED_NAME")

        // Get all the centerList where name equals to UPDATED_NAME
        defaultCenterShouldNotBeFound("name.in=$UPDATED_NAME")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllCentersByNameIsNullOrNotNull() {
        // Initialize the database
        centerRepository.saveAndFlush(center)

        // Get all the centerList where name is not null
        defaultCenterShouldBeFound("name.specified=true")

        // Get all the centerList where name is null
        defaultCenterShouldNotBeFound("name.specified=false")
    }
    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllCentersByNameContainsSomething() {
        // Initialize the database
        centerRepository.saveAndFlush(center)

        // Get all the centerList where name contains DEFAULT_NAME
        defaultCenterShouldBeFound("name.contains=$DEFAULT_NAME")

        // Get all the centerList where name contains UPDATED_NAME
        defaultCenterShouldNotBeFound("name.contains=$UPDATED_NAME")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllCentersByNameNotContainsSomething() {
        // Initialize the database
        centerRepository.saveAndFlush(center)

        // Get all the centerList where name does not contain DEFAULT_NAME
        defaultCenterShouldNotBeFound("name.doesNotContain=$DEFAULT_NAME")

        // Get all the centerList where name does not contain UPDATED_NAME
        defaultCenterShouldBeFound("name.doesNotContain=$UPDATED_NAME")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllCentersByCityIsEqualToSomething() {
        // Initialize the database
        centerRepository.saveAndFlush(center)

        // Get all the centerList where city equals to DEFAULT_CITY
        defaultCenterShouldBeFound("city.equals=$DEFAULT_CITY")

        // Get all the centerList where city equals to UPDATED_CITY
        defaultCenterShouldNotBeFound("city.equals=$UPDATED_CITY")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllCentersByCityIsNotEqualToSomething() {
        // Initialize the database
        centerRepository.saveAndFlush(center)

        // Get all the centerList where city not equals to DEFAULT_CITY
        defaultCenterShouldNotBeFound("city.notEquals=$DEFAULT_CITY")

        // Get all the centerList where city not equals to UPDATED_CITY
        defaultCenterShouldBeFound("city.notEquals=$UPDATED_CITY")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllCentersByCityIsInShouldWork() {
        // Initialize the database
        centerRepository.saveAndFlush(center)

        // Get all the centerList where city in DEFAULT_CITY or UPDATED_CITY
        defaultCenterShouldBeFound("city.in=$DEFAULT_CITY,$UPDATED_CITY")

        // Get all the centerList where city equals to UPDATED_CITY
        defaultCenterShouldNotBeFound("city.in=$UPDATED_CITY")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllCentersByCityIsNullOrNotNull() {
        // Initialize the database
        centerRepository.saveAndFlush(center)

        // Get all the centerList where city is not null
        defaultCenterShouldBeFound("city.specified=true")

        // Get all the centerList where city is null
        defaultCenterShouldNotBeFound("city.specified=false")
    }
    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllCentersByCityContainsSomething() {
        // Initialize the database
        centerRepository.saveAndFlush(center)

        // Get all the centerList where city contains DEFAULT_CITY
        defaultCenterShouldBeFound("city.contains=$DEFAULT_CITY")

        // Get all the centerList where city contains UPDATED_CITY
        defaultCenterShouldNotBeFound("city.contains=$UPDATED_CITY")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllCentersByCityNotContainsSomething() {
        // Initialize the database
        centerRepository.saveAndFlush(center)

        // Get all the centerList where city does not contain DEFAULT_CITY
        defaultCenterShouldNotBeFound("city.doesNotContain=$DEFAULT_CITY")

        // Get all the centerList where city does not contain UPDATED_CITY
        defaultCenterShouldBeFound("city.doesNotContain=$UPDATED_CITY")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllCentersByCountryIsEqualToSomething() {
        // Initialize the database
        centerRepository.saveAndFlush(center)

        // Get all the centerList where country equals to DEFAULT_COUNTRY
        defaultCenterShouldBeFound("country.equals=$DEFAULT_COUNTRY")

        // Get all the centerList where country equals to UPDATED_COUNTRY
        defaultCenterShouldNotBeFound("country.equals=$UPDATED_COUNTRY")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllCentersByCountryIsNotEqualToSomething() {
        // Initialize the database
        centerRepository.saveAndFlush(center)

        // Get all the centerList where country not equals to DEFAULT_COUNTRY
        defaultCenterShouldNotBeFound("country.notEquals=$DEFAULT_COUNTRY")

        // Get all the centerList where country not equals to UPDATED_COUNTRY
        defaultCenterShouldBeFound("country.notEquals=$UPDATED_COUNTRY")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllCentersByCountryIsInShouldWork() {
        // Initialize the database
        centerRepository.saveAndFlush(center)

        // Get all the centerList where country in DEFAULT_COUNTRY or UPDATED_COUNTRY
        defaultCenterShouldBeFound("country.in=$DEFAULT_COUNTRY,$UPDATED_COUNTRY")

        // Get all the centerList where country equals to UPDATED_COUNTRY
        defaultCenterShouldNotBeFound("country.in=$UPDATED_COUNTRY")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllCentersByCountryIsNullOrNotNull() {
        // Initialize the database
        centerRepository.saveAndFlush(center)

        // Get all the centerList where country is not null
        defaultCenterShouldBeFound("country.specified=true")

        // Get all the centerList where country is null
        defaultCenterShouldNotBeFound("country.specified=false")
    }
    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllCentersByCountryContainsSomething() {
        // Initialize the database
        centerRepository.saveAndFlush(center)

        // Get all the centerList where country contains DEFAULT_COUNTRY
        defaultCenterShouldBeFound("country.contains=$DEFAULT_COUNTRY")

        // Get all the centerList where country contains UPDATED_COUNTRY
        defaultCenterShouldNotBeFound("country.contains=$UPDATED_COUNTRY")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllCentersByCountryNotContainsSomething() {
        // Initialize the database
        centerRepository.saveAndFlush(center)

        // Get all the centerList where country does not contain DEFAULT_COUNTRY
        defaultCenterShouldNotBeFound("country.doesNotContain=$DEFAULT_COUNTRY")

        // Get all the centerList where country does not contain UPDATED_COUNTRY
        defaultCenterShouldBeFound("country.doesNotContain=$UPDATED_COUNTRY")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllCentersByTabletIsEqualToSomething() {
        // Initialize the database
        centerRepository.saveAndFlush(center)
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
        center.addTablet(tablet)
        centerRepository.saveAndFlush(center)
        val tabletId = tablet?.id

        // Get all the centerList where tablet equals to tabletId
        defaultCenterShouldBeFound("tabletId.equals=$tabletId")

        // Get all the centerList where tablet equals to (tabletId?.plus(1))
        defaultCenterShouldNotBeFound("tabletId.equals=${(tabletId?.plus(1))}")
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */

    @Throws(Exception::class)
    private fun defaultCenterShouldBeFound(filter: String) {
        restCenterMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(center.id?.toInt())))
            .andExpect(jsonPath("$.[*].createTimeStamp").value(hasItem(DEFAULT_CREATE_TIME_STAMP.toString())))
            .andExpect(jsonPath("$.[*].updateTimeStamp").value(hasItem(DEFAULT_UPDATE_TIME_STAMP.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))

        // Check, that the count call also returns 1
        restCenterMockMvc.perform(get(ENTITY_API_URL + "/count?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"))
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    @Throws(Exception::class)
    private fun defaultCenterShouldNotBeFound(filter: String) {
        restCenterMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$").isEmpty)

        // Check, that the count call also returns 0
        restCenterMockMvc.perform(get(ENTITY_API_URL + "/count?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"))
    }
    @Test
    @Transactional
    @Throws(Exception::class)
    fun getNonExistingCenter() {
        // Get the center
        restCenterMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }
    @Test
    @Transactional
    fun putNewCenter() {
        // Initialize the database
        centerRepository.saveAndFlush(center)

        val databaseSizeBeforeUpdate = centerRepository.findAll().size

        // Update the center
        val updatedCenter = centerRepository.findById(center.id).orElseThrow()
        // Disconnect from session so that the updates on updatedCenter are not directly saved in db
        em.detach(updatedCenter)
        updatedCenter.createTimeStamp = UPDATED_CREATE_TIME_STAMP
        updatedCenter.updateTimeStamp = UPDATED_UPDATE_TIME_STAMP
        updatedCenter.name = UPDATED_NAME
        updatedCenter.city = UPDATED_CITY
        updatedCenter.country = UPDATED_COUNTRY
        val centerDTO = centerMapper.toDto(updatedCenter)

        restCenterMockMvc.perform(
            put(ENTITY_API_URL_ID, centerDTO.id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(centerDTO))
        ).andExpect(status().isOk)

        // Validate the Center in the database
        val centerList = centerRepository.findAll()
        assertThat(centerList).hasSize(databaseSizeBeforeUpdate)
        val testCenter = centerList[centerList.size - 1]
        assertThat(testCenter.createTimeStamp).isEqualTo(UPDATED_CREATE_TIME_STAMP)
        assertThat(testCenter.updateTimeStamp).isEqualTo(UPDATED_UPDATE_TIME_STAMP)
        assertThat(testCenter.name).isEqualTo(UPDATED_NAME)
        assertThat(testCenter.city).isEqualTo(UPDATED_CITY)
        assertThat(testCenter.country).isEqualTo(UPDATED_COUNTRY)
    }

    @Test
    @Transactional
    fun putNonExistingCenter() {
        val databaseSizeBeforeUpdate = centerRepository.findAll().size
        center.id = count.incrementAndGet()

        // Create the Center
        val centerDTO = centerMapper.toDto(center)

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCenterMockMvc.perform(
            put(ENTITY_API_URL_ID, centerDTO.id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(centerDTO))
        )
            .andExpect(status().isBadRequest)

        // Validate the Center in the database
        val centerList = centerRepository.findAll()
        assertThat(centerList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun putWithIdMismatchCenter() {
        val databaseSizeBeforeUpdate = centerRepository.findAll().size
        center.id = count.incrementAndGet()

        // Create the Center
        val centerDTO = centerMapper.toDto(center)

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCenterMockMvc.perform(
            put(ENTITY_API_URL_ID, count.incrementAndGet())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(centerDTO))
        ).andExpect(status().isBadRequest)

        // Validate the Center in the database
        val centerList = centerRepository.findAll()
        assertThat(centerList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun putWithMissingIdPathParamCenter() {
        val databaseSizeBeforeUpdate = centerRepository.findAll().size
        center.id = count.incrementAndGet()

        // Create the Center
        val centerDTO = centerMapper.toDto(center)

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCenterMockMvc.perform(
            put(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(centerDTO))
        )
            .andExpect(status().isMethodNotAllowed)

        // Validate the Center in the database
        val centerList = centerRepository.findAll()
        assertThat(centerList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun partialUpdateCenterWithPatch() {
        centerRepository.saveAndFlush(center)

        val databaseSizeBeforeUpdate = centerRepository.findAll().size

// Update the center using partial update
        val partialUpdatedCenter = Center().apply {
            id = center.id

            createTimeStamp = UPDATED_CREATE_TIME_STAMP
            country = UPDATED_COUNTRY
        }

        restCenterMockMvc.perform(
            patch(ENTITY_API_URL_ID, partialUpdatedCenter.id)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(partialUpdatedCenter))
        )
            .andExpect(status().isOk)

// Validate the Center in the database
        val centerList = centerRepository.findAll()
        assertThat(centerList).hasSize(databaseSizeBeforeUpdate)
        val testCenter = centerList.last()
        assertThat(testCenter.createTimeStamp).isEqualTo(UPDATED_CREATE_TIME_STAMP)
        assertThat(testCenter.updateTimeStamp).isEqualTo(DEFAULT_UPDATE_TIME_STAMP)
        assertThat(testCenter.name).isEqualTo(DEFAULT_NAME)
        assertThat(testCenter.city).isEqualTo(DEFAULT_CITY)
        assertThat(testCenter.country).isEqualTo(UPDATED_COUNTRY)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun fullUpdateCenterWithPatch() {
        centerRepository.saveAndFlush(center)

        val databaseSizeBeforeUpdate = centerRepository.findAll().size

// Update the center using partial update
        val partialUpdatedCenter = Center().apply {
            id = center.id

            createTimeStamp = UPDATED_CREATE_TIME_STAMP
            updateTimeStamp = UPDATED_UPDATE_TIME_STAMP
            name = UPDATED_NAME
            city = UPDATED_CITY
            country = UPDATED_COUNTRY
        }

        restCenterMockMvc.perform(
            patch(ENTITY_API_URL_ID, partialUpdatedCenter.id)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(partialUpdatedCenter))
        )
            .andExpect(status().isOk)

// Validate the Center in the database
        val centerList = centerRepository.findAll()
        assertThat(centerList).hasSize(databaseSizeBeforeUpdate)
        val testCenter = centerList.last()
        assertThat(testCenter.createTimeStamp).isEqualTo(UPDATED_CREATE_TIME_STAMP)
        assertThat(testCenter.updateTimeStamp).isEqualTo(UPDATED_UPDATE_TIME_STAMP)
        assertThat(testCenter.name).isEqualTo(UPDATED_NAME)
        assertThat(testCenter.city).isEqualTo(UPDATED_CITY)
        assertThat(testCenter.country).isEqualTo(UPDATED_COUNTRY)
    }

    @Throws(Exception::class)
    fun patchNonExistingCenter() {
        val databaseSizeBeforeUpdate = centerRepository.findAll().size
        center.id = count.incrementAndGet()

        // Create the Center
        val centerDTO = centerMapper.toDto(center)

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCenterMockMvc.perform(
            patch(ENTITY_API_URL_ID, centerDTO.id)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(centerDTO))
        )
            .andExpect(status().isBadRequest)

        // Validate the Center in the database
        val centerList = centerRepository.findAll()
        assertThat(centerList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun patchWithIdMismatchCenter() {
        val databaseSizeBeforeUpdate = centerRepository.findAll().size
        center.id = count.incrementAndGet()

        // Create the Center
        val centerDTO = centerMapper.toDto(center)

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCenterMockMvc.perform(
            patch(ENTITY_API_URL_ID, count.incrementAndGet())
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(centerDTO))
        )
            .andExpect(status().isBadRequest)

        // Validate the Center in the database
        val centerList = centerRepository.findAll()
        assertThat(centerList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun patchWithMissingIdPathParamCenter() {
        val databaseSizeBeforeUpdate = centerRepository.findAll().size
        center.id = count.incrementAndGet()

        // Create the Center
        val centerDTO = centerMapper.toDto(center)

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCenterMockMvc.perform(
            patch(ENTITY_API_URL)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(centerDTO))
        )
            .andExpect(status().isMethodNotAllowed)

        // Validate the Center in the database
        val centerList = centerRepository.findAll()
        assertThat(centerList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun deleteCenter() {
        // Initialize the database
        centerRepository.saveAndFlush(center)

        val databaseSizeBeforeDelete = centerRepository.findAll().size

        // Delete the center
        restCenterMockMvc.perform(
            delete(ENTITY_API_URL_ID, center.id)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val centerList = centerRepository.findAll()
        assertThat(centerList).hasSize(databaseSizeBeforeDelete - 1)
    }

    companion object {

        private val DEFAULT_CREATE_TIME_STAMP: Instant = Instant.ofEpochMilli(0L)
        private val UPDATED_CREATE_TIME_STAMP: Instant = Instant.now().truncatedTo(ChronoUnit.MILLIS)

        private val DEFAULT_UPDATE_TIME_STAMP: Instant = Instant.ofEpochMilli(0L)
        private val UPDATED_UPDATE_TIME_STAMP: Instant = Instant.now().truncatedTo(ChronoUnit.MILLIS)

        private const val DEFAULT_NAME = "AAAAAAAAAA"
        private const val UPDATED_NAME = "BBBBBBBBBB"

        private const val DEFAULT_CITY = "AAAAAAAAAA"
        private const val UPDATED_CITY = "BBBBBBBBBB"

        private const val DEFAULT_COUNTRY = "AAAAAAAAAA"
        private const val UPDATED_COUNTRY = "BBBBBBBBBB"

        private val ENTITY_API_URL: String = "/api/centers"
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
        fun createEntity(em: EntityManager): Center {
            val center = Center(
                createTimeStamp = DEFAULT_CREATE_TIME_STAMP,

                updateTimeStamp = DEFAULT_UPDATE_TIME_STAMP,

                name = DEFAULT_NAME,

                city = DEFAULT_CITY,

                country = DEFAULT_COUNTRY

            )

            return center
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(em: EntityManager): Center {
            val center = Center(
                createTimeStamp = UPDATED_CREATE_TIME_STAMP,

                updateTimeStamp = UPDATED_UPDATE_TIME_STAMP,

                name = UPDATED_NAME,

                city = UPDATED_CITY,

                country = UPDATED_COUNTRY

            )

            return center
        }
    }
}
