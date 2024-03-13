package org.aydm.danak.service

import org.aydm.danak.domain.*
import org.aydm.danak.repository.TabletLogRepository
import org.aydm.danak.service.criteria.TabletLogCriteria
import org.aydm.danak.service.dto.TabletLogDTO
import org.aydm.danak.service.mapper.TabletLogMapper
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
 * Service for executing complex queries for [TabletLog] entities in the database.
 * The main input is a [TabletLogCriteria] which gets converted to [Specification],
 * in a way that all the filters must apply.
 * It returns a [MutableList] of [TabletLogDTO] or a [Page] of [TabletLogDTO] which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
class TabletLogQueryService(
    private val tabletLogRepository: TabletLogRepository,
    private val tabletLogMapper: TabletLogMapper,
) : QueryService<TabletLog>() {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Return a [MutableList] of [TabletLogDTO] which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    fun findByCriteria(criteria: TabletLogCriteria?): MutableList<TabletLogDTO> {
        log.debug("find by criteria : $criteria")
        val specification = createSpecification(criteria)
        return tabletLogMapper.toDto(tabletLogRepository.findAll(specification))
    }

    /**
     * Return a [Page] of [TabletLogDTO] which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    fun findByCriteria(criteria: TabletLogCriteria?, page: Pageable): Page<TabletLogDTO> {
        log.debug("find by criteria : $criteria, page: $page")
        val specification = createSpecification(criteria)
        return tabletLogRepository.findAll(specification, page)
            .map(tabletLogMapper::toDto)
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    fun countByCriteria(criteria: TabletLogCriteria?): Long {
        log.debug("count by criteria : $criteria")
        val specification = createSpecification(criteria)
        return tabletLogRepository.count(specification)
    }

    /**
     * Function to convert [TabletLogCriteria] to a [Specification].
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching [Specification] of the entity.
     */
    protected fun createSpecification(criteria: TabletLogCriteria?): Specification<TabletLog?> {
        var specification: Specification<TabletLog?> = Specification.where(null)
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            val distinctCriteria = criteria.distinct
            if (distinctCriteria != null) {
                specification = specification.and(distinct(distinctCriteria))
            }
            if (criteria.id != null) {
                specification = specification.and(buildRangeSpecification(criteria.id, TabletLog_.id))
            }
            if (criteria.createTimeStamp != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.createTimeStamp, TabletLog_.createTimeStamp))
            }
            if (criteria.tabletId != null) {
                specification = specification.and(
                    buildSpecification(criteria.tabletId as Filter<Long>) {
                        it.join(TabletLog_.tablet, JoinType.LEFT).get(Tablet_.id)
                    }
                )
            }
        }
        return specification
    }
}
