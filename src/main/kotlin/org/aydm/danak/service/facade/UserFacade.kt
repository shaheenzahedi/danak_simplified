package org.aydm.danak.service.facade

import org.aydm.danak.domain.Authority
import org.aydm.danak.domain.User
import org.aydm.danak.repository.UserRepository
import org.aydm.danak.security.DONOR
import org.aydm.danak.service.DonorService
import org.aydm.danak.service.UsernameAlreadyUsedException
import org.aydm.danak.service.dto.DonorDTO
import org.aydm.danak.service.mapper.UserMapper
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

interface UserFacade {
    fun registerDonor(dto: DonorDTO): DonorDTO
}

@Transactional
@Service
class UserFacadeImpl(
    private val userRepository: UserRepository,
    private val donorService: DonorService,
    private val userMapper:UserMapper,
    private val passwordEncoder: PasswordEncoder,
) : UserFacade {

    override fun registerDonor(dto: DonorDTO): DonorDTO {
        userRepository
            .findOneByEmailIgnoreCase(dto.user?.email)
            .ifPresent { existingUser ->
                val removed = removeNonActivatedUser(existingUser)
                if (!removed) throw UsernameAlreadyUsedException()
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
