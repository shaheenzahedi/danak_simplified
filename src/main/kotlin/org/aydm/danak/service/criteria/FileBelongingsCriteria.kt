package org.aydm.danak.service.criteria

import org.springdoc.api.annotations.ParameterObject
import tech.jhipster.service.Criteria
import tech.jhipster.service.filter.*
import java.io.Serializable

/**
 * Criteria class for the [org.aydm.danak.domain.FileBelongings] entity. This class is used in
 * [org.aydm.danak.web.rest.FileBelongingsResource] to receive all the possible filtering options from the
 * Http GET request parameters.
 * For example the following could be a valid request:
 * ```/file-belongings?id.greaterThan=5&attr1.contains=something&attr2.specified=false```
 * As Spring is unable to properly convert the types, unless specific [Filter] class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
data class FileBelongingsCriteria(
    var id: LongFilter? = null,
    var fileId: LongFilter? = null,
    var versionId: LongFilter? = null,
    var distinct: Boolean? = null
) : Serializable, Criteria {

    constructor(other: FileBelongingsCriteria) :
        this(
            other.id?.copy(),
            other.fileId?.copy(),
            other.versionId?.copy(),
            other.distinct
        )

    override fun copy() = FileBelongingsCriteria(this)

    companion object {
        private const val serialVersionUID: Long = 1L
    }
}
