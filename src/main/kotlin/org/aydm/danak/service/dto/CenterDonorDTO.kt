package org.aydm.danak.service.dto

import java.io.Serializable
import java.util.Objects

/**
 * A DTO for the [org.aydm.domain.CenterDonor] entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
data class CenterDonorDTO(

    var id: Long? = null,

    var center: CenterDTO? = null,

    var donor: DonorDTO? = null
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CenterDonorDTO) return false
        val centerDonorDTO = other
        if (this.id == null) {
            return false
        }
        return Objects.equals(this.id, centerDonorDTO.id)
    }

    override fun hashCode() = Objects.hash(this.id)
}
