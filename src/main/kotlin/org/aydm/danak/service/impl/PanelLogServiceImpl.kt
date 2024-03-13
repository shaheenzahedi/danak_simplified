package org.aydm.danak.service.impl

import org.aydm.danak.domain.PanelLog
import org.aydm.danak.repository.PanelLogRepository
import org.aydm.danak.service.PanelLogService
import org.aydm.danak.service.dto.PanelLogDTO
import org.aydm.danak.service.mapper.PanelLogMapper
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * Service Implementation for managing [PanelLog].
 */
@Service
@Transactional
class PanelLogServiceImpl(
    private val panelLogRepository: PanelLogRepository,
    private val panelLogMapper: PanelLogMapper,
) : PanelLogService {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun save(panelLogDTO: PanelLogDTO): PanelLogDTO {
        log.debug("Request to save PanelLog : $panelLogDTO")
        var panelLog = panelLogMapper.toEntity(panelLogDTO)
        panelLog = panelLogRepository.save(panelLog)
        return panelLogMapper.toDto(panelLog)
    }

    override fun update(panelLogDTO: PanelLogDTO): PanelLogDTO {
        log.debug("Request to update PanelLog : {}", panelLogDTO)
        var panelLog = panelLogMapper.toEntity(panelLogDTO)
        panelLog = panelLogRepository.save(panelLog)
        return panelLogMapper.toDto(panelLog)
    }

    override fun partialUpdate(panelLogDTO: PanelLogDTO): Optional<PanelLogDTO> {
        log.debug("Request to partially update PanelLog : {}", panelLogDTO)

        return panelLogRepository.findById(panelLogDTO.id)
            .map {
                panelLogMapper.partialUpdate(it, panelLogDTO)
                it
            }
            .map { panelLogRepository.save(it) }
            .map { panelLogMapper.toDto(it) }
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable): Page<PanelLogDTO> {
        log.debug("Request to get all PanelLogs")
        return panelLogRepository.findAll(pageable)
            .map(panelLogMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findOne(id: Long): Optional<PanelLogDTO> {
        log.debug("Request to get PanelLog : $id")
        return panelLogRepository.findById(id)
            .map(panelLogMapper::toDto)
    }

    override fun delete(id: Long) {
        log.debug("Request to delete PanelLog : $id")

        panelLogRepository.deleteById(id)
    }
}
