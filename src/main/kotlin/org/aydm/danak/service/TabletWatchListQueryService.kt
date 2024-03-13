package org.aydm.danak.service

import org.aydm.danak.domain.*
import org.aydm.danak.repository.TabletWatchListRepository
import org.aydm.danak.service.criteria.TabletWatchListCriteria
import org.aydm.danak.service.dto.TabletWatchListDTO
import org.aydm.danak.service.mapper.TabletWatchListMapper
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
 * Service for executing complex queries for [TabletWatchList] entities in the database.
 * The main input is a [TabletWatchListCriteria] which gets converted to [Specification],
 * in a way that all the filters must apply.
 * It returns a [MutableList] of [TabletWatchListDTO] or a [Page] of [TabletWatchListDTO] which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
class TabletWatchListQueryService(
    private val tabletWatchListRepository: TabletWatchListRepository,
    private val tabletWatchListMapper: TabletWatchListMapper,
) : QueryService<TabletWatchList>() {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Return a [MutableList] of [TabletWatchListDTO] which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    fun findByCriteria(criteria: TabletWatchListCriteria?): MutableList<TabletWatchListDTO> {
        log.debug("find by criteria : $criteria")
        val specification = createSpecification(criteria)
        return tabletWatchListMapper.toDto(tabletWatchListRepository.findAll(specification))
    }

    /**
     * Return a [Page] of [TabletWatchListDTO] which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    fun findByCriteria(criteria: TabletWatchListCriteria?, page: Pageable): Page<TabletWatchListDTO> {
        log.debug("find by criteria : $criteria, page: $page")
        val specification = createSpecification(criteria)
        return tabletWatchListRepository.findAll(specification, page)
            .map(tabletWatchListMapper::toDto)
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    fun countByCriteria(criteria: TabletWatchListCriteria?): Long {
        log.debug("count by criteria : $criteria")
        val specification = createSpecification(criteria)
        return tabletWatchListRepository.count(specification)
    }

    /**
     * Function to convert [TabletWatchListCriteria] to a [Specification].
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching [Specification] of the entity.
     */
    protected fun createSpecification(criteria: TabletWatchListCriteria?): Specification<TabletWatchList?> {
        var specification: Specification<TabletWatchList?> = Specification.where(null)
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            val distinctCriteria = criteria.distinct
            if (distinctCriteria != null) {
                specification = specification.and(distinct(distinctCriteria))
            }
            if (criteria.id != null) {
                specification = specification.and(buildRangeSpecification(criteria.id, TabletWatchList_.id))
            }
            if (criteria.tabletId != null) {
                specification = specification.and(
                    buildSpecification(criteria.tabletId as Filter<Long>) {
                        it.join(TabletWatchList_.tablet, JoinType.LEFT).get(Tablet_.id)
                    }
                )
            }
            if (criteria.userId != null) {
                specification = specification.and(
                    buildSpecification(criteria.userId as Filter<Long>) {
                        it.join(TabletWatchList_.user, JoinType.LEFT).get(User_.id)
                    }
                )
            }
        }
        return specification
    }
}
