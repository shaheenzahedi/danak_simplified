package org.aydm.danak.service

import org.aydm.danak.domain.*
import org.aydm.danak.repository.TabletUserImageRepository
import org.aydm.danak.service.criteria.TabletUserImageCriteria
import org.aydm.danak.service.dto.TabletUserImageDTO
import org.aydm.danak.service.mapper.TabletUserImageMapper
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
 * Service for executing complex queries for [TabletUserImage] entities in the database.
 * The main input is a [TabletUserImageCriteria] which gets converted to [Specification],
 * in a way that all the filters must apply.
 * It returns a [MutableList] of [TabletUserImageDTO] or a [Page] of [TabletUserImageDTO] which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
class TabletUserImageQueryService(
    private val tabletUserImageRepository: TabletUserImageRepository,
    private val tabletUserImageMapper: TabletUserImageMapper,
) : QueryService<TabletUserImage>() {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Return a [MutableList] of [TabletUserImageDTO] which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    fun findByCriteria(criteria: TabletUserImageCriteria?): MutableList<TabletUserImageDTO> {
        log.debug("find by criteria : $criteria")
        val specification = createSpecification(criteria)
        return tabletUserImageMapper.toDto(tabletUserImageRepository.findAll(specification))
    }

    /**
     * Return a [Page] of [TabletUserImageDTO] which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    fun findByCriteria(criteria: TabletUserImageCriteria?, page: Pageable): Page<TabletUserImageDTO> {
        log.debug("find by criteria : $criteria, page: $page")
        val specification = createSpecification(criteria)
        return tabletUserImageRepository.findAll(specification, page)
            .map(tabletUserImageMapper::toDto)
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    fun countByCriteria(criteria: TabletUserImageCriteria?): Long {
        log.debug("count by criteria : $criteria")
        val specification = createSpecification(criteria)
        return tabletUserImageRepository.count(specification)
    }

    /**
     * Function to convert [TabletUserImageCriteria] to a [Specification].
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching [Specification] of the entity.
     */
    protected fun createSpecification(criteria: TabletUserImageCriteria?): Specification<TabletUserImage?> {
        var specification: Specification<TabletUserImage?> = Specification.where(null)
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            val distinctCriteria = criteria.distinct
            if (distinctCriteria != null) {
                specification = specification.and(distinct(distinctCriteria))
            }
            if (criteria.id != null) {
                specification = specification.and(buildRangeSpecification(criteria.id, TabletUserImage_.id))
            }
            if (criteria.tabletUserImageType != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.tabletUserImageType,
                        TabletUserImage_.tabletUserImageType
                    )
                )
            }
            if (criteria.fileId != null) {
                specification = specification.and(
                    buildSpecification(criteria.fileId as Filter<Long>) {
                        it.join(TabletUserImage_.file, JoinType.LEFT).get(UploadedFile_.id)
                    }
                )
            }
            if (criteria.tabletUserId != null) {
                specification = specification.and(
                    buildSpecification(criteria.tabletUserId as Filter<Long>) {
                        it.join(TabletUserImage_.tabletUser, JoinType.LEFT).get(TabletUser_.id)
                    }
                )
            }
        }
        return specification
    }
}
