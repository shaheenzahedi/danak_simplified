package org.aydm.danak.service.criteria

import org.aydm.danak.domain.enumeration.DonorType
import org.springdoc.api.annotations.ParameterObject
import tech.jhipster.service.Criteria
import tech.jhipster.service.filter.*
import java.io.Serializable

/**
 * Criteria class for the [org.aydm.danak.domain.CenterDonor] entity. This class is used in
 * [org.aydm.danak.web.rest.CenterDonorResource] to receive all the possible filtering options from the
 * Http GET request parameters.
 * For example the following could be a valid request:
 * ```/center-donors?id.greaterThan=5&attr1.contains=something&attr2.specified=false```
 * As Spring is unable to properly convert the types, unless specific [Filter] class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
data class CenterDonorCriteria(
    var id: LongFilter? = null,
    var joinedTimeStamp: InstantFilter? = null,
    var donorType: DonorTypeFilter? = null,
    var centerId: LongFilter? = null,
    var donorId: LongFilter? = null,
    var distinct: Boolean? = null
) : Serializable, Criteria {

    constructor(other: CenterDonorCriteria) :
        this(
            other.id?.copy(),
            other.joinedTimeStamp?.copy(),
            other.donorType?.copy(),
            other.centerId?.copy(),
            other.donorId?.copy(),
            other.distinct
        )

    /**
     * Class for filtering DonorType
     */
    class DonorTypeFilter : Filter<DonorType> {
        constructor()

        constructor(filter: DonorTypeFilter) : super(filter)

        override fun copy() = DonorTypeFilter(this)
    }

    override fun copy() = CenterDonorCriteria(this)

    companion object {
        private const val serialVersionUID: Long = 1L
    }
}
