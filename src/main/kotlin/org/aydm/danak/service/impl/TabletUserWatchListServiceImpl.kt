package org.aydm.danak.service.impl

import org.aydm.danak.domain.TabletUserWatchList
import org.aydm.danak.repository.TabletUserWatchListRepository
import org.aydm.danak.service.TabletUserWatchListService
import org.aydm.danak.service.dto.TabletUserWatchListDTO
import org.aydm.danak.service.mapper.TabletUserWatchListMapper
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * Service Implementation for managing [TabletUserWatchList].
 */
@Service
@Transactional
class TabletUserWatchListServiceImpl(
    private val tabletUserWatchListRepository: TabletUserWatchListRepository,
    private val tabletUserWatchListMapper: TabletUserWatchListMapper,
) : TabletUserWatchListService {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun save(tabletUserWatchListDTO: TabletUserWatchListDTO): TabletUserWatchListDTO {
        log.debug("Request to save TabletUserWatchList : $tabletUserWatchListDTO")
        var tabletUserWatchList = tabletUserWatchListMapper.toEntity(tabletUserWatchListDTO)
        tabletUserWatchList = tabletUserWatchListRepository.save(tabletUserWatchList)
        return tabletUserWatchListMapper.toDto(tabletUserWatchList)
    }

    override fun update(tabletUserWatchListDTO: TabletUserWatchListDTO): TabletUserWatchListDTO {
        log.debug("Request to update TabletUserWatchList : {}", tabletUserWatchListDTO)
        var tabletUserWatchList = tabletUserWatchListMapper.toEntity(tabletUserWatchListDTO)
        tabletUserWatchList = tabletUserWatchListRepository.save(tabletUserWatchList)
        return tabletUserWatchListMapper.toDto(tabletUserWatchList)
    }

    override fun partialUpdate(tabletUserWatchListDTO: TabletUserWatchListDTO): Optional<TabletUserWatchListDTO> {
        log.debug("Request to partially update TabletUserWatchList : {}", tabletUserWatchListDTO)

        return tabletUserWatchListRepository.findById(tabletUserWatchListDTO.id)
            .map {
                tabletUserWatchListMapper.partialUpdate(it, tabletUserWatchListDTO)
                it
            }
            .map { tabletUserWatchListRepository.save(it) }
            .map { tabletUserWatchListMapper.toDto(it) }
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable): Page<TabletUserWatchListDTO> {
        log.debug("Request to get all TabletUserWatchLists")
        return tabletUserWatchListRepository.findAll(pageable)
            .map(tabletUserWatchListMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findOne(id: Long): Optional<TabletUserWatchListDTO> {
        log.debug("Request to get TabletUserWatchList : $id")
        return tabletUserWatchListRepository.findById(id)
            .map(tabletUserWatchListMapper::toDto)
    }

    override fun delete(id: Long) {
        log.debug("Request to delete TabletUserWatchList : $id")

        tabletUserWatchListRepository.deleteById(id)
    }
}
