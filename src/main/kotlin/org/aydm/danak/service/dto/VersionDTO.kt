package org.aydm.danak.service.dto

import java.util.Objects
import java.io.Serializable


/**
 * A DTO for the [org.aydm.danak.domain.Version] entity.
 */
data class VersionDTO(

    var id: Long? = null,

    var version: Int? = null,

    var tag: String? = null
) : Serializable {


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is VersionDTO) return false
        val versionDTO = other
        if (this.id == null){
            return false;
        }
        return Objects.equals(this.id, versionDTO.id);
    }

    override fun hashCode() =        Objects.hash(this.id)
}
