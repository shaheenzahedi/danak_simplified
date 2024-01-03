package org.aydm.danak.service

import org.aydm.danak.domain.* // for static metamodels
import org.aydm.danak.domain.Donor
import org.aydm.danak.repository.DonorRepository
import org.aydm.danak.service.criteria.DonorCriteria
import org.aydm.danak.service.dto.DonorDTO
import org.aydm.danak.service.mapper.DonorMapper
import org.aydm.danak.service.mapper.UserMapper
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
 * Service for executing complex queries for [Donor] entities in the database.
 * The main input is a [DonorCriteria] which gets converted to [Specification],
 * in a way that all the filters must apply.
 * It returns a [MutableList] of [DonorDTO] or a [Page] of [DonorDTO] which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
class DonorQueryService(
    private val donorRepository: DonorRepository,
    private val donorMapper: DonorMapper,
    private val userMapper: UserMapper
) : QueryService<Donor>() {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Return a [MutableList] of [DonorDTO] which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    fun findByCriteria(criteria: DonorCriteria?): MutableList<DonorDTO> {
        log.debug("find by criteria : $criteria")
        val specification = createSpecification(criteria)
        return donorMapper.toDto(donorRepository.findAll(specification))
    }

    /**
     * Return a [Page] of [DonorDTO] which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    fun findByCriteria(criteria: DonorCriteria?, page: Pageable): Page<DonorDTO> {
        log.debug("find by criteria : $criteria, page: $page")
        val specification = createSpecification(criteria)
        return donorRepository.findAll(specification, page)
            .map(donorMapper::toDto)
    }

    @Transactional(readOnly = true)
    fun findByCriteriaPanel(criteria: DonorCriteria, page: Pageable): Page<DonorDTO> {
        log.debug("find by criteria : $criteria, page: $page")
        val specification = createSpecification(criteria)
        return donorRepository.findAll(specification, page)
            .map {
                val dto = donorMapper.toDto(it)
                dto.user= it.user?.let { userEntity -> userMapper.userToUserDTO(userEntity) }
                dto
            }

    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    fun countByCriteria(criteria: DonorCriteria?): Long {
        log.debug("count by criteria : $criteria")
        val specification = createSpecification(criteria)
        return donorRepository.count(specification)
    }

    /**
     * Function to convert [DonorCriteria] to a [Specification].
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching [Specification] of the entity.
     */
    protected fun createSpecification(criteria: DonorCriteria?): Specification<Donor?> {
        var specification: Specification<Donor?> = Specification.where(null)
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            val distinctCriteria = criteria.distinct
            if (distinctCriteria != null) {
                specification = specification.and(distinct(distinctCriteria))
            }
            if (criteria.id != null) {
                specification = specification.and(buildRangeSpecification(criteria.id, Donor_.id))
            }
            if (criteria.createTimeStamp != null) {
                specification = specification.and(buildRangeSpecification(criteria.createTimeStamp, Donor_.createTimeStamp))
            }
            if (criteria.updateTimeStamp != null) {
                specification = specification.and(buildRangeSpecification(criteria.updateTimeStamp, Donor_.updateTimeStamp))
            }
            if (criteria.name != null) {
                specification = specification.and(buildStringSpecification(criteria.name, Donor_.name))
            }
            if (criteria.city != null) {
                specification = specification.and(buildStringSpecification(criteria.city, Donor_.city))
            }
            if (criteria.country != null) {
                specification = specification.and(buildStringSpecification(criteria.country, Donor_.country))
            }
            if (criteria.userId != null) {
                specification = specification.and(
                    buildSpecification(criteria.userId as Filter<Long>) {
                        it.join(Donor_.user, JoinType.LEFT).get(User_.id)
                    }
                )
            }
            if (criteria.tabletId != null) {
                specification = specification.and(
                    buildSpecification(criteria.tabletId as Filter<Long>) {
                        it.join(Donor_.tablets, JoinType.LEFT).get(Tablet_.id)
                    }
                )
            }
        }
        return specification
    }
}
