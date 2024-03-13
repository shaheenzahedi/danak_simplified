package org.aydm.danak.service.criteria

import org.aydm.danak.domain.enumeration.CenterType
import org.springdoc.api.annotations.ParameterObject
import tech.jhipster.service.Criteria
import tech.jhipster.service.filter.*
import java.io.Serializable

/**
 * Criteria class for the [org.aydm.danak.domain.Center] entity. This class is used in
 * [org.aydm.danak.web.rest.CenterResource] to receive all the possible filtering options from the
 * Http GET request parameters.
 * For example the following could be a valid request:
 * ```/centers?id.greaterThan=5&attr1.contains=something&attr2.specified=false```
 * As Spring is unable to properly convert the types, unless specific [Filter] class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
data class CenterCriteria(
    var id: LongFilter? = null,
    var createTimeStamp: InstantFilter? = null,
    var updateTimeStamp: InstantFilter? = null,
    var name: StringFilter? = null,
    var city: StringFilter? = null,
    var country: StringFilter? = null,
    var archived: BooleanFilter? = null,
    var centerType: CenterTypeFilter? = null,
    var latitude: LongFilter? = null,
    var longitude: LongFilter? = null,
    var centerDonorId: LongFilter? = null,
    var centerImageId: LongFilter? = null,
    var centerWatchListId: LongFilter? = null,
    var tabletId: LongFilter? = null,
    var archivedById: LongFilter? = null,
    var createdById: LongFilter? = null,
    var modifiedById: LongFilter? = null,
    var distinct: Boolean? = null
) : Serializable, Criteria {

    constructor(other: CenterCriteria) :
        this(
            other.id?.copy(),
            other.createTimeStamp?.copy(),
            other.updateTimeStamp?.copy(),
            other.name?.copy(),
            other.city?.copy(),
            other.country?.copy(),
            other.archived?.copy(),
            other.centerType?.copy(),
            other.latitude?.copy(),
            other.longitude?.copy(),
            other.centerDonorId?.copy(),
            other.centerImageId?.copy(),
            other.centerWatchListId?.copy(),
            other.tabletId?.copy(),
            other.archivedById?.copy(),
            other.createdById?.copy(),
            other.modifiedById?.copy(),
            other.distinct
        )

    /**
     * Class for filtering CenterType
     */
    class CenterTypeFilter : Filter<CenterType> {
        constructor()

        constructor(filter: CenterTypeFilter) : super(filter)

        override fun copy() = CenterTypeFilter(this)
    }

    override fun copy() = CenterCriteria(this)

    companion object {
        private const val serialVersionUID: Long = 1L
    }
}
