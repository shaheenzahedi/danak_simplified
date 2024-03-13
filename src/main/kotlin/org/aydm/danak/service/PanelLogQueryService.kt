package org.aydm.danak.service

import org.aydm.danak.domain.*
import org.aydm.danak.repository.PanelLogRepository
import org.aydm.danak.service.criteria.PanelLogCriteria
import org.aydm.danak.service.dto.PanelLogDTO
import org.aydm.danak.service.mapper.PanelLogMapper
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tech.jhipster.service.QueryService
import tech.jhipster.service.filter.Filter
import javax.persistence.criteria.JoinType

/**
 * Service for executing complex queries for [PanelLog] entities in the database.
 * The main input is a [PanelLogCriteria] which gets converted to [Specification],
 * in a way that all the filters must apply.
 * It returns a [MutableList] of [PanelLogDTO] or a [Page] of [PanelLogDTO] which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
class PanelLogQueryService(
    private val panelLogRepository: PanelLogRepository,
    private val panelLogMapper: PanelLogMapper,
) : QueryService<PanelLog>() {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Return a [MutableList] of [PanelLogDTO] which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    fun findByCriteria(criteria: PanelLogCriteria?): MutableList<PanelLogDTO> {
        log.debug("find by criteria : $criteria")
        val specification = createSpecification(criteria)
        return panelLogMapper.toDto(panelLogRepository.findAll(specification))
    }

    /**
     * Return a [Page] of [PanelLogDTO] which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    fun findByCriteria(criteria: PanelLogCriteria?, page: Pageable): Page<PanelLogDTO> {
        log.debug("find by criteria : $criteria, page: $page")
        val specification = createSpecification(criteria)
        return panelLogRepository.findAll(specification, page)
            .map(panelLogMapper::toDto)
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    fun countByCriteria(criteria: PanelLogCriteria?): Long {
        log.debug("count by criteria : $criteria")
        val specification = createSpecification(criteria)
        return panelLogRepository.count(specification)
    }

    /**
     * Function to convert [PanelLogCriteria] to a [Specification].
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching [Specification] of the entity.
     */
    protected fun createSpecification(criteria: PanelLogCriteria?): Specification<PanelLog?> {
        var specification: Specification<PanelLog?> = Specification.where(null)
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            val distinctCriteria = criteria.distinct
            if (distinctCriteria != null) {
                specification = specification.and(distinct(distinctCriteria))
            }
            if (criteria.id != null) {
                specification = specification.and(buildRangeSpecification(criteria.id, PanelLog_.id))
            }
            if (criteria.createTimeStamp != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.createTimeStamp, PanelLog_.createTimeStamp))
            }
            if (criteria.panelLogType != null) {
                specification = specification.and(buildSpecification(criteria.panelLogType, PanelLog_.panelLogType))
            }
            if (criteria.userId != null) {
                specification = specification.and(
                    buildSpecification(criteria.userId as Filter<Long>) {
                        it.join(PanelLog_.user, JoinType.LEFT).get(User_.id)
                    }
                )
            }
        }
        return specification
    }
}
