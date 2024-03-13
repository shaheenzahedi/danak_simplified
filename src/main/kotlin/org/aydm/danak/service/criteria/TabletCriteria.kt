package org.aydm.danak.service.criteria

import org.springdoc.api.annotations.ParameterObject
import tech.jhipster.service.Criteria
import tech.jhipster.service.filter.*
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
@SuppressWarnings("common-java:DuplicatedBlocks")
data class TabletCriteria(
    var id: LongFilter? = null,
    var createTimeStamp: InstantFilter? = null,
    var updateTimeStamp: InstantFilter? = null,
    var identifier: StringFilter? = null,
    var tag: StringFilter? = null,
    var name: StringFilter? = null,
    var androidId: StringFilter? = null,
    var macId: StringFilter? = null,
    var model: StringFilter? = null,
    var description: StringFilter? = null,
    var archived: BooleanFilter? = null,
    var tabletLogId: LongFilter? = null,
    var tabletUserId: LongFilter? = null,
    var tabletWatchListId: LongFilter? = null,
    var centerId: LongFilter? = null,
    var donorId: LongFilter? = null,
    var archivedById: LongFilter? = null,
    var modifiedById: LongFilter? = null,
    var searchField: StringFilter? = null,
    var distinct: Boolean? = null
) : Serializable, Criteria {

    constructor(other: TabletCriteria) :
        this(
            other.id?.copy(),
            other.createTimeStamp?.copy(),
            other.updateTimeStamp?.copy(),
            other.identifier?.copy(),
            other.tag?.copy(),
            other.name?.copy(),
            other.androidId?.copy(),
            other.macId?.copy(),
            other.model?.copy(),
            other.description?.copy(),
            other.archived?.copy(),
            other.tabletLogId?.copy(),
            other.tabletUserId?.copy(),
            other.tabletWatchListId?.copy(),
            other.centerId?.copy(),
            other.donorId?.copy(),
            other.archivedById?.copy(),
            other.modifiedById?.copy(),
            other.searchField?.copy(),
            other.distinct
        )

    override fun copy() = TabletCriteria(this)

    companion object {
        private const val serialVersionUID: Long = 1L
    }
}
