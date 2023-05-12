package org.aydm.danak.service.impl

import org.aydm.danak.domain.File
import org.aydm.danak.repository.FileRepository
import org.aydm.danak.service.FileService
import org.aydm.danak.service.dto.FileDTO
import org.aydm.danak.service.mapper.FileMapper
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.Optional

/**
 * Service Implementation for managing [File].
 */
@Service
@Transactional
class FileServiceImpl(
    private val fileRepository: FileRepository,
    private val fileMapper: FileMapper,
) : FileService {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun save(fileDTO: FileDTO): FileDTO {
        log.debug("Request to save File : $fileDTO")
        var file = fileMapper.toEntity(fileDTO)
        file = fileRepository.save(file)
        return fileMapper.toDto(file)
    }

    override fun update(fileDTO: FileDTO): FileDTO {
        log.debug("Request to save File : {}", fileDTO)
        var file = fileMapper.toEntity(fileDTO)
        file = fileRepository.save(file)
        return fileMapper.toDto(file)
    }

    override fun partialUpdate(fileDTO: FileDTO): Optional<FileDTO> {
        log.debug("Request to partially update File : {}", fileDTO)

        return fileRepository.findById(fileDTO.id)
            .map {
                fileMapper.partialUpdate(it, fileDTO)
                it
            }
            .map { fileRepository.save(it) }
            .map { fileMapper.toDto(it) }
    }

    @Transactional(readOnly = true)
    override fun findAll(): MutableList<FileDTO> {
        log.debug("Request to get all Files")
        return fileRepository.findAll()
            .mapTo(mutableListOf(), fileMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findOne(id: Long): Optional<FileDTO> {
        log.debug("Request to get File : $id")
        return fileRepository.findById(id)
            .map(fileMapper::toDto)
    }

    override fun delete(id: Long) {
        log.debug("Request to delete File : $id")

        fileRepository.deleteById(id)
    }

    override fun saveAll(files: MutableList<FileDTO>): MutableList<FileDTO> {
        return fileMapper.toDto(fileRepository.saveAll(fileMapper.toEntity(files)))
    }

    override fun findAllLastVersion(versionId: Long): MutableList<FileDTO> {
        return fileMapper.toDto(fileRepository.findAllByPlacementId(versionId))
    }

    override fun findAllBelongsToVersion(version: Long): MutableList<FileDTO> {
        return fileMapper.toDto(fileRepository.findAllBelongsToVersion(version))
    }

    override fun findAllUpdates(v1: Long, v2: Long): MutableList<FileDTO> {
        return fileMapper.toDto(fileRepository.findAllUpdates(v1, v2))
    }
}
