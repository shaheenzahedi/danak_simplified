package org.aydm.danak.service

import org.aydm.danak.domain.* // for static metamodels
import org.aydm.danak.domain.Center
import org.aydm.danak.repository.CenterRepository
import org.aydm.danak.service.criteria.CenterCriteria
import org.aydm.danak.service.dto.CenterDTO
import org.aydm.danak.service.mapper.CenterMapper
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
 * Service for executing complex queries for [Center] entities in the database.
 * The main input is a [CenterCriteria] which gets converted to [Specification],
 * in a way that all the filters must apply.
 * It returns a [MutableList] of [CenterDTO] or a [Page] of [CenterDTO] which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
class CenterQueryService(
    private val centerRepository: CenterRepository,
    private val centerMapper: CenterMapper,
) : QueryService<Center>() {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Return a [MutableList] of [CenterDTO] which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    fun findByCriteria(criteria: CenterCriteria?): MutableList<CenterDTO> {
        log.debug("find by criteria : $criteria")
        val specification = createSpecification(criteria)
        return centerMapper.toDto(centerRepository.findAll(specification))
    }

    /**
     * Return a [Page] of [CenterDTO] which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    fun findByCriteria(criteria: CenterCriteria?, page: Pageable): Page<CenterDTO> {
        log.debug("find by criteria : $criteria, page: $page")
        val specification = createSpecification(criteria)
        return centerRepository.findAll(specification, page)
            .map(centerMapper::toDto)
    }

    fun findByCriteriaPanel(criteria: CenterCriteria, page: Pageable): Page<CenterDTO> {
        log.debug("find by criteria : $criteria, page: $page")
        val specification = createSpecification(criteria)
        return centerRepository.findAll(specification, page)
            .map { centerMapper.toDto(it).apply { tabletCount = it.tablets?.size ?: 0 } }
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    fun countByCriteria(criteria: CenterCriteria?): Long {
        log.debug("count by criteria : $criteria")
        val specification = createSpecification(criteria)
        return centerRepository.count(specification)
    }

    /**
     * Function to convert [CenterCriteria] to a [Specification].
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching [Specification] of the entity.
     */
    protected fun createSpecification(criteria: CenterCriteria?): Specification<Center?> {
        var specification: Specification<Center?> = Specification.where(null)
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            val distinctCriteria = criteria.distinct
            if (distinctCriteria != null) {
                specification = specification.and(distinct(distinctCriteria))
            }
            if (criteria.id != null) {
                specification = specification.and(buildRangeSpecification(criteria.id, Center_.id))
            }
            if (criteria.createTimeStamp != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.createTimeStamp, Center_.createTimeStamp))
            }
            if (criteria.updateTimeStamp != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.updateTimeStamp, Center_.updateTimeStamp))
            }
            if (criteria.name != null) {
                specification = specification.and(buildStringSpecification(criteria.name, Center_.name))
            }
            if (criteria.city != null) {
                specification = specification.and(buildStringSpecification(criteria.city, Center_.city))
            }
            if (criteria.country != null) {
                specification = specification.and(buildStringSpecification(criteria.country, Center_.country))
            }
            if (criteria.archived != null) {
                specification = specification.and(buildSpecification(criteria.archived, Center_.archived))
            }
            if (criteria.centerType != null) {
                specification = specification.and(buildSpecification(criteria.centerType, Center_.centerType))
            }
            if (criteria.latitude != null) {
                specification = specification.and(buildRangeSpecification(criteria.latitude, Center_.latitude))
            }
            if (criteria.longitude != null) {
                specification = specification.and(buildRangeSpecification(criteria.longitude, Center_.longitude))
            }
            if (criteria.centerDonorId != null) {
                specification = specification.and(
                    buildSpecification(criteria.centerDonorId as Filter<Long>) {
                        it.join(Center_.centerDonors, JoinType.LEFT).get(CenterDonor_.id)
                    }
                )
            }
            if (criteria.centerImageId != null) {
                specification = specification.and(
                    buildSpecification(criteria.centerImageId as Filter<Long>) {
                        it.join(Center_.centerImages, JoinType.LEFT).get(CenterImage_.id)
                    }
                )
            }
            if (criteria.centerWatchListId != null) {
                specification = specification.and(
                    buildSpecification(criteria.centerWatchListId as Filter<Long>) {
                        it.join(Center_.centerWatchLists, JoinType.LEFT).get(CenterWatchList_.id)
                    }
                )
            }
            if (criteria.tabletId != null) {
                specification = specification.and(
                    buildSpecification(criteria.tabletId as Filter<Long>) {
                        it.join(Center_.tablets, JoinType.LEFT).get(Tablet_.id)
                    }
                )
            }
            if (criteria.archivedById != null) {
                specification = specification.and(
                    buildSpecification(criteria.archivedById as Filter<Long>) {
                        it.join(Center_.archivedBy, JoinType.LEFT).get(User_.id)
                    }
                )
            }
            if (criteria.createdById != null) {
                specification = specification.and(
                    buildSpecification(criteria.createdById as Filter<Long>) {
                        it.join(Center_.createdBy, JoinType.LEFT).get(User_.id)
                    }
                )
            }
            if (criteria.modifiedById != null) {
                specification = specification.and(
                    buildSpecification(criteria.modifiedById as Filter<Long>) {
                        it.join(Center_.modifiedBy, JoinType.LEFT).get(User_.id)
                    }
                )
            }
        }
        return specification
    }
}
