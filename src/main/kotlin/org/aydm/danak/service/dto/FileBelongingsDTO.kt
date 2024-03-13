package org.aydm.danak.service.dto

import java.io.Serializable
import java.util.*

/**
 * A DTO for the [org.aydm.danak.domain.FileBelongings] entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
data class FileBelongingsDTO(

    var id: Long? = null,

    var file: FileDTO? = null,

    var version: VersionDTO? = null
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FileBelongingsDTO) return false
        val fileBelongingsDTO = other
        if (this.id == null) {
            return false
        }
        return Objects.equals(this.id, fileBelongingsDTO.id)
    }

    override fun hashCode() = Objects.hash(this.id)
}
