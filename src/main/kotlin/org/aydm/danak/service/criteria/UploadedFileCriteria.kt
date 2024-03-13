package org.aydm.danak.service.criteria

import org.springdoc.api.annotations.ParameterObject
import tech.jhipster.service.Criteria
import tech.jhipster.service.filter.*
import java.io.Serializable

/**
 * Criteria class for the [org.aydm.danak.domain.UploadedFile] entity. This class is used in
 * [org.aydm.danak.web.rest.UploadedFileResource] to receive all the possible filtering options from the
 * Http GET request parameters.
 * For example the following could be a valid request:
 * ```/uploaded-files?id.greaterThan=5&attr1.contains=something&attr2.specified=false```
 * As Spring is unable to properly convert the types, unless specific [Filter] class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
data class UploadedFileCriteria(
    var id: LongFilter? = null,
    var createTimeStamp: InstantFilter? = null,
    var deleteTimeStamp: InstantFilter? = null,
    var isPublic: BooleanFilter? = null,
    var name: StringFilter? = null,
    var path: StringFilter? = null,
    var updateTimeStamp: InstantFilter? = null,
    var fileShareId: LongFilter? = null,
    var distinct: Boolean? = null
) : Serializable, Criteria {

    constructor(other: UploadedFileCriteria) :
        this(
            other.id?.copy(),
            other.createTimeStamp?.copy(),
            other.deleteTimeStamp?.copy(),
            other.isPublic?.copy(),
            other.name?.copy(),
            other.path?.copy(),
            other.updateTimeStamp?.copy(),
            other.fileShareId?.copy(),
            other.distinct
        )

    override fun copy() = UploadedFileCriteria(this)

    companion object {
        private const val serialVersionUID: Long = 1L
    }
}
