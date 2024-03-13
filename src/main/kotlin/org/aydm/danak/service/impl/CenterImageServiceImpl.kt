package org.aydm.danak.service.impl

import org.aydm.danak.domain.CenterImage
import org.aydm.danak.repository.CenterImageRepository
import org.aydm.danak.service.CenterImageService
import org.aydm.danak.service.dto.CenterImageDTO
import org.aydm.danak.service.mapper.CenterImageMapper
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * Service Implementation for managing [CenterImage].
 */
@Service
@Transactional
class CenterImageServiceImpl(
    private val centerImageRepository: CenterImageRepository,
    private val centerImageMapper: CenterImageMapper,
) : CenterImageService {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun save(centerImageDTO: CenterImageDTO): CenterImageDTO {
        log.debug("Request to save CenterImage : $centerImageDTO")
        var centerImage = centerImageMapper.toEntity(centerImageDTO)
        centerImage = centerImageRepository.save(centerImage)
        return centerImageMapper.toDto(centerImage)
    }

    override fun update(centerImageDTO: CenterImageDTO): CenterImageDTO {
        log.debug("Request to update CenterImage : {}", centerImageDTO)
        var centerImage = centerImageMapper.toEntity(centerImageDTO)
        centerImage = centerImageRepository.save(centerImage)
        return centerImageMapper.toDto(centerImage)
    }

    override fun partialUpdate(centerImageDTO: CenterImageDTO): Optional<CenterImageDTO> {
        log.debug("Request to partially update CenterImage : {}", centerImageDTO)

        return centerImageRepository.findById(centerImageDTO.id)
            .map {
                centerImageMapper.partialUpdate(it, centerImageDTO)
                it
            }
            .map { centerImageRepository.save(it) }
            .map { centerImageMapper.toDto(it) }
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable): Page<CenterImageDTO> {
        log.debug("Request to get all CenterImages")
        return centerImageRepository.findAll(pageable)
            .map(centerImageMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findOne(id: Long): Optional<CenterImageDTO> {
        log.debug("Request to get CenterImage : $id")
        return centerImageRepository.findById(id)
            .map(centerImageMapper::toDto)
    }

    override fun delete(id: Long) {
        log.debug("Request to delete CenterImage : $id")

        centerImageRepository.deleteById(id)
    }
}
