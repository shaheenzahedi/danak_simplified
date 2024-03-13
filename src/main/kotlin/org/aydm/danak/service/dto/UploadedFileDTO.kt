package org.aydm.danak.service.dto

import java.io.Serializable
import java.time.Instant
import java.util.*

/**
 * A DTO for the [org.aydm.danak.domain.UploadedFile] entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
data class UploadedFileDTO(

    var id: Long? = null,

    var createTimeStamp: Instant? = null,

    var deleteTimeStamp: Instant? = null,

    var isPublic: Boolean? = null,

    var name: String? = null,

    var path: String? = null,

    var updateTimeStamp: Instant? = null
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is UploadedFileDTO) return false
        val uploadedFileDTO = other
        if (this.id == null) {
            return false
        }
        return Objects.equals(this.id, uploadedFileDTO.id)
    }

    override fun hashCode() = Objects.hash(this.id)
}
