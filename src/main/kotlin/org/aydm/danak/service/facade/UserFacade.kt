package org.aydm.danak.service.facade

import org.aydm.danak.domain.Authority
import org.aydm.danak.domain.User
import org.aydm.danak.repository.UserRepository
import org.aydm.danak.security.DONOR
import org.aydm.danak.service.*
import org.aydm.danak.service.criteria.DonorCriteria
import org.aydm.danak.service.criteria.TabletUserCriteria
import org.aydm.danak.service.criteria.UserActivityCriteria
import org.aydm.danak.service.dto.DonorDTO
import org.aydm.danak.service.dto.TabletDTO
import org.aydm.danak.service.dto.TabletUserDTO
import org.aydm.danak.service.dto.UserActivityDTO
import org.aydm.danak.service.mapper.UserMapper
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tech.jhipster.service.filter.LongFilter
import java.util.*

interface UserFacade {
    fun registerDonor(dto: DonorDTO): DonorDTO
    fun getDonorKeyword(): String?
    fun getDonors(pageable: Pageable): Page<DonorDTO>?
    fun findAllTablets(@org.springdoc.api.annotations.ParameterObject pageable: Pageable): Page<TabletDTO>
    fun findAllTabletUsers(criteria: TabletUserCriteria?, pageable: Pageable): Page<TabletUserDTO>
    fun getAllActivities(criteria: UserActivityCriteria?, pageable: Pageable): Page<UserActivityDTO>
    fun tabletsFixDuplicates()
    fun tabletsGetDuplicates(): MutableList<TabletDTO>
}

@Transactional
@Service
class UserFacadeImpl(
    private val userRepository: UserRepository,
    private val donorService: DonorService,
    private val donorQueryService: DonorQueryService,
    private val userMapper: UserMapper,
    private val passwordEncoder: PasswordEncoder,
    private val tabletService: TabletService,
    private val activityQueryService: UserActivityQueryService,
    private val tabletUserQueryService: TabletUserQueryService
) : UserFacade {
    private fun getCurrentUserName(): String {
        return SecurityContextHolder.getContext().authentication.name
    }

    fun getCurrentUser(): Optional<User> {
        return userRepository.findOneByLogin(getCurrentUserName())
    }

    override fun getDonorKeyword(): String? {
        val currentUser = getCurrentUser().orElse(null) ?: return null
        val criteria = DonorCriteria().apply { userId = LongFilter().apply { equals = currentUser.id } }
        val result = donorQueryService.findByCriteria(criteria)
        return if (result.isEmpty()) null else result.first().name
    }

    override fun getDonors(pageable: Pageable): Page<DonorDTO>? {
        return donorService.findAll(pageable)
            .map {
                val user = userRepository.findById(it.user?.id!!).orElseThrow()
                val userDTO = userMapper.userToUserDTO(user)
                it.user = userDTO.apply {
                    firstName = user.firstName
                    lastName = user.lastName
                }
                it
            }
    }

    override fun findAllTablets(pageable: Pageable): Page<TabletDTO> {
        return tabletService.findAll(pageable)
            .onEach {
                it.numberOfUsers = tabletUserQueryService.countByCriteria(
                    TabletUserCriteria().apply {
                        this.tabletId = LongFilter().apply { equals = it.id }
                    }
                )
            }
    }

    override fun findAllTabletUsers(criteria: TabletUserCriteria?, pageable: Pageable): Page<TabletUserDTO> =
        tabletUserQueryService.findAll(criteria, pageable)

    override fun getAllActivities(criteria: UserActivityCriteria?, pageable: Pageable): Page<UserActivityDTO> {
        return activityQueryService.findByCriteria(criteria, pageable)
    }

    override fun tabletsFixDuplicates() {
        val duplicateTablets = tabletService.findAllDuplicates()
            .onEach { it.numberOfUsers = tabletUserQueryService.countTabletUsers(it.id!!) }
            .groupBy { it.name }
        val listToDelete = mutableListOf<TabletDTO>()
        val listToEdit = mutableListOf<TabletDTO>()
        for (entry in duplicateTablets) {
            val longestModel = entry.value
                .mapNotNull { it.model } // Filter out null 'model' values
                .maxByOrNull { it.length } // Find the max by 'model' length
            val longestIdentifier = entry.value
                .mapNotNull { it.identifier } // Filter out null 'identifier' values
                .maxByOrNull { it.length } // Find the max by 'identifier' length
            entry.value.forEach {
                if (it.numberOfUsers == 0L) {
                    listToDelete.add(it)
                } else {
                    it.model = longestModel
                    it.identifier = longestIdentifier
                    listToEdit.add(it)
                }
            }
        }
        tabletService.deleteAll(listToDelete)
        tabletService.saveAll(listToEdit)
    }

    fun TabletUserQueryService.countTabletUsers(tabletId: Long): Long {
        val criteria = TabletUserCriteria().apply {
            this.tabletId = LongFilter().apply { equals = tabletId }
        }
        return this.countByCriteria(criteria)
    }

    override fun tabletsGetDuplicates(): MutableList<TabletDTO> {
        return tabletService.findAllDuplicates()
    }

    override fun registerDonor(dto: DonorDTO): DonorDTO {
        userRepository
            .findOneByEmailIgnoreCase(dto.user?.email)
            .ifPresent {
                throw UsernameAlreadyUsedException()
            }
        val newUser = User()
        val encryptedPassword: String = passwordEncoder.encode(dto.user?.password)
        newUser.login = dto.user?.email?.lowercase(Locale.getDefault())
        newUser.password = encryptedPassword
        newUser.firstName = dto.user?.firstName
        newUser.lastName = dto.user?.lastName
        newUser.email = dto.user?.email
        newUser.activated = true
        val authorities = HashSet<Authority>()
        authorities.add(Authority(DONOR))
        newUser.authorities = authorities
        val user = userRepository.save(newUser)
        dto.user = userMapper.userToUserDTO(user)
        return donorService.save(dto)
    }

    private fun removeNonActivatedUser(existingUser: User): Boolean {
        if (existingUser.activated == null || !existingUser.activated!!) {
            return false
        }
        userRepository.delete(existingUser)
        userRepository.flush()
        return true
    }
}
