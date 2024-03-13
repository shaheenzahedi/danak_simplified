package org.aydm.danak.service.criteria

import org.aydm.danak.domain.enumeration.DonorImageType
import org.springdoc.api.annotations.ParameterObject
import tech.jhipster.service.Criteria
import tech.jhipster.service.filter.*
import java.io.Serializable

/**
 * Criteria class for the [org.aydm.danak.domain.DonorImage] entity. This class is used in
 * [org.aydm.danak.web.rest.DonorImageResource] to receive all the possible filtering options from the
 * Http GET request parameters.
 * For example the following could be a valid request:
 * ```/donor-images?id.greaterThan=5&attr1.contains=something&attr2.specified=false```
 * As Spring is unable to properly convert the types, unless specific [Filter] class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
data class DonorImageCriteria(
    var id: LongFilter? = null,
    var donorImageType: DonorImageTypeFilter? = null,
    var fileId: LongFilter? = null,
    var donorId: LongFilter? = null,
    var distinct: Boolean? = null
) : Serializable, Criteria {

    constructor(other: DonorImageCriteria) :
        this(
            other.id?.copy(),
            other.donorImageType?.copy(),
            other.fileId?.copy(),
            other.donorId?.copy(),
            other.distinct
        )

    /**
     * Class for filtering DonorImageType
     */
    class DonorImageTypeFilter : Filter<DonorImageType> {
        constructor()

        constructor(filter: DonorImageTypeFilter) : super(filter)

        override fun copy() = DonorImageTypeFilter(this)
    }

    override fun copy() = DonorImageCriteria(this)

    companion object {
        private const val serialVersionUID: Long = 1L
    }
}
