package org.aydm.danak.service.impl

import org.aydm.danak.domain.UploadedFile
import org.aydm.danak.repository.UploadedFileRepository
import org.aydm.danak.service.UploadedFileService
import org.aydm.danak.service.dto.UploadedFileDTO
import org.aydm.danak.service.mapper.UploadedFileMapper
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * Service Implementation for managing [UploadedFile].
 */
@Service
@Transactional
class UploadedFileServiceImpl(
    private val uploadedFileRepository: UploadedFileRepository,
    private val uploadedFileMapper: UploadedFileMapper,
) : UploadedFileService {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun save(uploadedFileDTO: UploadedFileDTO): UploadedFileDTO {
        log.debug("Request to save UploadedFile : $uploadedFileDTO")
        var uploadedFile = uploadedFileMapper.toEntity(uploadedFileDTO)
        uploadedFile = uploadedFileRepository.save(uploadedFile)
        return uploadedFileMapper.toDto(uploadedFile)
    }

    override fun update(uploadedFileDTO: UploadedFileDTO): UploadedFileDTO {
        log.debug("Request to update UploadedFile : {}", uploadedFileDTO)
        var uploadedFile = uploadedFileMapper.toEntity(uploadedFileDTO)
        uploadedFile = uploadedFileRepository.save(uploadedFile)
        return uploadedFileMapper.toDto(uploadedFile)
    }

    override fun partialUpdate(uploadedFileDTO: UploadedFileDTO): Optional<UploadedFileDTO> {
        log.debug("Request to partially update UploadedFile : {}", uploadedFileDTO)

        return uploadedFileRepository.findById(uploadedFileDTO.id)
            .map {
                uploadedFileMapper.partialUpdate(it, uploadedFileDTO)
                it
            }
            .map { uploadedFileRepository.save(it) }
            .map { uploadedFileMapper.toDto(it) }
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable): Page<UploadedFileDTO> {
        log.debug("Request to get all UploadedFiles")
        return uploadedFileRepository.findAll(pageable)
            .map(uploadedFileMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findOne(id: Long): Optional<UploadedFileDTO> {
        log.debug("Request to get UploadedFile : $id")
        return uploadedFileRepository.findById(id)
            .map(uploadedFileMapper::toDto)
    }

    override fun delete(id: Long) {
        log.debug("Request to delete UploadedFile : $id")

        uploadedFileRepository.deleteById(id)
    }
}
