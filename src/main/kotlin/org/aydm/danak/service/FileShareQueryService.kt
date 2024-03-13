package org.aydm.danak.service

import org.aydm.danak.domain.*
import org.aydm.danak.repository.FileShareRepository
import org.aydm.danak.service.criteria.FileShareCriteria
import org.aydm.danak.service.dto.FileShareDTO
import org.aydm.danak.service.mapper.FileShareMapper
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
 * Service for executing complex queries for [FileShare] entities in the database.
 * The main input is a [FileShareCriteria] which gets converted to [Specification],
 * in a way that all the filters must apply.
 * It returns a [MutableList] of [FileShareDTO] or a [Page] of [FileShareDTO] which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
class FileShareQueryService(
    private val fileShareRepository: FileShareRepository,
    private val fileShareMapper: FileShareMapper,
) : QueryService<FileShare>() {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Return a [MutableList] of [FileShareDTO] which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    fun findByCriteria(criteria: FileShareCriteria?): MutableList<FileShareDTO> {
        log.debug("find by criteria : $criteria")
        val specification = createSpecification(criteria)
        return fileShareMapper.toDto(fileShareRepository.findAll(specification))
    }

    /**
     * Return a [Page] of [FileShareDTO] which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    fun findByCriteria(criteria: FileShareCriteria?, page: Pageable): Page<FileShareDTO> {
        log.debug("find by criteria : $criteria, page: $page")
        val specification = createSpecification(criteria)
        return fileShareRepository.findAll(specification, page)
            .map(fileShareMapper::toDto)
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    fun countByCriteria(criteria: FileShareCriteria?): Long {
        log.debug("count by criteria : $criteria")
        val specification = createSpecification(criteria)
        return fileShareRepository.count(specification)
    }

    /**
     * Function to convert [FileShareCriteria] to a [Specification].
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching [Specification] of the entity.
     */
    protected fun createSpecification(criteria: FileShareCriteria?): Specification<FileShare?> {
        var specification: Specification<FileShare?> = Specification.where(null)
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            val distinctCriteria = criteria.distinct
            if (distinctCriteria != null) {
                specification = specification.and(distinct(distinctCriteria))
            }
            if (criteria.id != null) {
                specification = specification.and(buildRangeSpecification(criteria.id, FileShare_.id))
            }
            if (criteria.fileId != null) {
                specification = specification.and(
                    buildSpecification(criteria.fileId as Filter<Long>) {
                        it.join(FileShare_.file, JoinType.LEFT).get(UploadedFile_.id)
                    }
                )
            }
            if (criteria.userId != null) {
                specification = specification.and(
                    buildSpecification(criteria.userId as Filter<Long>) {
                        it.join(FileShare_.user, JoinType.LEFT).get(User_.id)
                    }
                )
            }
        }
        return specification
    }
}
