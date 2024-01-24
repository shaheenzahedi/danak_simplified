package org.aydm.danak.service.impl

import org.aydm.danak.domain.CenterDonor
import org.aydm.danak.repository.CenterDonorRepository
import org.aydm.danak.service.CenterDonorService
import org.aydm.danak.service.dto.CenterDonorDTO
import org.aydm.danak.service.dto.UserDTO
import org.aydm.danak.service.mapper.CenterDonorMapper
import org.aydm.danak.service.mapper.CenterMapper
import org.aydm.danak.service.mapper.DonorMapper
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.Optional

/**
 * Service Implementation for managing [CenterDonor].
 */
@Service
@Transactional
class CenterDonorServiceImpl(
    private val centerDonorRepository: CenterDonorRepository,
    private val centerDonorMapper: CenterDonorMapper,
    private val centerMapper: CenterMapper,
    private val donorMapper: DonorMapper,
) : CenterDonorService {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun save(centerDonorDTO: CenterDonorDTO): CenterDonorDTO {
        log.debug("Request to save CenterDonor : $centerDonorDTO")
        var centerDonor = centerDonorMapper.toEntity(centerDonorDTO)
        centerDonor = centerDonorRepository.save(centerDonor)
        return centerDonorMapper.toDto(centerDonor)
    }

    override fun update(centerDonorDTO: CenterDonorDTO): CenterDonorDTO {
        log.debug("Request to update CenterDonor : {}", centerDonorDTO)
        var centerDonor = centerDonorMapper.toEntity(centerDonorDTO)
        centerDonor = centerDonorRepository.save(centerDonor)
        return centerDonorMapper.toDto(centerDonor)
    }

    override fun partialUpdate(centerDonorDTO: CenterDonorDTO): Optional<CenterDonorDTO> {
        log.debug("Request to partially update CenterDonor : {}", centerDonorDTO)

        return centerDonorRepository.findById(centerDonorDTO.id)
            .map {
                centerDonorMapper.partialUpdate(it, centerDonorDTO)
                it
            }
            .map { centerDonorRepository.save(it) }
            .map { centerDonorMapper.toDto(it) }
    }

    @Transactional(readOnly = true)
    override fun findAll(): MutableList<CenterDonorDTO> {
        log.debug("Request to get all CenterDonors")
        return centerDonorRepository.findAll()
            .mapTo(mutableListOf(), centerDonorMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findOne(id: Long): Optional<CenterDonorDTO> {
        log.debug("Request to get CenterDonor : $id")
        return centerDonorRepository.findById(id)
            .map(centerDonorMapper::toDto)
    }

    override fun delete(id: Long) {
        log.debug("Request to delete CenterDonor : $id")

        centerDonorRepository.deleteById(id)
    }

    override fun findAllCenterDonorsTable(): MutableList<CenterDonorDTO> {
        return centerDonorRepository.findAll()
            .mapTo(mutableListOf()) {
                centerDonorMapper.toDto(it).apply {
                    center = it.center?.let(centerMapper::toDto)
                    donor = it.donor?.let { donor ->
                        donorMapper.toDto(donor).apply {
                            user = UserDTO().apply {
                                firstName = donor.user?.firstName
                                lastName = donor.user?.lastName
                            }
                        }
                    }
                }
            }
    }
}
