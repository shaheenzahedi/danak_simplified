package org.aydm.danak.service.impl

import org.aydm.danak.domain.TabletWatchList
import org.aydm.danak.repository.TabletWatchListRepository
import org.aydm.danak.service.TabletWatchListService
import org.aydm.danak.service.dto.TabletWatchListDTO
import org.aydm.danak.service.mapper.TabletWatchListMapper
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * Service Implementation for managing [TabletWatchList].
 */
@Service
@Transactional
class TabletWatchListServiceImpl(
    private val tabletWatchListRepository: TabletWatchListRepository,
    private val tabletWatchListMapper: TabletWatchListMapper,
) : TabletWatchListService {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun save(tabletWatchListDTO: TabletWatchListDTO): TabletWatchListDTO {
        log.debug("Request to save TabletWatchList : $tabletWatchListDTO")
        var tabletWatchList = tabletWatchListMapper.toEntity(tabletWatchListDTO)
        tabletWatchList = tabletWatchListRepository.save(tabletWatchList)
        return tabletWatchListMapper.toDto(tabletWatchList)
    }

    override fun update(tabletWatchListDTO: TabletWatchListDTO): TabletWatchListDTO {
        log.debug("Request to update TabletWatchList : {}", tabletWatchListDTO)
        var tabletWatchList = tabletWatchListMapper.toEntity(tabletWatchListDTO)
        tabletWatchList = tabletWatchListRepository.save(tabletWatchList)
        return tabletWatchListMapper.toDto(tabletWatchList)
    }

    override fun partialUpdate(tabletWatchListDTO: TabletWatchListDTO): Optional<TabletWatchListDTO> {
        log.debug("Request to partially update TabletWatchList : {}", tabletWatchListDTO)

        return tabletWatchListRepository.findById(tabletWatchListDTO.id)
            .map {
                tabletWatchListMapper.partialUpdate(it, tabletWatchListDTO)
                it
            }
            .map { tabletWatchListRepository.save(it) }
            .map { tabletWatchListMapper.toDto(it) }
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable): Page<TabletWatchListDTO> {
        log.debug("Request to get all TabletWatchLists")
        return tabletWatchListRepository.findAll(pageable)
            .map(tabletWatchListMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findOne(id: Long): Optional<TabletWatchListDTO> {
        log.debug("Request to get TabletWatchList : $id")
        return tabletWatchListRepository.findById(id)
            .map(tabletWatchListMapper::toDto)
    }

    override fun delete(id: Long) {
        log.debug("Request to delete TabletWatchList : $id")

        tabletWatchListRepository.deleteById(id)
    }
}
