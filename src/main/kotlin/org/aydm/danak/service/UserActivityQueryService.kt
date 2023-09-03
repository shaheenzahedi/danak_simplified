package org.aydm.danak.service

import org.aydm.danak.domain.* // for static metamodels
import org.aydm.danak.domain.UserActivity
import org.aydm.danak.repository.UserActivityRepository
import org.aydm.danak.service.criteria.UserActivityCriteria
import org.aydm.danak.service.dto.UserActivityDTO
import org.aydm.danak.service.mapper.UserActivityMapper
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
 * Service for executing complex queries for [UserActivity] entities in the database.
 * The main input is a [UserActivityCriteria] which gets converted to [Specification],
 * in a way that all the filters must apply.
 * It returns a [MutableList] of [UserActivityDTO] or a [Page] of [UserActivityDTO] which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
class UserActivityQueryService(
    private val userActivityRepository: UserActivityRepository,
    private val userActivityMapper: UserActivityMapper,
) : QueryService<UserActivity>() {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Return a [MutableList] of [UserActivityDTO] which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    fun findByCriteria(criteria: UserActivityCriteria?): MutableList<UserActivityDTO> {
        log.debug("find by criteria : $criteria")
        val specification = createSpecification(criteria)
        return userActivityMapper.toDto(userActivityRepository.findAll(specification))
    }

    /**
     * Return a [Page] of [UserActivityDTO] which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    fun findByCriteria(criteria: UserActivityCriteria?, page: Pageable): Page<UserActivityDTO> {
        log.debug("find by criteria : $criteria, page: $page")
        val specification = createSpecification(criteria)
        return userActivityRepository.findAll(specification, page)
            .map(userActivityMapper::toDto)
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    fun countByCriteria(criteria: UserActivityCriteria?): Long {
        log.debug("count by criteria : $criteria")
        val specification = createSpecification(criteria)
        return userActivityRepository.count(specification)
    }

    /**
     * Function to convert [UserActivityCriteria] to a [Specification].
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching [Specification] of the entity.
     */
    protected fun createSpecification(criteria: UserActivityCriteria?): Specification<UserActivity?> {
        var specification: Specification<UserActivity?> = Specification.where(null)
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            val distinctCriteria = criteria.distinct
            if (distinctCriteria != null) {
                specification = specification.and(distinct(distinctCriteria))
            }
            if (criteria.id != null) {
                specification = specification.and(buildRangeSpecification(criteria.id, UserActivity_.id))
            }
            if (criteria.createTimeStamp != null) {
                specification = specification.and(buildRangeSpecification(criteria.createTimeStamp, UserActivity_.createTimeStamp))
            }
            if (criteria.updateTimeStamp != null) {
                specification = specification.and(buildRangeSpecification(criteria.updateTimeStamp, UserActivity_.updateTimeStamp))
            }
            if (criteria.deviceTimeStamp != null) {
                specification = specification.and(buildRangeSpecification(criteria.deviceTimeStamp, UserActivity_.deviceTimeStamp))
            }
            if (criteria.listName != null) {
                specification = specification.and(buildStringSpecification(criteria.listName, UserActivity_.listName))
            }
            if (criteria.total != null) {
                specification = specification.and(buildRangeSpecification(criteria.total, UserActivity_.total))
            }
            if (criteria.completed != null) {
                specification = specification.and(buildRangeSpecification(criteria.completed, UserActivity_.completed))
            }
            if (criteria.uniqueName != null) {
                specification = specification.and(buildStringSpecification(criteria.uniqueName, UserActivity_.uniqueName))
            }
            if (criteria.activityId != null) {
                specification = specification.and(
                    buildSpecification(criteria.activityId as Filter<Long>) {
                        it.join(UserActivity_.activity, JoinType.LEFT).get(TabletUser_.id)
                    }
                )
            }
        }
        return specification
    }
}
