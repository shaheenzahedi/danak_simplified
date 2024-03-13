package org.aydm.danak.service.impl

import org.aydm.danak.domain.DonorImage
import org.aydm.danak.repository.DonorImageRepository
import org.aydm.danak.service.DonorImageService
import org.aydm.danak.service.dto.DonorImageDTO
import org.aydm.danak.service.mapper.DonorImageMapper
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * Service Implementation for managing [DonorImage].
 */
@Service
@Transactional
class DonorImageServiceImpl(
    private val donorImageRepository: DonorImageRepository,
    private val donorImageMapper: DonorImageMapper,
) : DonorImageService {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun save(donorImageDTO: DonorImageDTO): DonorImageDTO {
        log.debug("Request to save DonorImage : $donorImageDTO")
        var donorImage = donorImageMapper.toEntity(donorImageDTO)
        donorImage = donorImageRepository.save(donorImage)
        return donorImageMapper.toDto(donorImage)
    }

    override fun update(donorImageDTO: DonorImageDTO): DonorImageDTO {
        log.debug("Request to update DonorImage : {}", donorImageDTO)
        var donorImage = donorImageMapper.toEntity(donorImageDTO)
        donorImage = donorImageRepository.save(donorImage)
        return donorImageMapper.toDto(donorImage)
    }

    override fun partialUpdate(donorImageDTO: DonorImageDTO): Optional<DonorImageDTO> {
        log.debug("Request to partially update DonorImage : {}", donorImageDTO)

        return donorImageRepository.findById(donorImageDTO.id)
            .map {
                donorImageMapper.partialUpdate(it, donorImageDTO)
                it
            }
            .map { donorImageRepository.save(it) }
            .map { donorImageMapper.toDto(it) }
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable): Page<DonorImageDTO> {
        log.debug("Request to get all DonorImages")
        return donorImageRepository.findAll(pageable)
            .map(donorImageMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findOne(id: Long): Optional<DonorImageDTO> {
        log.debug("Request to get DonorImage : $id")
        return donorImageRepository.findById(id)
            .map(donorImageMapper::toDto)
    }

    override fun delete(id: Long) {
        log.debug("Request to delete DonorImage : $id")

        donorImageRepository.deleteById(id)
    }
}
