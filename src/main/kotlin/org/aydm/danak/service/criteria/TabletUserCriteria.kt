package org.aydm.danak.service.criteria

import org.springdoc.api.annotations.ParameterObject
import tech.jhipster.service.Criteria
import tech.jhipster.service.filter.*
import java.io.Serializable

/**
 * Criteria class for the [org.aydm.danak.domain.TabletUser] entity. This class is used in
 * [org.aydm.danak.web.rest.TabletUserResource] to receive all the possible filtering options from the
 * Http GET request parameters.
 * For example the following could be a valid request:
 * ```/tablet-users?id.greaterThan=5&attr1.contains=something&attr2.specified=false```
 * As Spring is unable to properly convert the types, unless specific [Filter] class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
data class TabletUserCriteria(
    var id: LongFilter? = null,
    var createTimeStamp: InstantFilter? = null,
    var updateTimeStamp: InstantFilter? = null,
    var firstName: StringFilter? = null,
    var lastName: StringFilter? = null,
    var email: StringFilter? = null,
    var description: StringFilter? = null,
    var recoveryPhrase: StringFilter? = null,
    var archived: BooleanFilter? = null,
    var tabletUserImageId: LongFilter? = null,
    var tabletUserWatchListId: LongFilter? = null,
    var userActivityId: LongFilter? = null,
    var tabletId: LongFilter? = null,
    var archivedById: LongFilter? = null,
    var modifiedById: LongFilter? = null,
    var searchField: StringFilter? = null,
    var distinct: Boolean? = null
) : Serializable, Criteria {

    constructor(other: TabletUserCriteria) :
        this(
            other.id?.copy(),
            other.createTimeStamp?.copy(),
            other.updateTimeStamp?.copy(),
            other.firstName?.copy(),
            other.lastName?.copy(),
            other.email?.copy(),
            other.description?.copy(),
            other.recoveryPhrase?.copy(),
            other.archived?.copy(),
            other.tabletUserImageId?.copy(),
            other.tabletUserWatchListId?.copy(),
            other.userActivityId?.copy(),
            other.tabletId?.copy(),
            other.archivedById?.copy(),
            other.modifiedById?.copy(),
            other.searchField?.copy(),
            other.distinct
        )

    override fun copy() = TabletUserCriteria(this)

    companion object {
        private const val serialVersionUID: Long = 1L
    }
}
