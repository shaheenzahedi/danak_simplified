package org.aydm.danak.service.criteria

import org.springdoc.api.annotations.ParameterObject
import tech.jhipster.service.Criteria
import tech.jhipster.service.filter.LongFilter
import tech.jhipster.service.filter.StringFilter
import java.io.Serializable

/**
 * Criteria class for the [org.aydm.danak.domain.File] entity. This class is used in
 * [org.aydm.danak.web.rest.FileResource] to receive all the possible filtering options from the
 * Http GET request parameters.
 * For example the following could be a valid request:
 * ```/files?id.greaterThan=5&attr1.contains=something&attr2.specified=false```
 * As Spring is unable to properly convert the types, unless specific [Filter] class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
data class FileCriteria(
    var id: LongFilter? = null,
    var name: StringFilter? = null,
    var checksum: StringFilter? = null,
    var path: StringFilter? = null,
    var size: StringFilter? = null,
    var fileBelongingsId: LongFilter? = null,
    var placementId: LongFilter? = null,
    var distinct: Boolean? = null
) : Serializable, Criteria {

    constructor(other: FileCriteria) :
        this(
            other.id?.copy(),
            other.name?.copy(),
            other.checksum?.copy(),
            other.path?.copy(),
            other.size?.copy(),
            other.fileBelongingsId?.copy(),
            other.placementId?.copy(),
            other.distinct
        )

    override fun copy() = FileCriteria(this)

    companion object {
        private const val serialVersionUID: Long = 1L
    }
}
