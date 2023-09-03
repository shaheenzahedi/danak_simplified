package org.aydm.danak.service.criteria

import org.springdoc.api.annotations.ParameterObject
import tech.jhipster.service.Criteria
import tech.jhipster.service.filter.InstantFilter
import tech.jhipster.service.filter.LongFilter
import tech.jhipster.service.filter.StringFilter
import java.io.Serializable

/**
 * Criteria class for the [org.aydm.danak.domain.UserActivity] entity. This class is used in
 * [org.aydm.danak.web.rest.UserActivityResource] to receive all the possible filtering options from the
 * Http GET request parameters.
 * For example the following could be a valid request:
 * ```/user-activities?id.greaterThan=5&attr1.contains=something&attr2.specified=false```
 * As Spring is unable to properly convert the types, unless specific [Filter] class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
data class UserActivityCriteria(
    var id: LongFilter? = null,
    var createTimeStamp: InstantFilter? = null,
    var updateTimeStamp: InstantFilter? = null,
    var deviceTimeStamp: InstantFilter? = null,
    var listName: StringFilter? = null,
    var total: LongFilter? = null,
    var completed: LongFilter? = null,
    var uniqueName: StringFilter? = null,
    var activityId: LongFilter? = null,
    var distinct: Boolean? = null
) : Serializable, Criteria {

    constructor(other: UserActivityCriteria) :
        this(
            other.id?.copy(),
            other.createTimeStamp?.copy(),
            other.updateTimeStamp?.copy(),
            other.deviceTimeStamp?.copy(),
            other.listName?.copy(),
            other.total?.copy(),
            other.completed?.copy(),
            other.uniqueName?.copy(),
            other.activityId?.copy(),
            other.distinct
        )

    override fun copy() = UserActivityCriteria(this)

    companion object {
        private const val serialVersionUID: Long = 1L
    }
}
