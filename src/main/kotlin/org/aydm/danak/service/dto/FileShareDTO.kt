package org.aydm.danak.service.dto

import java.io.Serializable
import java.util.*

/**
 * A DTO for the [org.aydm.danak.domain.FileShare] entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
data class FileShareDTO(

    var id: Long? = null,

    var file: UploadedFileDTO? = null,

    var user: UserDTO? = null
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FileShareDTO) return false
        val fileShareDTO = other
        if (this.id == null) {
            return false
        }
        return Objects.equals(this.id, fileShareDTO.id)
    }

    override fun hashCode() = Objects.hash(this.id)
}
