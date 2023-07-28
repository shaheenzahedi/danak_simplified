package org.aydm.danak.service.impl

import org.aydm.danak.domain.TabletUser
import org.aydm.danak.repository.TabletUserRepository
import org.aydm.danak.service.TabletUserService
import org.aydm.danak.service.dto.OverallUserActivities
import org.aydm.danak.service.dto.TabletUserDTO
import org.aydm.danak.service.mapper.TabletUserMapper
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.Optional

/**
 * Service Implementation for managing [TabletUser].
 */
@Service
@Transactional
class TabletUserServiceImpl(
    private val tabletUserRepository: TabletUserRepository,
    private val tabletUserMapper: TabletUserMapper,
) : TabletUserService {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun save(tabletUserDTO: TabletUserDTO): TabletUserDTO {
        log.debug("Request to save TabletUser : $tabletUserDTO")
        var tabletUser = tabletUserMapper.toEntity(tabletUserDTO)
        tabletUser = tabletUserRepository.save(tabletUser)
        return tabletUserMapper.toDto(tabletUser)
    }

    override fun update(tabletUserDTO: TabletUserDTO): TabletUserDTO {
        log.debug("Request to save TabletUser : {}", tabletUserDTO)
        var tabletUser = tabletUserMapper.toEntity(tabletUserDTO)
        tabletUser = tabletUserRepository.save(tabletUser)
        return tabletUserMapper.toDto(tabletUser)
    }

    override fun partialUpdate(tabletUserDTO: TabletUserDTO): Optional<TabletUserDTO> {
        log.debug("Request to partially update TabletUser : {}", tabletUserDTO)

        return tabletUserRepository.findById(tabletUserDTO.id)
            .map {
                tabletUserMapper.partialUpdate(it, tabletUserDTO)
                it
            }
            .map { tabletUserRepository.save(it) }
            .map { tabletUserMapper.toDto(it) }
    }

    @Transactional(readOnly = true)
    override fun findAll(): MutableList<TabletUserDTO> {
        log.debug("Request to get all TabletUsers")
        return tabletUserRepository.findAll()
            .mapTo(mutableListOf(), tabletUserMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findOne(id: Long): Optional<TabletUserDTO> {
        log.debug("Request to get TabletUser : $id")
        return tabletUserRepository.findById(id)
            .map(tabletUserMapper::toDto)
    }

    override fun findAllByFirstLastNameImplicit(): MutableList<TabletUserDTO> {
        return tabletUserRepository.findAllByFirstLastNameImplicit()
            .mapTo(mutableListOf(), tabletUserMapper::toDto)
    }


    override fun createSave(tabletUserDTO: TabletUserDTO): TabletUserDTO {
        val tabletUser =
            tabletUserRepository.findByNameAndFamily(tabletUserDTO.firstName, tabletUserDTO.lastName).orElse(null)
        if (tabletUser != null) return tabletUserMapper.toDto(tabletUser)
        return save(tabletUserDTO)
    }

    override fun delete(id: Long) {
        log.debug("Request to delete TabletUser : $id")

        tabletUserRepository.deleteById(id)
    }
}
