package org.aydm.danak.service

import org.aydm.danak.domain.* // for static metamodels
import org.aydm.danak.domain.Tablet
import org.aydm.danak.repository.TabletRepository
import org.aydm.danak.service.criteria.TabletCriteria
import org.aydm.danak.service.dto.TabletDTO
import org.aydm.danak.service.mapper.TabletMapper
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tech.jhipster.service.QueryService
import tech.jhipster.service.filter.Filter
import tech.jhipster.service.filter.LongFilter
import javax.persistence.criteria.JoinType

/**
 * Service for executing complex queries for [Tablet] entities in the database.
 * The main input is a [TabletCriteria] which gets converted to [Specification],
 * in a way that all the filters must apply.
 * It returns a [MutableList] of [TabletDTO] or a [Page] of [TabletDTO] which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
class TabletQueryService(
    private val tabletRepository: TabletRepository,
    private val tabletMapper: TabletMapper,
) : QueryService<Tablet>() {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Return a [MutableList] of [TabletDTO] which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    fun findByCriteria(criteria: TabletCriteria?): MutableList<TabletDTO> {
        log.debug("find by criteria : $criteria")
        val specification = createSpecification(criteria)
        return tabletMapper.toDto(tabletRepository.findAll(specification))
    }

    @Transactional(readOnly = true)
    fun findByCriteriaByDonorId(criteria: TabletCriteria?, pageable: Pageable, donorId: Long?): Page<TabletDTO> {
        if (donorId == null) return findByCriteria(criteria, pageable)
        val tabletIds = tabletRepository.findAllTabletIdsByDonorId(donorId)
        val cr = criteria ?: TabletCriteria()
        cr.id = LongFilter().apply { `in` = tabletIds }
        return findByCriteria(cr, pageable)
    }

    /**
     * Return a [Page] of [TabletDTO] which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    fun findByCriteria(criteria: TabletCriteria?, page: Pageable): Page<TabletDTO> {
        log.debug("find by criteria : $criteria, page: $page")
        val specification = createSpecification(criteria)
        return tabletRepository.findAll(specification, page)
            .map(tabletMapper::toDto)
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    fun countByCriteria(criteria: TabletCriteria?): Long {
        log.debug("count by criteria : $criteria")
        val specification = createSpecification(criteria)
        return tabletRepository.count(specification)
    }

    /**
     * Function to convert [TabletCriteria] to a [Specification].
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching [Specification] of the entity.
     */
    protected fun createSpecification(criteria: TabletCriteria?): Specification<Tablet?> {
        var specification: Specification<Tablet?> = Specification.where(null)
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            val distinctCriteria = criteria.distinct
            if (distinctCriteria != null) {
                specification = specification.and(distinct(distinctCriteria))
            }
            if (criteria.id != null) {
                specification = specification.and(buildRangeSpecification(criteria.id, Tablet_.id))
            }
            if (criteria.createTimeStamp != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.createTimeStamp, Tablet_.createTimeStamp))
            }
            if (criteria.updateTimeStamp != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.updateTimeStamp, Tablet_.updateTimeStamp))
            }
            if (criteria.name != null) {
                specification = specification.and(buildStringSpecification(criteria.name, Tablet_.name))
            }
            if (criteria.identifier != null) {
                specification = specification.and(buildStringSpecification(criteria.identifier, Tablet_.identifier))
            }
            if (criteria.model != null) {
                specification = specification.and(buildStringSpecification(criteria.model, Tablet_.model))
            }
            if (criteria.tabletUserId != null) {
                specification = specification.and(
                    buildSpecification(criteria.tabletUserId as Filter<Long>) {
                        it.join(Tablet_.tabletUsers, JoinType.LEFT).get(TabletUser_.id)
                    }
                )
            }
            if (criteria.centerId != null) {
                specification = specification.and(
                    buildSpecification(criteria.centerId as Filter<Long>) {
                        it.join(Tablet_.center, JoinType.LEFT).get(Center_.id)
                    }
                )
            }
            if (criteria.donorId != null) {
                specification = specification.and(
                    buildSpecification(criteria.donorId as Filter<Long>) {
                        it.join(Tablet_.donor, JoinType.LEFT).get(Donor_.id)
                    }
                )
            }
            if (criteria.searchField != null) {
                specification = specification.and(
                    Specification.where(
                        buildStringSpecification(criteria.searchField, Tablet_.name)
                            .or(buildStringSpecification(criteria.searchField, Tablet_.identifier))
                            .or(buildStringSpecification(criteria.searchField, Tablet_.model))
                    )
                )
            }
        }
        return specification
    }
}
