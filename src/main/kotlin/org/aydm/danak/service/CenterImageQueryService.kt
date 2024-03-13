package org.aydm.danak.service

import org.aydm.danak.domain.*
import org.aydm.danak.repository.CenterImageRepository
import org.aydm.danak.service.criteria.CenterImageCriteria
import org.aydm.danak.service.dto.CenterImageDTO
import org.aydm.danak.service.mapper.CenterImageMapper
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
 * Service for executing complex queries for [CenterImage] entities in the database.
 * The main input is a [CenterImageCriteria] which gets converted to [Specification],
 * in a way that all the filters must apply.
 * It returns a [MutableList] of [CenterImageDTO] or a [Page] of [CenterImageDTO] which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
class CenterImageQueryService(
    private val centerImageRepository: CenterImageRepository,
    private val centerImageMapper: CenterImageMapper,
) : QueryService<CenterImage>() {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Return a [MutableList] of [CenterImageDTO] which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    fun findByCriteria(criteria: CenterImageCriteria?): MutableList<CenterImageDTO> {
        log.debug("find by criteria : $criteria")
        val specification = createSpecification(criteria)
        return centerImageMapper.toDto(centerImageRepository.findAll(specification))
    }

    /**
     * Return a [Page] of [CenterImageDTO] which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    fun findByCriteria(criteria: CenterImageCriteria?, page: Pageable): Page<CenterImageDTO> {
        log.debug("find by criteria : $criteria, page: $page")
        val specification = createSpecification(criteria)
        return centerImageRepository.findAll(specification, page)
            .map(centerImageMapper::toDto)
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    fun countByCriteria(criteria: CenterImageCriteria?): Long {
        log.debug("count by criteria : $criteria")
        val specification = createSpecification(criteria)
        return centerImageRepository.count(specification)
    }

    /**
     * Function to convert [CenterImageCriteria] to a [Specification].
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching [Specification] of the entity.
     */
    protected fun createSpecification(criteria: CenterImageCriteria?): Specification<CenterImage?> {
        var specification: Specification<CenterImage?> = Specification.where(null)
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            val distinctCriteria = criteria.distinct
            if (distinctCriteria != null) {
                specification = specification.and(distinct(distinctCriteria))
            }
            if (criteria.id != null) {
                specification = specification.and(buildRangeSpecification(criteria.id, CenterImage_.id))
            }
            if (criteria.centerImageType != null) {
                specification =
                    specification.and(buildSpecification(criteria.centerImageType, CenterImage_.centerImageType))
            }
            if (criteria.fileId != null) {
                specification = specification.and(
                    buildSpecification(criteria.fileId as Filter<Long>) {
                        it.join(CenterImage_.file, JoinType.LEFT).get(UploadedFile_.id)
                    }
                )
            }
            if (criteria.centerId != null) {
                specification = specification.and(
                    buildSpecification(criteria.centerId as Filter<Long>) {
                        it.join(CenterImage_.center, JoinType.LEFT).get(Center_.id)
                    }
                )
            }
        }
        return specification
    }
}
