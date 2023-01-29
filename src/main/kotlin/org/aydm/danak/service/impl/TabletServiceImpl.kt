package org.aydm.danak.service.impl

import liquibase.pro.packaged.it
import org.aydm.danak.domain.Tablet
import org.aydm.danak.repository.TabletRepository
import org.aydm.danak.service.TabletService
import org.aydm.danak.service.dto.TabletDTO
import org.aydm.danak.service.mapper.TabletMapper
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
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
    override fun createSave(tabletName: String): TabletDTO {
        val tablet = tabletRepository.findByName(tabletName).orElse(null)
        if (tablet != null) return tabletMapper.toDto(tablet)
        return save(TabletDTO(name = tabletName))
    }
}
