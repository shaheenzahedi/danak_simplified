package org.aydm.danak.service.criteria

import org.springdoc.api.annotations.ParameterObject
import tech.jhipster.service.Criteria
import tech.jhipster.service.filter.InstantFilter
import tech.jhipster.service.filter.LongFilter
import tech.jhipster.service.filter.StringFilter
import java.io.Serializable

/**
 * Criteria class for the [org.aydm.danak.domain.Tablet] entity. This class is used in
 * [org.aydm.danak.web.rest.TabletResource] to receive all the possible filtering options from the
 * Http GET request parameters.
 * For example the following could be a valid request:
 * ```/tablets?id.greaterThan=5&attr1.contains=something&attr2.specified=false```
 * As Spring is unable to properly convert the types, unless specific [Filter] class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
data class TabletCriteria(
    var id: LongFilter? = null,
    var createTimeStamp: InstantFilter? = null,
    var updateTimeStamp: InstantFilter? = null,
    var name: StringFilter? = null,
    var identifier: StringFilter? = null,
    var model: StringFilter? = null,
    var tabletUserId: LongFilter? = null,
    var centerId: LongFilter? = null,
    var donorId: LongFilter? = null,
    var distinct: Boolean? = null
) : Serializable, Criteria {

    constructor(other: TabletCriteria) :
        this(
            other.id?.copy(),
            other.createTimeStamp?.copy(),
            other.updateTimeStamp?.copy(),
            other.name?.copy(),
            other.identifier?.copy(),
            other.model?.copy(),
            other.tabletUserId?.copy(),
            other.centerId?.copy(),
            other.donorId?.copy(),
            other.distinct
        )

    override fun copy() = TabletCriteria(this)

    companion object {
        private const val serialVersionUID: Long = 1L
    }
}
