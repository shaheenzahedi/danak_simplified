package org.aydm.danak.service

import org.aydm.danak.domain.*
import org.aydm.danak.repository.DonorWatchListRepository
import org.aydm.danak.service.criteria.DonorWatchListCriteria
import org.aydm.danak.service.dto.DonorWatchListDTO
import org.aydm.danak.service.mapper.DonorWatchListMapper
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
 * Service for executing complex queries for [DonorWatchList] entities in the database.
 * The main input is a [DonorWatchListCriteria] which gets converted to [Specification],
 * in a way that all the filters must apply.
 * It returns a [MutableList] of [DonorWatchListDTO] or a [Page] of [DonorWatchListDTO] which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
class DonorWatchListQueryService(
    private val donorWatchListRepository: DonorWatchListRepository,
    private val donorWatchListMapper: DonorWatchListMapper,
) : QueryService<DonorWatchList>() {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Return a [MutableList] of [DonorWatchListDTO] which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    fun findByCriteria(criteria: DonorWatchListCriteria?): MutableList<DonorWatchListDTO> {
        log.debug("find by criteria : $criteria")
        val specification = createSpecification(criteria)
        return donorWatchListMapper.toDto(donorWatchListRepository.findAll(specification))
    }

    /**
     * Return a [Page] of [DonorWatchListDTO] which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    fun findByCriteria(criteria: DonorWatchListCriteria?, page: Pageable): Page<DonorWatchListDTO> {
        log.debug("find by criteria : $criteria, page: $page")
        val specification = createSpecification(criteria)
        return donorWatchListRepository.findAll(specification, page)
            .map(donorWatchListMapper::toDto)
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    fun countByCriteria(criteria: DonorWatchListCriteria?): Long {
        log.debug("count by criteria : $criteria")
        val specification = createSpecification(criteria)
        return donorWatchListRepository.count(specification)
    }

    /**
     * Function to convert [DonorWatchListCriteria] to a [Specification].
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching [Specification] of the entity.
     */
    protected fun createSpecification(criteria: DonorWatchListCriteria?): Specification<DonorWatchList?> {
        var specification: Specification<DonorWatchList?> = Specification.where(null)
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            val distinctCriteria = criteria.distinct
            if (distinctCriteria != null) {
                specification = specification.and(distinct(distinctCriteria))
            }
            if (criteria.id != null) {
                specification = specification.and(buildRangeSpecification(criteria.id, DonorWatchList_.id))
            }
            if (criteria.donorId != null) {
                specification = specification.and(
                    buildSpecification(criteria.donorId as Filter<Long>) {
                        it.join(DonorWatchList_.donor, JoinType.LEFT).get(Donor_.id)
                    }
                )
            }
            if (criteria.userId != null) {
                specification = specification.and(
                    buildSpecification(criteria.userId as Filter<Long>) {
                        it.join(DonorWatchList_.user, JoinType.LEFT).get(User_.id)
                    }
                )
            }
        }
        return specification
    }
}
