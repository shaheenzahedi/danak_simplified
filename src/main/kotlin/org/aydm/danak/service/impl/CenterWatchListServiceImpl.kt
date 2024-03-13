package org.aydm.danak.service.impl

import org.aydm.danak.domain.CenterWatchList
import org.aydm.danak.repository.CenterWatchListRepository
import org.aydm.danak.service.CenterWatchListService
import org.aydm.danak.service.dto.CenterWatchListDTO
import org.aydm.danak.service.mapper.CenterWatchListMapper
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * Service Implementation for managing [CenterWatchList].
 */
@Service
@Transactional
class CenterWatchListServiceImpl(
    private val centerWatchListRepository: CenterWatchListRepository,
    private val centerWatchListMapper: CenterWatchListMapper,
) : CenterWatchListService {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun save(centerWatchListDTO: CenterWatchListDTO): CenterWatchListDTO {
        log.debug("Request to save CenterWatchList : $centerWatchListDTO")
        var centerWatchList = centerWatchListMapper.toEntity(centerWatchListDTO)
        centerWatchList = centerWatchListRepository.save(centerWatchList)
        return centerWatchListMapper.toDto(centerWatchList)
    }

    override fun update(centerWatchListDTO: CenterWatchListDTO): CenterWatchListDTO {
        log.debug("Request to update CenterWatchList : {}", centerWatchListDTO)
        var centerWatchList = centerWatchListMapper.toEntity(centerWatchListDTO)
        centerWatchList = centerWatchListRepository.save(centerWatchList)
        return centerWatchListMapper.toDto(centerWatchList)
    }

    override fun partialUpdate(centerWatchListDTO: CenterWatchListDTO): Optional<CenterWatchListDTO> {
        log.debug("Request to partially update CenterWatchList : {}", centerWatchListDTO)

        return centerWatchListRepository.findById(centerWatchListDTO.id)
            .map {
                centerWatchListMapper.partialUpdate(it, centerWatchListDTO)
                it
            }
            .map { centerWatchListRepository.save(it) }
            .map { centerWatchListMapper.toDto(it) }
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable): Page<CenterWatchListDTO> {
        log.debug("Request to get all CenterWatchLists")
        return centerWatchListRepository.findAll(pageable)
            .map(centerWatchListMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findOne(id: Long): Optional<CenterWatchListDTO> {
        log.debug("Request to get CenterWatchList : $id")
        return centerWatchListRepository.findById(id)
            .map(centerWatchListMapper::toDto)
    }

    override fun delete(id: Long) {
        log.debug("Request to delete CenterWatchList : $id")

        centerWatchListRepository.deleteById(id)
    }
}
