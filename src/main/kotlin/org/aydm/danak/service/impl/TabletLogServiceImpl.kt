package org.aydm.danak.service.impl

import org.aydm.danak.domain.TabletLog
import org.aydm.danak.repository.TabletLogRepository
import org.aydm.danak.service.TabletLogService
import org.aydm.danak.service.dto.TabletLogDTO
import org.aydm.danak.service.mapper.TabletLogMapper
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * Service Implementation for managing [TabletLog].
 */
@Service
@Transactional
class TabletLogServiceImpl(
    private val tabletLogRepository: TabletLogRepository,
    private val tabletLogMapper: TabletLogMapper,
) : TabletLogService {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun save(tabletLogDTO: TabletLogDTO): TabletLogDTO {
        log.debug("Request to save TabletLog : $tabletLogDTO")
        var tabletLog = tabletLogMapper.toEntity(tabletLogDTO)
        tabletLog = tabletLogRepository.save(tabletLog)
        return tabletLogMapper.toDto(tabletLog)
    }

    override fun update(tabletLogDTO: TabletLogDTO): TabletLogDTO {
        log.debug("Request to update TabletLog : {}", tabletLogDTO)
        var tabletLog = tabletLogMapper.toEntity(tabletLogDTO)
        tabletLog = tabletLogRepository.save(tabletLog)
        return tabletLogMapper.toDto(tabletLog)
    }

    override fun partialUpdate(tabletLogDTO: TabletLogDTO): Optional<TabletLogDTO> {
        log.debug("Request to partially update TabletLog : {}", tabletLogDTO)

        return tabletLogRepository.findById(tabletLogDTO.id)
            .map {
                tabletLogMapper.partialUpdate(it, tabletLogDTO)
                it
            }
            .map { tabletLogRepository.save(it) }
            .map { tabletLogMapper.toDto(it) }
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable): Page<TabletLogDTO> {
        log.debug("Request to get all TabletLogs")
        return tabletLogRepository.findAll(pageable)
            .map(tabletLogMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findOne(id: Long): Optional<TabletLogDTO> {
        log.debug("Request to get TabletLog : $id")
        return tabletLogRepository.findById(id)
            .map(tabletLogMapper::toDto)
    }

    override fun delete(id: Long) {
        log.debug("Request to delete TabletLog : $id")

        tabletLogRepository.deleteById(id)
    }
}
