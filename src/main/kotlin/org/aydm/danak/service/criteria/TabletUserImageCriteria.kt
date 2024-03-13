package org.aydm.danak.service.criteria

import org.aydm.danak.domain.enumeration.TabletUserImageType
import org.springdoc.api.annotations.ParameterObject
import tech.jhipster.service.Criteria
import tech.jhipster.service.filter.*
import java.io.Serializable

/**
 * Criteria class for the [org.aydm.danak.domain.TabletUserImage] entity. This class is used in
 * [org.aydm.danak.web.rest.TabletUserImageResource] to receive all the possible filtering options from the
 * Http GET request parameters.
 * For example the following could be a valid request:
 * ```/tablet-user-images?id.greaterThan=5&attr1.contains=something&attr2.specified=false```
 * As Spring is unable to properly convert the types, unless specific [Filter] class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
data class TabletUserImageCriteria(
    var id: LongFilter? = null,
    var tabletUserImageType: TabletUserImageTypeFilter? = null,
    var fileId: LongFilter? = null,
    var tabletUserId: LongFilter? = null,
    var distinct: Boolean? = null
) : Serializable, Criteria {

    constructor(other: TabletUserImageCriteria) :
        this(
            other.id?.copy(),
            other.tabletUserImageType?.copy(),
            other.fileId?.copy(),
            other.tabletUserId?.copy(),
            other.distinct
        )

    /**
     * Class for filtering TabletUserImageType
     */
    class TabletUserImageTypeFilter : Filter<TabletUserImageType> {
        constructor()

        constructor(filter: TabletUserImageTypeFilter) : super(filter)

        override fun copy() = TabletUserImageTypeFilter(this)
    }

    override fun copy() = TabletUserImageCriteria(this)

    companion object {
        private const val serialVersionUID: Long = 1L
    }
}
