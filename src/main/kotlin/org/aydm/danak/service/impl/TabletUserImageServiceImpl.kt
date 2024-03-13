package org.aydm.danak.service.impl

import org.aydm.danak.domain.TabletUserImage
import org.aydm.danak.repository.TabletUserImageRepository
import org.aydm.danak.service.TabletUserImageService
import org.aydm.danak.service.dto.TabletUserImageDTO
import org.aydm.danak.service.mapper.TabletUserImageMapper
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * Service Implementation for managing [TabletUserImage].
 */
@Service
@Transactional
class TabletUserImageServiceImpl(
    private val tabletUserImageRepository: TabletUserImageRepository,
    private val tabletUserImageMapper: TabletUserImageMapper,
) : TabletUserImageService {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun save(tabletUserImageDTO: TabletUserImageDTO): TabletUserImageDTO {
        log.debug("Request to save TabletUserImage : $tabletUserImageDTO")
        var tabletUserImage = tabletUserImageMapper.toEntity(tabletUserImageDTO)
        tabletUserImage = tabletUserImageRepository.save(tabletUserImage)
        return tabletUserImageMapper.toDto(tabletUserImage)
    }

    override fun update(tabletUserImageDTO: TabletUserImageDTO): TabletUserImageDTO {
        log.debug("Request to update TabletUserImage : {}", tabletUserImageDTO)
        var tabletUserImage = tabletUserImageMapper.toEntity(tabletUserImageDTO)
        tabletUserImage = tabletUserImageRepository.save(tabletUserImage)
        return tabletUserImageMapper.toDto(tabletUserImage)
    }

    override fun partialUpdate(tabletUserImageDTO: TabletUserImageDTO): Optional<TabletUserImageDTO> {
        log.debug("Request to partially update TabletUserImage : {}", tabletUserImageDTO)

        return tabletUserImageRepository.findById(tabletUserImageDTO.id)
            .map {
                tabletUserImageMapper.partialUpdate(it, tabletUserImageDTO)
                it
            }
            .map { tabletUserImageRepository.save(it) }
            .map { tabletUserImageMapper.toDto(it) }
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable): Page<TabletUserImageDTO> {
        log.debug("Request to get all TabletUserImages")
        return tabletUserImageRepository.findAll(pageable)
            .map(tabletUserImageMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findOne(id: Long): Optional<TabletUserImageDTO> {
        log.debug("Request to get TabletUserImage : $id")
        return tabletUserImageRepository.findById(id)
            .map(tabletUserImageMapper::toDto)
    }

    override fun delete(id: Long) {
        log.debug("Request to delete TabletUserImage : $id")

        tabletUserImageRepository.deleteById(id)
    }
}
