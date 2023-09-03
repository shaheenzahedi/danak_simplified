package org.aydm.danak.service.criteria

import org.springdoc.api.annotations.ParameterObject
import tech.jhipster.service.Criteria
import tech.jhipster.service.filter.InstantFilter
import tech.jhipster.service.filter.LongFilter
import tech.jhipster.service.filter.StringFilter
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
data class CenterCriteria(
    var id: LongFilter? = null,
    var createTimeStamp: InstantFilter? = null,
    var updateTimeStamp: InstantFilter? = null,
    var name: StringFilter? = null,
    var city: StringFilter? = null,
    var country: StringFilter? = null,
    var tabletId: LongFilter? = null,
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
            other.tabletId?.copy(),
            other.distinct
        )

    override fun copy() = CenterCriteria(this)

    companion object {
        private const val serialVersionUID: Long = 1L
    }
}
