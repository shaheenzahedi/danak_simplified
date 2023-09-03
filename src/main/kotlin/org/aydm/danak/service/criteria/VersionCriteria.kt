package org.aydm.danak.service.criteria

import org.springdoc.api.annotations.ParameterObject
import tech.jhipster.service.Criteria
import tech.jhipster.service.filter.IntegerFilter
import tech.jhipster.service.filter.LongFilter
import tech.jhipster.service.filter.StringFilter
import java.io.Serializable

/**
 * Criteria class for the [org.aydm.danak.domain.Version] entity. This class is used in
 * [org.aydm.danak.web.rest.VersionResource] to receive all the possible filtering options from the
 * Http GET request parameters.
 * For example the following could be a valid request:
 * ```/versions?id.greaterThan=5&attr1.contains=something&attr2.specified=false```
 * As Spring is unable to properly convert the types, unless specific [Filter] class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
data class VersionCriteria(
    var id: LongFilter? = null,
    var version: IntegerFilter? = null,
    var tag: StringFilter? = null,
    var fileBelongingsId: LongFilter? = null,
    var fileId: LongFilter? = null,
    var distinct: Boolean? = null
) : Serializable, Criteria {

    constructor(other: VersionCriteria) :
        this(
            other.id?.copy(),
            other.version?.copy(),
            other.tag?.copy(),
            other.fileBelongingsId?.copy(),
            other.fileId?.copy(),
            other.distinct
        )

    override fun copy() = VersionCriteria(this)

    companion object {
        private const val serialVersionUID: Long = 1L
    }
}
