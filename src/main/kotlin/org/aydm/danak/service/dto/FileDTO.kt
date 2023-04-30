package org.aydm.danak.service.dto

import java.util.Objects
import java.io.Serializable


/**
 * A DTO for the [org.aydm.danak.domain.File] entity.
 */
data class FileDTO(

    var id: Long? = null,

    var name: String? = null,

    var checksum: String? = null,

    var path: String? = null,

    var placement: VersionDTO? = null
) : Serializable {


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FileDTO) return false
        val fileDTO = other
        if (this.id == null){
            return false;
        }
        return Objects.equals(this.id, fileDTO.id);
    }

    override fun hashCode() =        Objects.hash(this.id)
}
