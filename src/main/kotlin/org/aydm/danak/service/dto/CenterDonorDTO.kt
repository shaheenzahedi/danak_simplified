package org.aydm.danak.service.dto

import org.aydm.danak.domain.enumeration.DonorType
import java.io.Serializable
import java.time.Instant
import java.util.*

/**
 * A DTO for the [org.aydm.danak.domain.CenterDonor] entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
data class CenterDonorDTO(

    var id: Long? = null,

    var joinedTimeStamp: Instant? = null,

    var donorType: DonorType? = null,

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
