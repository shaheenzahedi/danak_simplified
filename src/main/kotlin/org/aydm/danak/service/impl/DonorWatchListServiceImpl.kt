package org.aydm.danak.service.impl

import org.aydm.danak.domain.DonorWatchList
import org.aydm.danak.repository.DonorWatchListRepository
import org.aydm.danak.service.DonorWatchListService
import org.aydm.danak.service.dto.DonorWatchListDTO
import org.aydm.danak.service.mapper.DonorWatchListMapper
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * Service Implementation for managing [DonorWatchList].
 */
@Service
@Transactional
class DonorWatchListServiceImpl(
    private val donorWatchListRepository: DonorWatchListRepository,
    private val donorWatchListMapper: DonorWatchListMapper,
) : DonorWatchListService {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun save(donorWatchListDTO: DonorWatchListDTO): DonorWatchListDTO {
        log.debug("Request to save DonorWatchList : $donorWatchListDTO")
        var donorWatchList = donorWatchListMapper.toEntity(donorWatchListDTO)
        donorWatchList = donorWatchListRepository.save(donorWatchList)
        return donorWatchListMapper.toDto(donorWatchList)
    }

    override fun update(donorWatchListDTO: DonorWatchListDTO): DonorWatchListDTO {
        log.debug("Request to update DonorWatchList : {}", donorWatchListDTO)
        var donorWatchList = donorWatchListMapper.toEntity(donorWatchListDTO)
        donorWatchList = donorWatchListRepository.save(donorWatchList)
        return donorWatchListMapper.toDto(donorWatchList)
    }

    override fun partialUpdate(donorWatchListDTO: DonorWatchListDTO): Optional<DonorWatchListDTO> {
        log.debug("Request to partially update DonorWatchList : {}", donorWatchListDTO)

        return donorWatchListRepository.findById(donorWatchListDTO.id)
            .map {
                donorWatchListMapper.partialUpdate(it, donorWatchListDTO)
                it
            }
            .map { donorWatchListRepository.save(it) }
            .map { donorWatchListMapper.toDto(it) }
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable): Page<DonorWatchListDTO> {
        log.debug("Request to get all DonorWatchLists")
        return donorWatchListRepository.findAll(pageable)
            .map(donorWatchListMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findOne(id: Long): Optional<DonorWatchListDTO> {
        log.debug("Request to get DonorWatchList : $id")
        return donorWatchListRepository.findById(id)
            .map(donorWatchListMapper::toDto)
    }

    override fun delete(id: Long) {
        log.debug("Request to delete DonorWatchList : $id")

        donorWatchListRepository.deleteById(id)
    }
}
