package org.aydm.danak.service.criteria

import org.aydm.danak.domain.enumeration.CenterImageType
import org.springdoc.api.annotations.ParameterObject
import tech.jhipster.service.Criteria
import tech.jhipster.service.filter.*
import java.io.Serializable

/**
 * Criteria class for the [org.aydm.danak.domain.CenterImage] entity. This class is used in
 * [org.aydm.danak.web.rest.CenterImageResource] to receive all the possible filtering options from the
 * Http GET request parameters.
 * For example the following could be a valid request:
 * ```/center-images?id.greaterThan=5&attr1.contains=something&attr2.specified=false```
 * As Spring is unable to properly convert the types, unless specific [Filter] class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
data class CenterImageCriteria(
    var id: LongFilter? = null,
    var centerImageType: CenterImageTypeFilter? = null,
    var fileId: LongFilter? = null,
    var centerId: LongFilter? = null,
    var distinct: Boolean? = null
) : Serializable, Criteria {

    constructor(other: CenterImageCriteria) :
        this(
            other.id?.copy(),
            other.centerImageType?.copy(),
            other.fileId?.copy(),
            other.centerId?.copy(),
            other.distinct
        )

    /**
     * Class for filtering CenterImageType
     */
    class CenterImageTypeFilter : Filter<CenterImageType> {
        constructor()

        constructor(filter: CenterImageTypeFilter) : super(filter)

        override fun copy() = CenterImageTypeFilter(this)
    }

    override fun copy() = CenterImageCriteria(this)

    companion object {
        private const val serialVersionUID: Long = 1L
    }
}
