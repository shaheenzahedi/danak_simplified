package org.aydm.danak.service.impl

import org.aydm.danak.domain.Center
import org.aydm.danak.repository.CenterRepository
import org.aydm.danak.service.CenterService
import org.aydm.danak.service.dto.CenterDTO
import org.aydm.danak.service.mapper.CenterMapper
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * Service Implementation for managing [Center].
 */
@Service
@Transactional
class CenterServiceImpl(
    private val centerRepository: CenterRepository,
    private val centerMapper: CenterMapper,
) : CenterService {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun save(centerDTO: CenterDTO): CenterDTO {
        log.debug("Request to save Center : $centerDTO")
        var center = centerMapper.toEntity(centerDTO)
        center = centerRepository.save(center)
        return centerMapper.toDto(center)
    }

    override fun update(centerDTO: CenterDTO): CenterDTO {
        log.debug("Request to update Center : {}", centerDTO)
        var center = centerMapper.toEntity(centerDTO)
        center = centerRepository.save(center)
        return centerMapper.toDto(center)
    }

    override fun partialUpdate(centerDTO: CenterDTO): Optional<CenterDTO> {
        log.debug("Request to partially update Center : {}", centerDTO)

        return centerRepository.findById(centerDTO.id)
            .map {
                centerMapper.partialUpdate(it, centerDTO)
                it
            }
            .map { centerRepository.save(it) }
            .map { centerMapper.toDto(it) }
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable): Page<CenterDTO> {
        log.debug("Request to get all Centers")
        return centerRepository.findAll(pageable)
            .map(centerMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findOne(id: Long): Optional<CenterDTO> {
        log.debug("Request to get Center : $id")
        return centerRepository.findById(id)
            .map(centerMapper::toDto)
    }

    override fun delete(id: Long) {
        log.debug("Request to delete Center : $id")

        centerRepository.deleteById(id)
    }
}
