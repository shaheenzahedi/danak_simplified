package org.aydm.danak.service

import org.aydm.danak.domain.* // for static metamodels
import org.aydm.danak.domain.File
import org.aydm.danak.repository.FileRepository
import org.aydm.danak.service.criteria.FileCriteria
import org.aydm.danak.service.dto.FileDTO
import org.aydm.danak.service.mapper.FileMapper
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
 * Service for executing complex queries for [File] entities in the database.
 * The main input is a [FileCriteria] which gets converted to [Specification],
 * in a way that all the filters must apply.
 * It returns a [MutableList] of [FileDTO] or a [Page] of [FileDTO] which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
class FileQueryService(
    private val fileRepository: FileRepository,
    private val fileMapper: FileMapper,
) : QueryService<File>() {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Return a [MutableList] of [FileDTO] which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    fun findByCriteria(criteria: FileCriteria?): MutableList<FileDTO> {
        log.debug("find by criteria : $criteria")
        val specification = createSpecification(criteria)
        return fileMapper.toDto(fileRepository.findAll(specification))
    }

    /**
     * Return a [Page] of [FileDTO] which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    fun findByCriteria(criteria: FileCriteria?, page: Pageable): Page<FileDTO> {
        log.debug("find by criteria : $criteria, page: $page")
        val specification = createSpecification(criteria)
        return fileRepository.findAll(specification, page)
            .map(fileMapper::toDto)
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    fun countByCriteria(criteria: FileCriteria?): Long {
        log.debug("count by criteria : $criteria")
        val specification = createSpecification(criteria)
        return fileRepository.count(specification)
    }

    /**
     * Function to convert [FileCriteria] to a [Specification].
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching [Specification] of the entity.
     */
    protected fun createSpecification(criteria: FileCriteria?): Specification<File?> {
        var specification: Specification<File?> = Specification.where(null)
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            val distinctCriteria = criteria.distinct
            if (distinctCriteria != null) {
                specification = specification.and(distinct(distinctCriteria))
            }
            if (criteria.id != null) {
                specification = specification.and(buildRangeSpecification(criteria.id, File_.id))
            }
            if (criteria.name != null) {
                specification = specification.and(buildStringSpecification(criteria.name, File_.name))
            }
            if (criteria.checksum != null) {
                specification = specification.and(buildStringSpecification(criteria.checksum, File_.checksum))
            }
            if (criteria.path != null) {
                specification = specification.and(buildStringSpecification(criteria.path, File_.path))
            }
            if (criteria.size != null) {
                specification = specification.and(buildStringSpecification(criteria.size, File_.size))
            }
            if (criteria.fileBelongingsId != null) {
                specification = specification.and(
                    buildSpecification(criteria.fileBelongingsId as Filter<Long>) {
                        it.join(File_.fileBelongings, JoinType.LEFT).get(FileBelongings_.id)
                    }
                )
            }
            if (criteria.placementId != null) {
                specification = specification.and(
                    buildSpecification(criteria.placementId as Filter<Long>) {
                        it.join(File_.placement, JoinType.LEFT).get(Version_.id)
                    }
                )
            }
        }
        return specification
    }
}
