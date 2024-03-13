package org.aydm.danak.service

import org.aydm.danak.domain.*
import org.aydm.danak.repository.TabletUserWatchListRepository
import org.aydm.danak.service.criteria.TabletUserWatchListCriteria
import org.aydm.danak.service.dto.TabletUserWatchListDTO
import org.aydm.danak.service.mapper.TabletUserWatchListMapper
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
 * Service for executing complex queries for [TabletUserWatchList] entities in the database.
 * The main input is a [TabletUserWatchListCriteria] which gets converted to [Specification],
 * in a way that all the filters must apply.
 * It returns a [MutableList] of [TabletUserWatchListDTO] or a [Page] of [TabletUserWatchListDTO] which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
class TabletUserWatchListQueryService(
    private val tabletUserWatchListRepository: TabletUserWatchListRepository,
    private val tabletUserWatchListMapper: TabletUserWatchListMapper,
) : QueryService<TabletUserWatchList>() {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Return a [MutableList] of [TabletUserWatchListDTO] which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    fun findByCriteria(criteria: TabletUserWatchListCriteria?): MutableList<TabletUserWatchListDTO> {
        log.debug("find by criteria : $criteria")
        val specification = createSpecification(criteria)
        return tabletUserWatchListMapper.toDto(tabletUserWatchListRepository.findAll(specification))
    }

    /**
     * Return a [Page] of [TabletUserWatchListDTO] which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    fun findByCriteria(criteria: TabletUserWatchListCriteria?, page: Pageable): Page<TabletUserWatchListDTO> {
        log.debug("find by criteria : $criteria, page: $page")
        val specification = createSpecification(criteria)
        return tabletUserWatchListRepository.findAll(specification, page)
            .map(tabletUserWatchListMapper::toDto)
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    fun countByCriteria(criteria: TabletUserWatchListCriteria?): Long {
        log.debug("count by criteria : $criteria")
        val specification = createSpecification(criteria)
        return tabletUserWatchListRepository.count(specification)
    }

    /**
     * Function to convert [TabletUserWatchListCriteria] to a [Specification].
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching [Specification] of the entity.
     */
    protected fun createSpecification(criteria: TabletUserWatchListCriteria?): Specification<TabletUserWatchList?> {
        var specification: Specification<TabletUserWatchList?> = Specification.where(null)
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            val distinctCriteria = criteria.distinct
            if (distinctCriteria != null) {
                specification = specification.and(distinct(distinctCriteria))
            }
            if (criteria.id != null) {
                specification = specification.and(buildRangeSpecification(criteria.id, TabletUserWatchList_.id))
            }
            if (criteria.tabletUserId != null) {
                specification = specification.and(
                    buildSpecification(criteria.tabletUserId as Filter<Long>) {
                        it.join(TabletUserWatchList_.tabletUser, JoinType.LEFT).get(TabletUser_.id)
                    }
                )
            }
            if (criteria.userId != null) {
                specification = specification.and(
                    buildSpecification(criteria.userId as Filter<Long>) {
                        it.join(TabletUserWatchList_.user, JoinType.LEFT).get(User_.id)
                    }
                )
            }
        }
        return specification
    }
}
