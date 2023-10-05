package org.aydm.danak.service.impl

import org.aydm.danak.domain.Tablet
import org.aydm.danak.repository.TabletRepository
import org.aydm.danak.service.TabletService
import org.aydm.danak.service.dto.TabletDTO
import org.aydm.danak.service.mapper.TabletMapper
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.Optional

/**
 * Service Implementation for managing [Tablet].
 */
@Service
@Transactional
class TabletServiceImpl(
    private val tabletRepository: TabletRepository,
    private val tabletMapper: TabletMapper,
) : TabletService {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun save(tabletDTO: TabletDTO): TabletDTO {
        log.debug("Request to save Tablet : $tabletDTO")
        var tablet = tabletMapper.toEntity(tabletDTO)
        tablet = tabletRepository.save(tablet)
        return tabletMapper.toDto(tablet)
    }

    override fun update(tabletDTO: TabletDTO): TabletDTO {
        log.debug("Request to save Tablet : {}", tabletDTO)
        var tablet = tabletMapper.toEntity(tabletDTO)
        tablet = tabletRepository.save(tablet)
        return tabletMapper.toDto(tablet)
    }

    override fun partialUpdate(tabletDTO: TabletDTO): Optional<TabletDTO> {
        log.debug("Request to partially update Tablet : {}", tabletDTO)

        return tabletRepository.findById(tabletDTO.id)
            .map {
                tabletMapper.partialUpdate(it, tabletDTO)
                it
            }
            .map { tabletRepository.save(it) }
            .map { tabletMapper.toDto(it) }
    }

    @Transactional(readOnly = true)
    override fun findAll(): MutableList<TabletDTO> {
        log.debug("Request to get all Tablets")
        return tabletRepository.findAll()
            .mapTo(mutableListOf(), tabletMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAllRegistered(): List<Long>? {
        log.debug("Request to get all Tablets")
        return tabletRepository.findAllRegistered()
    }

    @Transactional(readOnly = true)
    override fun findOne(id: Long): Optional<TabletDTO> {
        log.debug("Request to get Tablet : $id")
        return tabletRepository.findById(id)
            .map(tabletMapper::toDto)
    }

    override fun delete(id: Long) {
        log.debug("Request to delete Tablet : $id")
        tabletRepository.deleteById(id)
    }

    @Transactional
    override fun createSave(tabletName: String, tabletId: Long?): TabletDTO {
        if (tabletId != null) return findOne(tabletId).orElse(null)
        val tablet = tabletRepository.findByName(tabletName).orElse(null)
        if (tablet != null) {
            val tabletDTO = tabletMapper.toDto(tablet)
            tabletDTO.updateTimeStamp = Instant.now()
            return tabletDTO
        }
        return save(TabletDTO(name = tabletName, createTimeStamp = Instant.now()))
    }

    override fun findAll(pageable: Pageable): Page<TabletDTO> {
        log.debug("Request to get all Tablets")
        return tabletRepository.findAll(pageable).map { tabletMapper.toDto(it) }
    }
}
