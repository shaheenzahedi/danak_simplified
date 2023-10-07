package org.aydm.danak.service

import org.aydm.danak.domain.* // for static metamodels
import org.aydm.danak.domain.TabletUser
import org.aydm.danak.repository.TabletUserRepository
import org.aydm.danak.service.criteria.TabletUserCriteria
import org.aydm.danak.service.dto.TabletUserDTO
import org.aydm.danak.service.mapper.TabletMapper
import org.aydm.danak.service.mapper.TabletUserMapper
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
 * Service for executing complex queries for [TabletUser] entities in the database.
 * The main input is a [TabletUserCriteria] which gets converted to [Specification],
 * in a way that all the filters must apply.
 * It returns a [MutableList] of [TabletUserDTO] or a [Page] of [TabletUserDTO] which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
class TabletUserQueryService(
    private val tabletUserRepository: TabletUserRepository,
    private val tabletUserMapper: TabletUserMapper,
    private val tabletMapper: TabletMapper,
) : QueryService<TabletUser>() {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Return a [MutableList] of [TabletUserDTO] which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    fun findByCriteria(criteria: TabletUserCriteria?): MutableList<TabletUserDTO> {
        log.debug("find by criteria : $criteria")
        val specification = createSpecification(criteria)
        return tabletUserMapper.toDto(tabletUserRepository.findAll(specification))
    }

    /**
     * Return a [Page] of [TabletUserDTO] which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    fun findByCriteria(criteria: TabletUserCriteria?, page: Pageable): Page<TabletUserDTO> {
        log.debug("find by criteria : $criteria, page: $page")
        val specification = createSpecification(criteria)
        return tabletUserRepository.findAll(specification, page)
            .map(tabletUserMapper::toDto)
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    fun countByCriteria(criteria: TabletUserCriteria?): Long {
        log.debug("count by criteria : $criteria")
        val specification = createSpecification(criteria)
        return tabletUserRepository.count(specification)
    }

    /**
     * Function to convert [TabletUserCriteria] to a [Specification].
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching [Specification] of the entity.
     */
    protected fun createSpecification(criteria: TabletUserCriteria?): Specification<TabletUser?> {
        var specification: Specification<TabletUser?> = Specification.where(null)
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            val distinctCriteria = criteria.distinct
            if (distinctCriteria != null) {
                specification = specification.and(distinct(distinctCriteria))
            }
            if (criteria.id != null) {
                specification = specification.and(buildRangeSpecification(criteria.id, TabletUser_.id))
            }
            if (criteria.createTimeStamp != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.createTimeStamp, TabletUser_.createTimeStamp))
            }
            if (criteria.updateTimeStamp != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.updateTimeStamp, TabletUser_.updateTimeStamp))
            }
            if (criteria.firstName != null) {
                specification = specification.and(buildStringSpecification(criteria.firstName, TabletUser_.firstName))
            }
            if (criteria.lastName != null) {
                specification = specification.and(buildStringSpecification(criteria.lastName, TabletUser_.lastName))
            }
            if (criteria.email != null) {
                specification = specification.and(buildStringSpecification(criteria.email, TabletUser_.email))
            }
            if (criteria.userActivityId != null) {
                specification = specification.and(
                    buildSpecification(criteria.userActivityId as Filter<Long>) {
                        it.join(TabletUser_.userActivities, JoinType.LEFT).get(UserActivity_.id)
                    }
                )
            }
            if (criteria.tabletId != null) {
                specification = specification.and(
                    buildSpecification(criteria.tabletId as Filter<Long>) {
                        it.join(TabletUser_.tablet, JoinType.LEFT).get(Tablet_.id)
                    }
                )
            }
        }
        return specification
    }

    fun findAll(criteria: TabletUserCriteria?, pageable: Pageable): Page<TabletUserDTO> = tabletUserRepository
        .findAll(createSpecification(criteria), pageable).map {
            tabletUserMapper.toDto(it).apply {
                tablet = tabletMapper.toDto(it.tablet!!)
            }
        }
}
