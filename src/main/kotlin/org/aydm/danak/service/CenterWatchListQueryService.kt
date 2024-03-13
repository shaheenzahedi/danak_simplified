package org.aydm.danak.service

import org.aydm.danak.domain.*
import org.aydm.danak.repository.CenterWatchListRepository
import org.aydm.danak.service.criteria.CenterWatchListCriteria
import org.aydm.danak.service.dto.CenterWatchListDTO
import org.aydm.danak.service.mapper.CenterWatchListMapper
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
 * Service for executing complex queries for [CenterWatchList] entities in the database.
 * The main input is a [CenterWatchListCriteria] which gets converted to [Specification],
 * in a way that all the filters must apply.
 * It returns a [MutableList] of [CenterWatchListDTO] or a [Page] of [CenterWatchListDTO] which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
class CenterWatchListQueryService(
    private val centerWatchListRepository: CenterWatchListRepository,
    private val centerWatchListMapper: CenterWatchListMapper,
) : QueryService<CenterWatchList>() {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Return a [MutableList] of [CenterWatchListDTO] which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    fun findByCriteria(criteria: CenterWatchListCriteria?): MutableList<CenterWatchListDTO> {
        log.debug("find by criteria : $criteria")
        val specification = createSpecification(criteria)
        return centerWatchListMapper.toDto(centerWatchListRepository.findAll(specification))
    }

    /**
     * Return a [Page] of [CenterWatchListDTO] which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    fun findByCriteria(criteria: CenterWatchListCriteria?, page: Pageable): Page<CenterWatchListDTO> {
        log.debug("find by criteria : $criteria, page: $page")
        val specification = createSpecification(criteria)
        return centerWatchListRepository.findAll(specification, page)
            .map(centerWatchListMapper::toDto)
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    fun countByCriteria(criteria: CenterWatchListCriteria?): Long {
        log.debug("count by criteria : $criteria")
        val specification = createSpecification(criteria)
        return centerWatchListRepository.count(specification)
    }

    /**
     * Function to convert [CenterWatchListCriteria] to a [Specification].
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching [Specification] of the entity.
     */
    protected fun createSpecification(criteria: CenterWatchListCriteria?): Specification<CenterWatchList?> {
        var specification: Specification<CenterWatchList?> = Specification.where(null)
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            val distinctCriteria = criteria.distinct
            if (distinctCriteria != null) {
                specification = specification.and(distinct(distinctCriteria))
            }
            if (criteria.id != null) {
                specification = specification.and(buildRangeSpecification(criteria.id, CenterWatchList_.id))
            }
            if (criteria.centerId != null) {
                specification = specification.and(
                    buildSpecification(criteria.centerId as Filter<Long>) {
                        it.join(CenterWatchList_.center, JoinType.LEFT).get(Center_.id)
                    }
                )
            }
            if (criteria.userId != null) {
                specification = specification.and(
                    buildSpecification(criteria.userId as Filter<Long>) {
                        it.join(CenterWatchList_.user, JoinType.LEFT).get(User_.id)
                    }
                )
            }
        }
        return specification
    }
}
