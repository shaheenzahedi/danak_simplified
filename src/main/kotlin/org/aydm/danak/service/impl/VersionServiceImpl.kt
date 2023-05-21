package org.aydm.danak.service.impl


import org.aydm.danak.service.VersionService
import org.aydm.danak.domain.Version
import org.aydm.danak.repository.VersionRepository
import org.aydm.danak.service.dto.VersionDTO
import org.aydm.danak.service.mapper.VersionMapper
import org.slf4j.LoggerFactory

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.Optional
import java.util.stream.Collectors

/**
 * Service Implementation for managing [Version].
 */
@Service
@Transactional
class VersionServiceImpl(
            private val versionRepository: VersionRepository,
        private val versionMapper: VersionMapper,
) : VersionService {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun save(versionDTO: VersionDTO): VersionDTO {
        log.debug("Request to save Version : $versionDTO")
        var version = versionMapper.toEntity(versionDTO)
        version = versionRepository.save(version)
        return versionMapper.toDto(version)
    }

    override fun update(versionDTO: VersionDTO): VersionDTO{
            log.debug("Request to save Version : {}", versionDTO);
            var version = versionMapper.toEntity(versionDTO)
        version = versionRepository.save(version)
        return versionMapper.toDto(version)
        }

    override fun partialUpdate(versionDTO: VersionDTO): Optional<VersionDTO> {
        log.debug("Request to partially update Version : {}", versionDTO)


         return versionRepository.findById(versionDTO.id)
            .map {
versionMapper.partialUpdate(it, versionDTO)
               it
            }
            .map { versionRepository.save(it) }
.map { versionMapper.toDto(it) }

    }

    @Transactional(readOnly = true)
    override fun findAll(): MutableList<VersionDTO> {
        log.debug("Request to get all Versions")
        return versionRepository.findAll()
            .mapTo(mutableListOf(), versionMapper::toDto)
    }


    @Transactional(readOnly = true)
    override fun findOne(id: Long): Optional<VersionDTO> {
        log.debug("Request to get Version : $id")
        return versionRepository.findById(id)
            .map(versionMapper::toDto)
    }

    override fun delete(id: Long) {
        log.debug("Request to delete Version : $id")

        versionRepository.deleteById(id)
    }

    override fun findIdByVersion(fromVersion: Int): Long {
        return versionRepository.findByVersion(fromVersion).id!!
    }
}
