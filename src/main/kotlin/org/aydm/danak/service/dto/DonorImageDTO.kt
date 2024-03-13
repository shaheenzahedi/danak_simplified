package org.aydm.danak.service.dto

import org.aydm.danak.domain.enumeration.DonorImageType
import java.io.Serializable
import java.util.*

/**
 * A DTO for the [org.aydm.danak.domain.DonorImage] entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
data class DonorImageDTO(

    var id: Long? = null,

    var donorImageType: DonorImageType? = null,

    var file: UploadedFileDTO? = null,

    var donor: DonorDTO? = null
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DonorImageDTO) return false
        val donorImageDTO = other
        if (this.id == null) {
            return false
        }
        return Objects.equals(this.id, donorImageDTO.id)
    }

    override fun hashCode() = Objects.hash(this.id)
}
