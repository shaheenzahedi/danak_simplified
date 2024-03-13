package org.aydm.danak.service.impl

import org.aydm.danak.domain.FileShare
import org.aydm.danak.repository.FileShareRepository
import org.aydm.danak.service.FileShareService
import org.aydm.danak.service.dto.FileShareDTO
import org.aydm.danak.service.mapper.FileShareMapper
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * Service Implementation for managing [FileShare].
 */
@Service
@Transactional
class FileShareServiceImpl(
    private val fileShareRepository: FileShareRepository,
    private val fileShareMapper: FileShareMapper,
) : FileShareService {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun save(fileShareDTO: FileShareDTO): FileShareDTO {
        log.debug("Request to save FileShare : $fileShareDTO")
        var fileShare = fileShareMapper.toEntity(fileShareDTO)
        fileShare = fileShareRepository.save(fileShare)
        return fileShareMapper.toDto(fileShare)
    }

    override fun update(fileShareDTO: FileShareDTO): FileShareDTO {
        log.debug("Request to update FileShare : {}", fileShareDTO)
        var fileShare = fileShareMapper.toEntity(fileShareDTO)
        fileShare = fileShareRepository.save(fileShare)
        return fileShareMapper.toDto(fileShare)
    }

    override fun partialUpdate(fileShareDTO: FileShareDTO): Optional<FileShareDTO> {
        log.debug("Request to partially update FileShare : {}", fileShareDTO)

        return fileShareRepository.findById(fileShareDTO.id)
            .map {
                fileShareMapper.partialUpdate(it, fileShareDTO)
                it
            }
            .map { fileShareRepository.save(it) }
            .map { fileShareMapper.toDto(it) }
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable): Page<FileShareDTO> {
        log.debug("Request to get all FileShares")
        return fileShareRepository.findAll(pageable)
            .map(fileShareMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findOne(id: Long): Optional<FileShareDTO> {
        log.debug("Request to get FileShare : $id")
        return fileShareRepository.findById(id)
            .map(fileShareMapper::toDto)
    }

    override fun delete(id: Long) {
        log.debug("Request to delete FileShare : $id")

        fileShareRepository.deleteById(id)
    }
}
