package org.aydm.danak.service.impl

import org.aydm.danak.domain.FileBelongings
import org.aydm.danak.repository.FileBelongingsRepository
import org.aydm.danak.service.FileBelongingsService
import org.aydm.danak.service.dto.FileBelongingsDTO
import org.aydm.danak.service.mapper.FileBelongingsMapper
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * Service Implementation for managing [FileBelongings].
 */
@Service
@Transactional
class FileBelongingsServiceImpl(
    private val fileBelongingsRepository: FileBelongingsRepository,
    private val fileBelongingsMapper: FileBelongingsMapper,
) : FileBelongingsService {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun save(fileBelongingsDTO: FileBelongingsDTO): FileBelongingsDTO {
        log.debug("Request to save FileBelongings : $fileBelongingsDTO")
        var fileBelongings = fileBelongingsMapper.toEntity(fileBelongingsDTO)
        fileBelongings = fileBelongingsRepository.save(fileBelongings)
        return fileBelongingsMapper.toDto(fileBelongings)
    }

    override fun update(fileBelongingsDTO: FileBelongingsDTO): FileBelongingsDTO {
        log.debug("Request to update FileBelongings : {}", fileBelongingsDTO)
        var fileBelongings = fileBelongingsMapper.toEntity(fileBelongingsDTO)
        fileBelongings = fileBelongingsRepository.save(fileBelongings)
        return fileBelongingsMapper.toDto(fileBelongings)
    }

    override fun partialUpdate(fileBelongingsDTO: FileBelongingsDTO): Optional<FileBelongingsDTO> {
        log.debug("Request to partially update FileBelongings : {}", fileBelongingsDTO)

        return fileBelongingsRepository.findById(fileBelongingsDTO.id)
            .map {
                fileBelongingsMapper.partialUpdate(it, fileBelongingsDTO)
                it
            }
            .map { fileBelongingsRepository.save(it) }
            .map { fileBelongingsMapper.toDto(it) }
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable): Page<FileBelongingsDTO> {
        log.debug("Request to get all FileBelongings")
        return fileBelongingsRepository.findAll(pageable)
            .map(fileBelongingsMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findOne(id: Long): Optional<FileBelongingsDTO> {
        log.debug("Request to get FileBelongings : $id")
        return fileBelongingsRepository.findById(id)
            .map(fileBelongingsMapper::toDto)
    }

    override fun delete(id: Long) {
        log.debug("Request to delete FileBelongings : $id")

        fileBelongingsRepository.deleteById(id)
    }

    override fun saveAll(fileBelongings: MutableList<FileBelongingsDTO>): MutableList<FileBelongingsDTO> {
        return fileBelongingsMapper.toDto(fileBelongingsRepository.saveAll(fileBelongingsMapper.toEntity(fileBelongings)))
    }
}
