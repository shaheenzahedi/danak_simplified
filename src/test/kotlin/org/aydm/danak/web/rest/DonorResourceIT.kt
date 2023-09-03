package org.aydm.danak.web.rest

import org.assertj.core.api.Assertions.assertThat
import org.aydm.danak.IntegrationTest
import org.aydm.danak.domain.Donor
import org.aydm.danak.domain.Tablet
import org.aydm.danak.domain.User
import org.aydm.danak.repository.DonorRepository
import org.aydm.danak.service.mapper.DonorMapper
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
 * Integration tests for the [DonorResource] REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DonorResourceIT {
    @Autowired
    private lateinit var donorRepository: DonorRepository

    @Autowired
    private lateinit var donorMapper: DonorMapper

    @Autowired
    private lateinit var jacksonMessageConverter: MappingJackson2HttpMessageConverter

    @Autowired
    private lateinit var pageableArgumentResolver: PageableHandlerMethodArgumentResolver

    @Autowired
    private lateinit var validator: Validator

    @Autowired
    private lateinit var em: EntityManager

    @Autowired
    private lateinit var restDonorMockMvc: MockMvc

    private lateinit var donor: Donor

    @BeforeEach
    fun initTest() {
        donor = createEntity(em)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createDonor() {
        val databaseSizeBeforeCreate = donorRepository.findAll().size
        // Create the Donor
        val donorDTO = donorMapper.toDto(donor)
        restDonorMockMvc.perform(
            post(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(donorDTO))
        ).andExpect(status().isCreated)

        // Validate the Donor in the database
        val donorList = donorRepository.findAll()
        assertThat(donorList).hasSize(databaseSizeBeforeCreate + 1)
        val testDonor = donorList[donorList.size - 1]

        assertThat(testDonor.createTimeStamp).isEqualTo(DEFAULT_CREATE_TIME_STAMP)
        assertThat(testDonor.updateTimeStamp).isEqualTo(DEFAULT_UPDATE_TIME_STAMP)
        assertThat(testDonor.name).isEqualTo(DEFAULT_NAME)
        assertThat(testDonor.city).isEqualTo(DEFAULT_CITY)
        assertThat(testDonor.country).isEqualTo(DEFAULT_COUNTRY)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createDonorWithExistingId() {
        // Create the Donor with an existing ID
        donor.id = 1L
        val donorDTO = donorMapper.toDto(donor)

        val databaseSizeBeforeCreate = donorRepository.findAll().size

        // An entity with an existing ID cannot be created, so this API call must fail
        restDonorMockMvc.perform(
            post(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(donorDTO))
        ).andExpect(status().isBadRequest)

        // Validate the Donor in the database
        val donorList = donorRepository.findAll()
        assertThat(donorList).hasSize(databaseSizeBeforeCreate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllDonors() {
        // Initialize the database
        donorRepository.saveAndFlush(donor)

        // Get all the donorList
        restDonorMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(donor.id?.toInt())))
            .andExpect(jsonPath("$.[*].createTimeStamp").value(hasItem(DEFAULT_CREATE_TIME_STAMP.toString())))
            .andExpect(jsonPath("$.[*].updateTimeStamp").value(hasItem(DEFAULT_UPDATE_TIME_STAMP.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getDonor() {
        // Initialize the database
        donorRepository.saveAndFlush(donor)

        val id = donor.id
        assertNotNull(id)

        // Get the donor
        restDonorMockMvc.perform(get(ENTITY_API_URL_ID, donor.id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(donor.id?.toInt()))
            .andExpect(jsonPath("$.createTimeStamp").value(DEFAULT_CREATE_TIME_STAMP.toString()))
            .andExpect(jsonPath("$.updateTimeStamp").value(DEFAULT_UPDATE_TIME_STAMP.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getDonorsByIdFiltering() {
        // Initialize the database
        donorRepository.saveAndFlush(donor)
        val id = donor.id

        defaultDonorShouldBeFound("id.equals=$id")
        defaultDonorShouldNotBeFound("id.notEquals=$id")
        defaultDonorShouldBeFound("id.greaterThanOrEqual=$id")
        defaultDonorShouldNotBeFound("id.greaterThan=$id")

        defaultDonorShouldBeFound("id.lessThanOrEqual=$id")
        defaultDonorShouldNotBeFound("id.lessThan=$id")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllDonorsByCreateTimeStampIsEqualToSomething() {
        // Initialize the database
        donorRepository.saveAndFlush(donor)

        // Get all the donorList where createTimeStamp equals to DEFAULT_CREATE_TIME_STAMP
        defaultDonorShouldBeFound("createTimeStamp.equals=$DEFAULT_CREATE_TIME_STAMP")

        // Get all the donorList where createTimeStamp equals to UPDATED_CREATE_TIME_STAMP
        defaultDonorShouldNotBeFound("createTimeStamp.equals=$UPDATED_CREATE_TIME_STAMP")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllDonorsByCreateTimeStampIsNotEqualToSomething() {
        // Initialize the database
        donorRepository.saveAndFlush(donor)

        // Get all the donorList where createTimeStamp not equals to DEFAULT_CREATE_TIME_STAMP
        defaultDonorShouldNotBeFound("createTimeStamp.notEquals=$DEFAULT_CREATE_TIME_STAMP")

        // Get all the donorList where createTimeStamp not equals to UPDATED_CREATE_TIME_STAMP
        defaultDonorShouldBeFound("createTimeStamp.notEquals=$UPDATED_CREATE_TIME_STAMP")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllDonorsByCreateTimeStampIsInShouldWork() {
        // Initialize the database
        donorRepository.saveAndFlush(donor)

        // Get all the donorList where createTimeStamp in DEFAULT_CREATE_TIME_STAMP or UPDATED_CREATE_TIME_STAMP
        defaultDonorShouldBeFound("createTimeStamp.in=$DEFAULT_CREATE_TIME_STAMP,$UPDATED_CREATE_TIME_STAMP")

        // Get all the donorList where createTimeStamp equals to UPDATED_CREATE_TIME_STAMP
        defaultDonorShouldNotBeFound("createTimeStamp.in=$UPDATED_CREATE_TIME_STAMP")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllDonorsByCreateTimeStampIsNullOrNotNull() {
        // Initialize the database
        donorRepository.saveAndFlush(donor)

        // Get all the donorList where createTimeStamp is not null
        defaultDonorShouldBeFound("createTimeStamp.specified=true")

        // Get all the donorList where createTimeStamp is null
        defaultDonorShouldNotBeFound("createTimeStamp.specified=false")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllDonorsByUpdateTimeStampIsEqualToSomething() {
        // Initialize the database
        donorRepository.saveAndFlush(donor)

        // Get all the donorList where updateTimeStamp equals to DEFAULT_UPDATE_TIME_STAMP
        defaultDonorShouldBeFound("updateTimeStamp.equals=$DEFAULT_UPDATE_TIME_STAMP")

        // Get all the donorList where updateTimeStamp equals to UPDATED_UPDATE_TIME_STAMP
        defaultDonorShouldNotBeFound("updateTimeStamp.equals=$UPDATED_UPDATE_TIME_STAMP")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllDonorsByUpdateTimeStampIsNotEqualToSomething() {
        // Initialize the database
        donorRepository.saveAndFlush(donor)

        // Get all the donorList where updateTimeStamp not equals to DEFAULT_UPDATE_TIME_STAMP
        defaultDonorShouldNotBeFound("updateTimeStamp.notEquals=$DEFAULT_UPDATE_TIME_STAMP")

        // Get all the donorList where updateTimeStamp not equals to UPDATED_UPDATE_TIME_STAMP
        defaultDonorShouldBeFound("updateTimeStamp.notEquals=$UPDATED_UPDATE_TIME_STAMP")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllDonorsByUpdateTimeStampIsInShouldWork() {
        // Initialize the database
        donorRepository.saveAndFlush(donor)

        // Get all the donorList where updateTimeStamp in DEFAULT_UPDATE_TIME_STAMP or UPDATED_UPDATE_TIME_STAMP
        defaultDonorShouldBeFound("updateTimeStamp.in=$DEFAULT_UPDATE_TIME_STAMP,$UPDATED_UPDATE_TIME_STAMP")

        // Get all the donorList where updateTimeStamp equals to UPDATED_UPDATE_TIME_STAMP
        defaultDonorShouldNotBeFound("updateTimeStamp.in=$UPDATED_UPDATE_TIME_STAMP")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllDonorsByUpdateTimeStampIsNullOrNotNull() {
        // Initialize the database
        donorRepository.saveAndFlush(donor)

        // Get all the donorList where updateTimeStamp is not null
        defaultDonorShouldBeFound("updateTimeStamp.specified=true")

        // Get all the donorList where updateTimeStamp is null
        defaultDonorShouldNotBeFound("updateTimeStamp.specified=false")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllDonorsByNameIsEqualToSomething() {
        // Initialize the database
        donorRepository.saveAndFlush(donor)

        // Get all the donorList where name equals to DEFAULT_NAME
        defaultDonorShouldBeFound("name.equals=$DEFAULT_NAME")

        // Get all the donorList where name equals to UPDATED_NAME
        defaultDonorShouldNotBeFound("name.equals=$UPDATED_NAME")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllDonorsByNameIsNotEqualToSomething() {
        // Initialize the database
        donorRepository.saveAndFlush(donor)

        // Get all the donorList where name not equals to DEFAULT_NAME
        defaultDonorShouldNotBeFound("name.notEquals=$DEFAULT_NAME")

        // Get all the donorList where name not equals to UPDATED_NAME
        defaultDonorShouldBeFound("name.notEquals=$UPDATED_NAME")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllDonorsByNameIsInShouldWork() {
        // Initialize the database
        donorRepository.saveAndFlush(donor)

        // Get all the donorList where name in DEFAULT_NAME or UPDATED_NAME
        defaultDonorShouldBeFound("name.in=$DEFAULT_NAME,$UPDATED_NAME")

        // Get all the donorList where name equals to UPDATED_NAME
        defaultDonorShouldNotBeFound("name.in=$UPDATED_NAME")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllDonorsByNameIsNullOrNotNull() {
        // Initialize the database
        donorRepository.saveAndFlush(donor)

        // Get all the donorList where name is not null
        defaultDonorShouldBeFound("name.specified=true")

        // Get all the donorList where name is null
        defaultDonorShouldNotBeFound("name.specified=false")
    }
    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllDonorsByNameContainsSomething() {
        // Initialize the database
        donorRepository.saveAndFlush(donor)

        // Get all the donorList where name contains DEFAULT_NAME
        defaultDonorShouldBeFound("name.contains=$DEFAULT_NAME")

        // Get all the donorList where name contains UPDATED_NAME
        defaultDonorShouldNotBeFound("name.contains=$UPDATED_NAME")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllDonorsByNameNotContainsSomething() {
        // Initialize the database
        donorRepository.saveAndFlush(donor)

        // Get all the donorList where name does not contain DEFAULT_NAME
        defaultDonorShouldNotBeFound("name.doesNotContain=$DEFAULT_NAME")

        // Get all the donorList where name does not contain UPDATED_NAME
        defaultDonorShouldBeFound("name.doesNotContain=$UPDATED_NAME")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllDonorsByCityIsEqualToSomething() {
        // Initialize the database
        donorRepository.saveAndFlush(donor)

        // Get all the donorList where city equals to DEFAULT_CITY
        defaultDonorShouldBeFound("city.equals=$DEFAULT_CITY")

        // Get all the donorList where city equals to UPDATED_CITY
        defaultDonorShouldNotBeFound("city.equals=$UPDATED_CITY")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllDonorsByCityIsNotEqualToSomething() {
        // Initialize the database
        donorRepository.saveAndFlush(donor)

        // Get all the donorList where city not equals to DEFAULT_CITY
        defaultDonorShouldNotBeFound("city.notEquals=$DEFAULT_CITY")

        // Get all the donorList where city not equals to UPDATED_CITY
        defaultDonorShouldBeFound("city.notEquals=$UPDATED_CITY")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllDonorsByCityIsInShouldWork() {
        // Initialize the database
        donorRepository.saveAndFlush(donor)

        // Get all the donorList where city in DEFAULT_CITY or UPDATED_CITY
        defaultDonorShouldBeFound("city.in=$DEFAULT_CITY,$UPDATED_CITY")

        // Get all the donorList where city equals to UPDATED_CITY
        defaultDonorShouldNotBeFound("city.in=$UPDATED_CITY")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllDonorsByCityIsNullOrNotNull() {
        // Initialize the database
        donorRepository.saveAndFlush(donor)

        // Get all the donorList where city is not null
        defaultDonorShouldBeFound("city.specified=true")

        // Get all the donorList where city is null
        defaultDonorShouldNotBeFound("city.specified=false")
    }
    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllDonorsByCityContainsSomething() {
        // Initialize the database
        donorRepository.saveAndFlush(donor)

        // Get all the donorList where city contains DEFAULT_CITY
        defaultDonorShouldBeFound("city.contains=$DEFAULT_CITY")

        // Get all the donorList where city contains UPDATED_CITY
        defaultDonorShouldNotBeFound("city.contains=$UPDATED_CITY")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllDonorsByCityNotContainsSomething() {
        // Initialize the database
        donorRepository.saveAndFlush(donor)

        // Get all the donorList where city does not contain DEFAULT_CITY
        defaultDonorShouldNotBeFound("city.doesNotContain=$DEFAULT_CITY")

        // Get all the donorList where city does not contain UPDATED_CITY
        defaultDonorShouldBeFound("city.doesNotContain=$UPDATED_CITY")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllDonorsByCountryIsEqualToSomething() {
        // Initialize the database
        donorRepository.saveAndFlush(donor)

        // Get all the donorList where country equals to DEFAULT_COUNTRY
        defaultDonorShouldBeFound("country.equals=$DEFAULT_COUNTRY")

        // Get all the donorList where country equals to UPDATED_COUNTRY
        defaultDonorShouldNotBeFound("country.equals=$UPDATED_COUNTRY")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllDonorsByCountryIsNotEqualToSomething() {
        // Initialize the database
        donorRepository.saveAndFlush(donor)

        // Get all the donorList where country not equals to DEFAULT_COUNTRY
        defaultDonorShouldNotBeFound("country.notEquals=$DEFAULT_COUNTRY")

        // Get all the donorList where country not equals to UPDATED_COUNTRY
        defaultDonorShouldBeFound("country.notEquals=$UPDATED_COUNTRY")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllDonorsByCountryIsInShouldWork() {
        // Initialize the database
        donorRepository.saveAndFlush(donor)

        // Get all the donorList where country in DEFAULT_COUNTRY or UPDATED_COUNTRY
        defaultDonorShouldBeFound("country.in=$DEFAULT_COUNTRY,$UPDATED_COUNTRY")

        // Get all the donorList where country equals to UPDATED_COUNTRY
        defaultDonorShouldNotBeFound("country.in=$UPDATED_COUNTRY")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllDonorsByCountryIsNullOrNotNull() {
        // Initialize the database
        donorRepository.saveAndFlush(donor)

        // Get all the donorList where country is not null
        defaultDonorShouldBeFound("country.specified=true")

        // Get all the donorList where country is null
        defaultDonorShouldNotBeFound("country.specified=false")
    }
    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllDonorsByCountryContainsSomething() {
        // Initialize the database
        donorRepository.saveAndFlush(donor)

        // Get all the donorList where country contains DEFAULT_COUNTRY
        defaultDonorShouldBeFound("country.contains=$DEFAULT_COUNTRY")

        // Get all the donorList where country contains UPDATED_COUNTRY
        defaultDonorShouldNotBeFound("country.contains=$UPDATED_COUNTRY")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllDonorsByCountryNotContainsSomething() {
        // Initialize the database
        donorRepository.saveAndFlush(donor)

        // Get all the donorList where country does not contain DEFAULT_COUNTRY
        defaultDonorShouldNotBeFound("country.doesNotContain=$DEFAULT_COUNTRY")

        // Get all the donorList where country does not contain UPDATED_COUNTRY
        defaultDonorShouldBeFound("country.doesNotContain=$UPDATED_COUNTRY")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllDonorsByUserIsEqualToSomething() {
        // Initialize the database
        donorRepository.saveAndFlush(donor)
        var user: User
        if (findAll(em, User::class).isEmpty()) {
            user = UserResourceIT.createEntity(em)
            em.persist(user)
            em.flush()
        } else {
            user = findAll(em, User::class)[0]
        }
        em.persist(user)
        em.flush()
        donor.user = user
        donorRepository.saveAndFlush(donor)
        val userId = user?.id

        // Get all the donorList where user equals to userId
        defaultDonorShouldBeFound("userId.equals=$userId")

        // Get all the donorList where user equals to (userId?.plus(1))
        defaultDonorShouldNotBeFound("userId.equals=${(userId?.plus(1))}")
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllDonorsByTabletIsEqualToSomething() {
        // Initialize the database
        donorRepository.saveAndFlush(donor)
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
        donor.addTablet(tablet)
        donorRepository.saveAndFlush(donor)
        val tabletId = tablet?.id

        // Get all the donorList where tablet equals to tabletId
        defaultDonorShouldBeFound("tabletId.equals=$tabletId")

        // Get all the donorList where tablet equals to (tabletId?.plus(1))
        defaultDonorShouldNotBeFound("tabletId.equals=${(tabletId?.plus(1))}")
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */

    @Throws(Exception::class)
    private fun defaultDonorShouldBeFound(filter: String) {
        restDonorMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(donor.id?.toInt())))
            .andExpect(jsonPath("$.[*].createTimeStamp").value(hasItem(DEFAULT_CREATE_TIME_STAMP.toString())))
            .andExpect(jsonPath("$.[*].updateTimeStamp").value(hasItem(DEFAULT_UPDATE_TIME_STAMP.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))

        // Check, that the count call also returns 1
        restDonorMockMvc.perform(get(ENTITY_API_URL + "/count?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"))
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    @Throws(Exception::class)
    private fun defaultDonorShouldNotBeFound(filter: String) {
        restDonorMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$").isEmpty)

        // Check, that the count call also returns 0
        restDonorMockMvc.perform(get(ENTITY_API_URL + "/count?sort=id,desc&$filter"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"))
    }
    @Test
    @Transactional
    @Throws(Exception::class)
    fun getNonExistingDonor() {
        // Get the donor
        restDonorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }
    @Test
    @Transactional
    fun putNewDonor() {
        // Initialize the database
        donorRepository.saveAndFlush(donor)

        val databaseSizeBeforeUpdate = donorRepository.findAll().size

        // Update the donor
        val updatedDonor = donorRepository.findById(donor.id).get()
        // Disconnect from session so that the updates on updatedDonor are not directly saved in db
        em.detach(updatedDonor)
        updatedDonor.createTimeStamp = UPDATED_CREATE_TIME_STAMP
        updatedDonor.updateTimeStamp = UPDATED_UPDATE_TIME_STAMP
        updatedDonor.name = UPDATED_NAME
        updatedDonor.city = UPDATED_CITY
        updatedDonor.country = UPDATED_COUNTRY
        val donorDTO = donorMapper.toDto(updatedDonor)

        restDonorMockMvc.perform(
            put(ENTITY_API_URL_ID, donorDTO.id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(donorDTO))
        ).andExpect(status().isOk)

        // Validate the Donor in the database
        val donorList = donorRepository.findAll()
        assertThat(donorList).hasSize(databaseSizeBeforeUpdate)
        val testDonor = donorList[donorList.size - 1]
        assertThat(testDonor.createTimeStamp).isEqualTo(UPDATED_CREATE_TIME_STAMP)
        assertThat(testDonor.updateTimeStamp).isEqualTo(UPDATED_UPDATE_TIME_STAMP)
        assertThat(testDonor.name).isEqualTo(UPDATED_NAME)
        assertThat(testDonor.city).isEqualTo(UPDATED_CITY)
        assertThat(testDonor.country).isEqualTo(UPDATED_COUNTRY)
    }

    @Test
    @Transactional
    fun putNonExistingDonor() {
        val databaseSizeBeforeUpdate = donorRepository.findAll().size
        donor.id = count.incrementAndGet()

        // Create the Donor
        val donorDTO = donorMapper.toDto(donor)

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDonorMockMvc.perform(
            put(ENTITY_API_URL_ID, donorDTO.id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(donorDTO))
        )
            .andExpect(status().isBadRequest)

        // Validate the Donor in the database
        val donorList = donorRepository.findAll()
        assertThat(donorList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun putWithIdMismatchDonor() {
        val databaseSizeBeforeUpdate = donorRepository.findAll().size
        donor.id = count.incrementAndGet()

        // Create the Donor
        val donorDTO = donorMapper.toDto(donor)

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDonorMockMvc.perform(
            put(ENTITY_API_URL_ID, count.incrementAndGet())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(donorDTO))
        ).andExpect(status().isBadRequest)

        // Validate the Donor in the database
        val donorList = donorRepository.findAll()
        assertThat(donorList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun putWithMissingIdPathParamDonor() {
        val databaseSizeBeforeUpdate = donorRepository.findAll().size
        donor.id = count.incrementAndGet()

        // Create the Donor
        val donorDTO = donorMapper.toDto(donor)

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDonorMockMvc.perform(
            put(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(donorDTO))
        )
            .andExpect(status().isMethodNotAllowed)

        // Validate the Donor in the database
        val donorList = donorRepository.findAll()
        assertThat(donorList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun partialUpdateDonorWithPatch() {
        donorRepository.saveAndFlush(donor)

        val databaseSizeBeforeUpdate = donorRepository.findAll().size

// Update the donor using partial update
        val partialUpdatedDonor = Donor().apply {
            id = donor.id

            createTimeStamp = UPDATED_CREATE_TIME_STAMP
        }

        restDonorMockMvc.perform(
            patch(ENTITY_API_URL_ID, partialUpdatedDonor.id)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(partialUpdatedDonor))
        )
            .andExpect(status().isOk)

// Validate the Donor in the database
        val donorList = donorRepository.findAll()
        assertThat(donorList).hasSize(databaseSizeBeforeUpdate)
        val testDonor = donorList.last()
        assertThat(testDonor.createTimeStamp).isEqualTo(UPDATED_CREATE_TIME_STAMP)
        assertThat(testDonor.updateTimeStamp).isEqualTo(DEFAULT_UPDATE_TIME_STAMP)
        assertThat(testDonor.name).isEqualTo(DEFAULT_NAME)
        assertThat(testDonor.city).isEqualTo(DEFAULT_CITY)
        assertThat(testDonor.country).isEqualTo(DEFAULT_COUNTRY)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun fullUpdateDonorWithPatch() {
        donorRepository.saveAndFlush(donor)

        val databaseSizeBeforeUpdate = donorRepository.findAll().size

// Update the donor using partial update
        val partialUpdatedDonor = Donor().apply {
            id = donor.id

            createTimeStamp = UPDATED_CREATE_TIME_STAMP
            updateTimeStamp = UPDATED_UPDATE_TIME_STAMP
            name = UPDATED_NAME
            city = UPDATED_CITY
            country = UPDATED_COUNTRY
        }

        restDonorMockMvc.perform(
            patch(ENTITY_API_URL_ID, partialUpdatedDonor.id)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(partialUpdatedDonor))
        )
            .andExpect(status().isOk)

// Validate the Donor in the database
        val donorList = donorRepository.findAll()
        assertThat(donorList).hasSize(databaseSizeBeforeUpdate)
        val testDonor = donorList.last()
        assertThat(testDonor.createTimeStamp).isEqualTo(UPDATED_CREATE_TIME_STAMP)
        assertThat(testDonor.updateTimeStamp).isEqualTo(UPDATED_UPDATE_TIME_STAMP)
        assertThat(testDonor.name).isEqualTo(UPDATED_NAME)
        assertThat(testDonor.city).isEqualTo(UPDATED_CITY)
        assertThat(testDonor.country).isEqualTo(UPDATED_COUNTRY)
    }

    @Throws(Exception::class)
    fun patchNonExistingDonor() {
        val databaseSizeBeforeUpdate = donorRepository.findAll().size
        donor.id = count.incrementAndGet()

        // Create the Donor
        val donorDTO = donorMapper.toDto(donor)

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDonorMockMvc.perform(
            patch(ENTITY_API_URL_ID, donorDTO.id)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(donorDTO))
        )
            .andExpect(status().isBadRequest)

        // Validate the Donor in the database
        val donorList = donorRepository.findAll()
        assertThat(donorList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun patchWithIdMismatchDonor() {
        val databaseSizeBeforeUpdate = donorRepository.findAll().size
        donor.id = count.incrementAndGet()

        // Create the Donor
        val donorDTO = donorMapper.toDto(donor)

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDonorMockMvc.perform(
            patch(ENTITY_API_URL_ID, count.incrementAndGet())
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(donorDTO))
        )
            .andExpect(status().isBadRequest)

        // Validate the Donor in the database
        val donorList = donorRepository.findAll()
        assertThat(donorList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun patchWithMissingIdPathParamDonor() {
        val databaseSizeBeforeUpdate = donorRepository.findAll().size
        donor.id = count.incrementAndGet()

        // Create the Donor
        val donorDTO = donorMapper.toDto(donor)

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDonorMockMvc.perform(
            patch(ENTITY_API_URL)
                .contentType("application/merge-patch+json")
                .content(convertObjectToJsonBytes(donorDTO))
        )
            .andExpect(status().isMethodNotAllowed)

        // Validate the Donor in the database
        val donorList = donorRepository.findAll()
        assertThat(donorList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun deleteDonor() {
        // Initialize the database
        donorRepository.saveAndFlush(donor)

        val databaseSizeBeforeDelete = donorRepository.findAll().size

        // Delete the donor
        restDonorMockMvc.perform(
            delete(ENTITY_API_URL_ID, donor.id)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val donorList = donorRepository.findAll()
        assertThat(donorList).hasSize(databaseSizeBeforeDelete - 1)
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

        private val ENTITY_API_URL: String = "/api/donors"
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
        fun createEntity(em: EntityManager): Donor {
            val donor = Donor(
                createTimeStamp = DEFAULT_CREATE_TIME_STAMP,

                updateTimeStamp = DEFAULT_UPDATE_TIME_STAMP,

                name = DEFAULT_NAME,

                city = DEFAULT_CITY,

                country = DEFAULT_COUNTRY

            )

            return donor
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(em: EntityManager): Donor {
            val donor = Donor(
                createTimeStamp = UPDATED_CREATE_TIME_STAMP,

                updateTimeStamp = UPDATED_UPDATE_TIME_STAMP,

                name = UPDATED_NAME,

                city = UPDATED_CITY,

                country = UPDATED_COUNTRY

            )

            return donor
        }
    }
}
