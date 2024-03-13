package org.aydm.danak.service.criteria

import org.aydm.danak.domain.enumeration.EducationType
import org.springdoc.api.annotations.ParameterObject
import tech.jhipster.service.Criteria
import tech.jhipster.service.filter.*
import java.io.Serializable

/**
 * Criteria class for the [org.aydm.danak.domain.Donor] entity. This class is used in
 * [org.aydm.danak.web.rest.DonorResource] to receive all the possible filtering options from the
 * Http GET request parameters.
 * For example the following could be a valid request:
 * ```/donors?id.greaterThan=5&attr1.contains=something&attr2.specified=false```
 * As Spring is unable to properly convert the types, unless specific [Filter] class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
data class DonorCriteria(
    var id: LongFilter? = null,
    var createTimeStamp: InstantFilter? = null,
    var updateTimeStamp: InstantFilter? = null,
    var name: StringFilter? = null,
    var city: StringFilter? = null,
    var country: StringFilter? = null,
    var nationalCode: StringFilter? = null,
    var educationType: EducationTypeFilter? = null,
    var education: StringFilter? = null,
    var occupation: StringFilter? = null,
    var workPlace: StringFilter? = null,
    var workPlacePhone: StringFilter? = null,
    var archived: BooleanFilter? = null,
    var otpPhoneCode: LongFilter? = null,
    var otpPhoneEnable: BooleanFilter? = null,
    var otpPhoneSentTimeStamp: InstantFilter? = null,
    var latitude: LongFilter? = null,
    var longitude: LongFilter? = null,
    var uid: StringFilter? = null,
    var centerDonorId: LongFilter? = null,
    var donorImageId: LongFilter? = null,
    var donorWatchListId: LongFilter? = null,
    var tabletId: LongFilter? = null,
    var userId: LongFilter? = null,
    var archivedById: LongFilter? = null,
    var createdById: LongFilter? = null,
    var modifiedById: LongFilter? = null,
    var distinct: Boolean? = null
) : Serializable, Criteria {

    constructor(other: DonorCriteria) :
        this(
            other.id?.copy(),
            other.createTimeStamp?.copy(),
            other.updateTimeStamp?.copy(),
            other.name?.copy(),
            other.city?.copy(),
            other.country?.copy(),
            other.nationalCode?.copy(),
            other.educationType?.copy(),
            other.education?.copy(),
            other.occupation?.copy(),
            other.workPlace?.copy(),
            other.workPlacePhone?.copy(),
            other.archived?.copy(),
            other.otpPhoneCode?.copy(),
            other.otpPhoneEnable?.copy(),
            other.otpPhoneSentTimeStamp?.copy(),
            other.latitude?.copy(),
            other.longitude?.copy(),
            other.uid?.copy(),
            other.centerDonorId?.copy(),
            other.donorImageId?.copy(),
            other.donorWatchListId?.copy(),
            other.tabletId?.copy(),
            other.userId?.copy(),
            other.archivedById?.copy(),
            other.createdById?.copy(),
            other.modifiedById?.copy(),
            other.distinct
        )

    /**
     * Class for filtering EducationType
     */
    class EducationTypeFilter : Filter<EducationType> {
        constructor()

        constructor(filter: EducationTypeFilter) : super(filter)

        override fun copy() = EducationTypeFilter(this)
    }

    override fun copy() = DonorCriteria(this)

    companion object {
        private const val serialVersionUID: Long = 1L
    }
}
