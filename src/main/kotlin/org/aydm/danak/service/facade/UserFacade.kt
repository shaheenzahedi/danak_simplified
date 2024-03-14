package org.aydm.danak.service.facade

import org.aydm.danak.domain.Authority
import org.aydm.danak.domain.TabletUser
import org.aydm.danak.domain.User
import org.aydm.danak.repository.UserRepository
import org.aydm.danak.security.DONOR
import org.aydm.danak.service.*
import org.aydm.danak.service.criteria.*
import org.aydm.danak.service.dto.*
import org.aydm.danak.service.mapper.UserMapper
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tech.jhipster.service.filter.BooleanFilter
import tech.jhipster.service.filter.LongFilter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

interface UserFacade {
    fun registerDonor(dto: DonorDTO): DonorDTO
    fun getDonorId(): Long?
    fun getDonors(pageable: Pageable): Page<DonorDTO>?
    fun findAllTablets(pageable: Pageable, criteria: TabletCriteria?): Page<TabletDTO>
    fun findAllTabletUsers(criteria: TabletUserCriteria?, pageable: Pageable): Page<TabletUserDTO>
    fun getAllActivities(criteria: UserActivityCriteria?, pageable: Pageable): Page<UserActivityDTO>
    fun tabletsFixDuplicates()
    fun tabletsGetDuplicates(): MutableList<TabletDTO>
    fun fixTabletNames()
    fun getDashboard(centerId: Long?, days: Int?): DashboardDTO
    fun archiveTabletUser(id: Long)
    fun restoreTabletUser(id: Long)
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
    private val tabletQueryService: TabletQueryService,
    private val activityQueryService: UserActivityQueryService,
    private val tabletUserQueryService: TabletUserQueryService,
    private val centerService: CenterService,
    private val centerQueryService: CenterQueryService
) : UserFacade {
    private fun getCurrentUserName(): String {
        return SecurityContextHolder.getContext().authentication.name
    }

    fun getCurrentUser(): Optional<User> {
        return userRepository.findOneByLogin(getCurrentUserName())
    }

    override fun getDonorId(): Long? {
        val currentUser = getCurrentUser().orElse(null) ?: return null
        val criteria = DonorCriteria().apply { userId = LongFilter().apply { equals = currentUser.id } }
        val result = donorQueryService.findByCriteria(criteria)
        return if (result.isEmpty()) null else result.first().id
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

    override fun findAllTablets(pageable: Pageable, criteria: TabletCriteria?): Page<TabletDTO> {
        val donorId = getDonorId()
        return tabletQueryService.findByCriteriaByDonorId(criteria, pageable, donorId)
            .onEach(processEachTablet)
    }

    val processEachTablet: (TabletDTO) -> Unit = { tabletDTO ->
        if (tabletDTO.center != null) {
            centerService.findOne(tabletDTO.center!!.id!!).ifPresent {
                tabletDTO.center = it
            }
        }
        tabletDTO.numberOfUsers = tabletUserQueryService.countByCriteria(
            TabletUserCriteria().apply {
                this.tabletId = LongFilter().apply { equals = tabletDTO.id }
            }
        )
    }

    override fun findAllTabletUsers(criteria: TabletUserCriteria?, pageable: Pageable): Page<TabletUserDTO> {
        val cr = criteria ?: TabletUserCriteria()
        cr.archived = BooleanFilter().apply {notEquals=true
        specified=false}
        val donorId = getDonorId() ?: return tabletUserQueryService.findAll(cr, pageable)
        val tabletIds = tabletService.findAllTabletIdsByDonorId(donorId)
        if (tabletIds.isEmpty()) return Page.empty()
        cr.tabletId = cr.tabletId?.apply { `in` = tabletIds } ?: LongFilter().apply { `in` = tabletIds }
        return tabletUserQueryService.findAll(cr, pageable)
    }

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

    override fun fixTabletNames() {
        tabletService.findAllTabletsWithoutIdentifier()
            .filterNot { it.tabletUsers.isNullOrEmpty() }
            .associate { it.id to extractTabletName(it.tabletUsers!!) }
            .filterNot { it.value == null }
            .forEach { tablet ->
                tabletService.findOne(tablet.key!!)
                    .ifPresent {
                        tabletService.save(
                            it.apply {
                                identifier = tablet.value
                                updateTimeStamp = Instant.now()
                            }
                        )
                    }
            }
    }

    override fun getDashboard(centerId: Long?, days: Int?): DashboardDTO {
        val numberOfTablets = tabletQueryService.countByCriteria(TabletCriteria())
        val numberOfCenters = centerQueryService.countByCriteria(CenterCriteria())
        val numberOfUsers = tabletUserQueryService.countByCriteria(TabletUserCriteria())
        val numberOfReports = 0L/*activityQueryService.countByCriteria(UserActivityCriteria())*/
        val reports = activityQueryService.findByCenterId(centerId, days)
            .groupBy {
                LocalDateTime.ofInstant(it.createTimeStamp, ZoneId.systemDefault()).toLocalDate()
            }.mapValues { (_, userActivities) ->
                // each user activity has different uniqueName field, I want all the activities with
                // same uniqueName to be grouped together and take the one with the greatest out of it
                val progressWithHighestId = userActivities.groupBy { it.uniqueName }
                    .mapValues { (_, activitiesWithSameUniqueName) ->
                        activitiesWithSameUniqueName.maxByOrNull { it.id!! }
                    }
                DashboardReport(
                    count = userActivities.size / progressWithHighestId.size,
                    completed = progressWithHighestId.values.sumOf { it?.completed!! },
                    children = userActivities.map { userActivity -> userActivity.activity?.id }.toSet().size
                )
            }
        val centers = centerQueryService.findByCriteria(CenterCriteria())
        return DashboardDTO(
            numberOfTablets = numberOfTablets,
            numberOfUsers = numberOfUsers,
            numberOfReports = numberOfReports,
            numberOfCenters = numberOfCenters,
            centers = centers,
            reports = reports
        )
    }

    override fun archiveTabletUser(id: Long) {
        val tabletUser = tabletUserQueryService.findOne(id)
            .orElseThrow { Exception("there is no tablet user with this id") }
        val currentUser = getCurrentUser().orElseThrow()
        tabletUser.updateTimeStamp = Instant.now()
        tabletUser.archived = true
        tabletUser.archivedBy = currentUser
        tabletUserQueryService.update(tabletUser)
    }

    override fun restoreTabletUser(id: Long) {
        val tabletUser = tabletUserQueryService.findOne(id)
            .orElseThrow { Exception("there is no tablet user with this id") }
        val currentUser = getCurrentUser().orElseThrow()
        tabletUser.updateTimeStamp = Instant.now()
        tabletUser.archived = false
        tabletUser.archivedBy = null
        tabletUser.modifiedBy = currentUser
        tabletUserQueryService.update(tabletUser)
    }

    private fun extractTabletName(tabletUsers: MutableSet<TabletUser>): String? {
        val regex = Regex("T\\d+") // create a regex object with the pattern
        for (user in tabletUsers) {
            val match = regex.find(user.firstName.orEmpty()) ?: regex.find(user.lastName.orEmpty())
            if (match != null) {
                return match.value
            }
        }
        return null
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
