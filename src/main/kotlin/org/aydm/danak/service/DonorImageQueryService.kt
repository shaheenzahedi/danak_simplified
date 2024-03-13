package org.aydm.danak.service

import org.aydm.danak.domain.*
import org.aydm.danak.repository.DonorImageRepository
import org.aydm.danak.service.criteria.DonorImageCriteria
import org.aydm.danak.service.dto.DonorImageDTO
import org.aydm.danak.service.mapper.DonorImageMapper
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
 * Service for executing complex queries for [DonorImage] entities in the database.
 * The main input is a [DonorImageCriteria] which gets converted to [Specification],
 * in a way that all the filters must apply.
 * It returns a [MutableList] of [DonorImageDTO] or a [Page] of [DonorImageDTO] which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
class DonorImageQueryService(
    private val donorImageRepository: DonorImageRepository,
    private val donorImageMapper: DonorImageMapper,
) : QueryService<DonorImage>() {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Return a [MutableList] of [DonorImageDTO] which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    fun findByCriteria(criteria: DonorImageCriteria?): MutableList<DonorImageDTO> {
        log.debug("find by criteria : $criteria")
        val specification = createSpecification(criteria)
        return donorImageMapper.toDto(donorImageRepository.findAll(specification))
    }

    /**
     * Return a [Page] of [DonorImageDTO] which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    fun findByCriteria(criteria: DonorImageCriteria?, page: Pageable): Page<DonorImageDTO> {
        log.debug("find by criteria : $criteria, page: $page")
        val specification = createSpecification(criteria)
        return donorImageRepository.findAll(specification, page)
            .map(donorImageMapper::toDto)
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    fun countByCriteria(criteria: DonorImageCriteria?): Long {
        log.debug("count by criteria : $criteria")
        val specification = createSpecification(criteria)
        return donorImageRepository.count(specification)
    }

    /**
     * Function to convert [DonorImageCriteria] to a [Specification].
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching [Specification] of the entity.
     */
    protected fun createSpecification(criteria: DonorImageCriteria?): Specification<DonorImage?> {
        var specification: Specification<DonorImage?> = Specification.where(null)
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            val distinctCriteria = criteria.distinct
            if (distinctCriteria != null) {
                specification = specification.and(distinct(distinctCriteria))
            }
            if (criteria.id != null) {
                specification = specification.and(buildRangeSpecification(criteria.id, DonorImage_.id))
            }
            if (criteria.donorImageType != null) {
                specification =
                    specification.and(buildSpecification(criteria.donorImageType, DonorImage_.donorImageType))
            }
            if (criteria.fileId != null) {
                specification = specification.and(
                    buildSpecification(criteria.fileId as Filter<Long>) {
                        it.join(DonorImage_.file, JoinType.LEFT).get(UploadedFile_.id)
                    }
                )
            }
            if (criteria.donorId != null) {
                specification = specification.and(
                    buildSpecification(criteria.donorId as Filter<Long>) {
                        it.join(DonorImage_.donor, JoinType.LEFT).get(Donor_.id)
                    }
                )
            }
        }
        return specification
    }
}
