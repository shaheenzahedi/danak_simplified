package org.aydm.danak.service

import org.aydm.danak.domain.*
import org.aydm.danak.repository.CenterDonorRepository
import org.aydm.danak.service.criteria.CenterDonorCriteria
import org.aydm.danak.service.dto.CenterDonorDTO
import org.aydm.danak.service.mapper.CenterDonorMapper
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
 * Service for executing complex queries for [CenterDonor] entities in the database.
 * The main input is a [CenterDonorCriteria] which gets converted to [Specification],
 * in a way that all the filters must apply.
 * It returns a [MutableList] of [CenterDonorDTO] or a [Page] of [CenterDonorDTO] which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
class CenterDonorQueryService(
    private val centerDonorRepository: CenterDonorRepository,
    private val centerDonorMapper: CenterDonorMapper,
) : QueryService<CenterDonor>() {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Return a [MutableList] of [CenterDonorDTO] which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    fun findByCriteria(criteria: CenterDonorCriteria?): MutableList<CenterDonorDTO> {
        log.debug("find by criteria : $criteria")
        val specification = createSpecification(criteria)
        return centerDonorMapper.toDto(centerDonorRepository.findAll(specification))
    }

    /**
     * Return a [Page] of [CenterDonorDTO] which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    fun findByCriteria(criteria: CenterDonorCriteria?, page: Pageable): Page<CenterDonorDTO> {
        log.debug("find by criteria : $criteria, page: $page")
        val specification = createSpecification(criteria)
        return centerDonorRepository.findAll(specification, page)
            .map(centerDonorMapper::toDto)
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    fun countByCriteria(criteria: CenterDonorCriteria?): Long {
        log.debug("count by criteria : $criteria")
        val specification = createSpecification(criteria)
        return centerDonorRepository.count(specification)
    }

    /**
     * Function to convert [CenterDonorCriteria] to a [Specification].
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching [Specification] of the entity.
     */
    protected fun createSpecification(criteria: CenterDonorCriteria?): Specification<CenterDonor?> {
        var specification: Specification<CenterDonor?> = Specification.where(null)
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            val distinctCriteria = criteria.distinct
            if (distinctCriteria != null) {
                specification = specification.and(distinct(distinctCriteria))
            }
            if (criteria.id != null) {
                specification = specification.and(buildRangeSpecification(criteria.id, CenterDonor_.id))
            }
            if (criteria.joinedTimeStamp != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.joinedTimeStamp, CenterDonor_.joinedTimeStamp))
            }
            if (criteria.donorType != null) {
                specification = specification.and(buildSpecification(criteria.donorType, CenterDonor_.donorType))
            }
            if (criteria.centerId != null) {
                specification = specification.and(
                    buildSpecification(criteria.centerId as Filter<Long>) {
                        it.join(CenterDonor_.center, JoinType.LEFT).get(Center_.id)
                    }
                )
            }
            if (criteria.donorId != null) {
                specification = specification.and(
                    buildSpecification(criteria.donorId as Filter<Long>) {
                        it.join(CenterDonor_.donor, JoinType.LEFT).get(Donor_.id)
                    }
                )
            }
        }
        return specification
    }
}
