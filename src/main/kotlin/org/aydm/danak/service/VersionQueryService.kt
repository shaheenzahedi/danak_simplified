package org.aydm.danak.service

import org.aydm.danak.domain.FileBelongings_
import org.aydm.danak.domain.File_
import org.aydm.danak.domain.Version
import org.aydm.danak.domain.Version_
import org.aydm.danak.repository.VersionRepository
import org.aydm.danak.service.criteria.VersionCriteria
import org.aydm.danak.service.dto.VersionDTO
import org.aydm.danak.service.mapper.VersionMapper
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
 * Service for executing complex queries for [Version] entities in the database.
 * The main input is a [VersionCriteria] which gets converted to [Specification],
 * in a way that all the filters must apply.
 * It returns a [MutableList] of [VersionDTO] or a [Page] of [VersionDTO] which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
class VersionQueryService(
    private val versionRepository: VersionRepository,
    private val versionMapper: VersionMapper,
) : QueryService<Version>() {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Return a [MutableList] of [VersionDTO] which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    fun findByCriteria(criteria: VersionCriteria?): MutableList<VersionDTO> {
        log.debug("find by criteria : $criteria")
        val specification = createSpecification(criteria)
        return versionMapper.toDto(versionRepository.findAll(specification))
    }

    /**
     * Return a [Page] of [VersionDTO] which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    fun findByCriteria(criteria: VersionCriteria?, page: Pageable): Page<VersionDTO> {
        log.debug("find by criteria : $criteria, page: $page")
        val specification = createSpecification(criteria)
        return versionRepository.findAll(specification, page)
            .map(versionMapper::toDto)
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    fun countByCriteria(criteria: VersionCriteria?): Long {
        log.debug("count by criteria : $criteria")
        val specification = createSpecification(criteria)
        return versionRepository.count(specification)
    }

    /**
     * Function to convert [VersionCriteria] to a [Specification].
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching [Specification] of the entity.
     */
    protected fun createSpecification(criteria: VersionCriteria?): Specification<Version?> {
        var specification: Specification<Version?> = Specification.where(null)
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            val distinctCriteria = criteria.distinct
            if (distinctCriteria != null) {
                specification = specification.and(distinct(distinctCriteria))
            }
            if (criteria.id != null) {
                specification = specification.and(buildRangeSpecification(criteria.id, Version_.id))
            }
            if (criteria.version != null) {
                specification = specification.and(buildRangeSpecification(criteria.version, Version_.version))
            }
            if (criteria.tag != null) {
                specification = specification.and(buildStringSpecification(criteria.tag, Version_.tag))
            }
            if (criteria.fileBelongingsId != null) {
                specification = specification.and(
                    buildSpecification(criteria.fileBelongingsId as Filter<Long>) {
                        it.join(Version_.fileBelongings, JoinType.LEFT).get(FileBelongings_.id)
                    }
                )
            }
            if (criteria.fileId != null) {
                specification = specification.and(
                    buildSpecification(criteria.fileId as Filter<Long>) {
                        it.join(Version_.files, JoinType.LEFT).get(File_.id)
                    }
                )
            }
        }
        return specification
    }
}
