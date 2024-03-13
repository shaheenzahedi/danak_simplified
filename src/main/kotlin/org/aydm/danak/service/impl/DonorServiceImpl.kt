package org.aydm.danak.service.impl

import org.aydm.danak.domain.Donor
import org.aydm.danak.repository.DonorRepository
import org.aydm.danak.service.DonorService
import org.aydm.danak.service.dto.DonorDTO
import org.aydm.danak.service.mapper.DonorMapper
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * Service Implementation for managing [Donor].
 */
@Service
@Transactional
class DonorServiceImpl(
    private val donorRepository: DonorRepository,
    private val donorMapper: DonorMapper,
) : DonorService {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun save(donorDTO: DonorDTO): DonorDTO {
        log.debug("Request to save Donor : $donorDTO")
        var donor = donorMapper.toEntity(donorDTO)
        donor = donorRepository.save(donor)
        return donorMapper.toDto(donor)
    }

    override fun update(donorDTO: DonorDTO): DonorDTO {
        log.debug("Request to update Donor : {}", donorDTO)
        var donor = donorMapper.toEntity(donorDTO)
        donor = donorRepository.save(donor)
        return donorMapper.toDto(donor)
    }

    override fun partialUpdate(donorDTO: DonorDTO): Optional<DonorDTO> {
        log.debug("Request to partially update Donor : {}", donorDTO)

        return donorRepository.findById(donorDTO.id)
            .map {
                donorMapper.partialUpdate(it, donorDTO)
                it
            }
            .map { donorRepository.save(it) }
            .map { donorMapper.toDto(it) }
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable): Page<DonorDTO> {
        log.debug("Request to get all Donors")
        return donorRepository.findAll(pageable)
            .map(donorMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findOne(id: Long): Optional<DonorDTO> {
        log.debug("Request to get Donor : $id")
        return donorRepository.findById(id)
            .map(donorMapper::toDto)
    }

    override fun delete(id: Long) {
        log.debug("Request to delete Donor : $id")

        donorRepository.deleteById(id)
    }
}
