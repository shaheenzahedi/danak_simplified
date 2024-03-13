package org.aydm.danak.service

import org.aydm.danak.domain.FileBelongings
import org.aydm.danak.domain.FileBelongings_
import org.aydm.danak.domain.File_
import org.aydm.danak.domain.Version_
import org.aydm.danak.repository.FileBelongingsRepository
import org.aydm.danak.service.criteria.FileBelongingsCriteria
import org.aydm.danak.service.dto.FileBelongingsDTO
import org.aydm.danak.service.mapper.FileBelongingsMapper
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
 * Service for executing complex queries for [FileBelongings] entities in the database.
 * The main input is a [FileBelongingsCriteria] which gets converted to [Specification],
 * in a way that all the filters must apply.
 * It returns a [MutableList] of [FileBelongingsDTO] or a [Page] of [FileBelongingsDTO] which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
class FileBelongingsQueryService(
    private val fileBelongingsRepository: FileBelongingsRepository,
    private val fileBelongingsMapper: FileBelongingsMapper,
) : QueryService<FileBelongings>() {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Return a [MutableList] of [FileBelongingsDTO] which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    fun findByCriteria(criteria: FileBelongingsCriteria?): MutableList<FileBelongingsDTO> {
        log.debug("find by criteria : $criteria")
        val specification = createSpecification(criteria)
        return fileBelongingsMapper.toDto(fileBelongingsRepository.findAll(specification))
    }

    /**
     * Return a [Page] of [FileBelongingsDTO] which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    fun findByCriteria(criteria: FileBelongingsCriteria?, page: Pageable): Page<FileBelongingsDTO> {
        log.debug("find by criteria : $criteria, page: $page")
        val specification = createSpecification(criteria)
        return fileBelongingsRepository.findAll(specification, page)
            .map(fileBelongingsMapper::toDto)
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    fun countByCriteria(criteria: FileBelongingsCriteria?): Long {
        log.debug("count by criteria : $criteria")
        val specification = createSpecification(criteria)
        return fileBelongingsRepository.count(specification)
    }

    /**
     * Function to convert [FileBelongingsCriteria] to a [Specification].
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching [Specification] of the entity.
     */
    protected fun createSpecification(criteria: FileBelongingsCriteria?): Specification<FileBelongings?> {
        var specification: Specification<FileBelongings?> = Specification.where(null)
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            val distinctCriteria = criteria.distinct
            if (distinctCriteria != null) {
                specification = specification.and(distinct(distinctCriteria))
            }
            if (criteria.id != null) {
                specification = specification.and(buildRangeSpecification(criteria.id, FileBelongings_.id))
            }
            if (criteria.fileId != null) {
                specification = specification.and(
                    buildSpecification(criteria.fileId as Filter<Long>) {
                        it.join(FileBelongings_.file, JoinType.LEFT).get(File_.id)
                    }
                )
            }
            if (criteria.versionId != null) {
                specification = specification.and(
                    buildSpecification(criteria.versionId as Filter<Long>) {
                        it.join(FileBelongings_.version, JoinType.LEFT).get(Version_.id)
                    }
                )
            }
        }
        return specification
    }
}
