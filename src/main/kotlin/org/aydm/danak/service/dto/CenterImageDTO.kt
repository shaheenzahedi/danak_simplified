package org.aydm.danak.service.dto

import org.aydm.danak.domain.enumeration.CenterImageType
import java.io.Serializable
import java.util.*

/**
 * A DTO for the [org.aydm.danak.domain.CenterImage] entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
data class CenterImageDTO(

    var id: Long? = null,

    var centerImageType: CenterImageType? = null,

    var file: UploadedFileDTO? = null,

    var center: CenterDTO? = null
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CenterImageDTO) return false
        val centerImageDTO = other
        if (this.id == null) {
            return false
        }
        return Objects.equals(this.id, centerImageDTO.id)
    }

    override fun hashCode() = Objects.hash(this.id)
}
