package org.aydm.danak.service

import org.aydm.danak.domain.*
import org.aydm.danak.repository.UploadedFileRepository
import org.aydm.danak.service.criteria.UploadedFileCriteria
import org.aydm.danak.service.dto.UploadedFileDTO
import org.aydm.danak.service.mapper.UploadedFileMapper
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
 * Service for executing complex queries for [UploadedFile] entities in the database.
 * The main input is a [UploadedFileCriteria] which gets converted to [Specification],
 * in a way that all the filters must apply.
 * It returns a [MutableList] of [UploadedFileDTO] or a [Page] of [UploadedFileDTO] which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
class UploadedFileQueryService(
    private val uploadedFileRepository: UploadedFileRepository,
    private val uploadedFileMapper: UploadedFileMapper,
) : QueryService<UploadedFile>() {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Return a [MutableList] of [UploadedFileDTO] which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    fun findByCriteria(criteria: UploadedFileCriteria?): MutableList<UploadedFileDTO> {
        log.debug("find by criteria : $criteria")
        val specification = createSpecification(criteria)
        return uploadedFileMapper.toDto(uploadedFileRepository.findAll(specification))
    }

    /**
     * Return a [Page] of [UploadedFileDTO] which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    fun findByCriteria(criteria: UploadedFileCriteria?, page: Pageable): Page<UploadedFileDTO> {
        log.debug("find by criteria : $criteria, page: $page")
        val specification = createSpecification(criteria)
        return uploadedFileRepository.findAll(specification, page)
            .map(uploadedFileMapper::toDto)
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    fun countByCriteria(criteria: UploadedFileCriteria?): Long {
        log.debug("count by criteria : $criteria")
        val specification = createSpecification(criteria)
        return uploadedFileRepository.count(specification)
    }

    /**
     * Function to convert [UploadedFileCriteria] to a [Specification].
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching [Specification] of the entity.
     */
    protected fun createSpecification(criteria: UploadedFileCriteria?): Specification<UploadedFile?> {
        var specification: Specification<UploadedFile?> = Specification.where(null)
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            val distinctCriteria = criteria.distinct
            if (distinctCriteria != null) {
                specification = specification.and(distinct(distinctCriteria))
            }
            if (criteria.id != null) {
                specification = specification.and(buildRangeSpecification(criteria.id, UploadedFile_.id))
            }
            if (criteria.createTimeStamp != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.createTimeStamp, UploadedFile_.createTimeStamp))
            }
            if (criteria.deleteTimeStamp != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.deleteTimeStamp, UploadedFile_.deleteTimeStamp))
            }
            if (criteria.isPublic != null) {
                specification = specification.and(buildSpecification(criteria.isPublic, UploadedFile_.isPublic))
            }
            if (criteria.name != null) {
                specification = specification.and(buildStringSpecification(criteria.name, UploadedFile_.name))
            }
            if (criteria.path != null) {
                specification = specification.and(buildStringSpecification(criteria.path, UploadedFile_.path))
            }
            if (criteria.updateTimeStamp != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.updateTimeStamp, UploadedFile_.updateTimeStamp))
            }
            if (criteria.fileShareId != null) {
                specification = specification.and(
                    buildSpecification(criteria.fileShareId as Filter<Long>) {
                        it.join(UploadedFile_.fileShares, JoinType.LEFT).get(FileShare_.id)
                    }
                )
            }
        }
        return specification
    }
}
