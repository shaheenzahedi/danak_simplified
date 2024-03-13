package org.aydm.danak.service.criteria

import org.springdoc.api.annotations.ParameterObject
import tech.jhipster.service.Criteria
import tech.jhipster.service.filter.*
import java.io.Serializable

/**
 * Criteria class for the [org.aydm.danak.domain.CenterWatchList] entity. This class is used in
 * [org.aydm.danak.web.rest.CenterWatchListResource] to receive all the possible filtering options from the
 * Http GET request parameters.
 * For example the following could be a valid request:
 * ```/center-watch-lists?id.greaterThan=5&attr1.contains=something&attr2.specified=false```
 * As Spring is unable to properly convert the types, unless specific [Filter] class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
data class CenterWatchListCriteria(
    var id: LongFilter? = null,
    var centerId: LongFilter? = null,
    var userId: LongFilter? = null,
    var distinct: Boolean? = null
) : Serializable, Criteria {

    constructor(other: CenterWatchListCriteria) :
        this(
            other.id?.copy(),
            other.centerId?.copy(),
            other.userId?.copy(),
            other.distinct
        )

    override fun copy() = CenterWatchListCriteria(this)

    companion object {
        private const val serialVersionUID: Long = 1L
    }
}
