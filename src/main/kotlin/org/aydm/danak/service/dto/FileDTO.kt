package org.aydm.danak.service.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import java.io.Serializable
import java.util.Objects

/**
 * A DTO for the [org.aydm.danak.domain.File] entity.
 */
data class FileDTO(

    @JsonIgnore
    var id: Long? = null,
    var name: String? = null,
    var checksum: String? = null,
    var path: String? = null,
    var placement: VersionDTO? = null
) : Serializable {

    fun getFtpPath(version: Int): String {
        return "$version/${if (path?.startsWith(".") == true) "" else "$path/"}$name"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FileDTO) return false
        val fileDTO = other
        if (this.id == null) {
            return false
        }
        return Objects.equals(this.id, fileDTO.id)
    }

    override fun hashCode() = Objects.hash(this.id)
}
