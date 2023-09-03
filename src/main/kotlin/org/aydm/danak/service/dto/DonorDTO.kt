package org.aydm.danak.service.dto

import java.io.Serializable
import java.time.Instant
import java.util.Objects

/**
 * A DTO for the [org.aydm.danak.domain.Donor] entity.
 */
data class DonorDTO(

    var id: Long? = null,

    var createTimeStamp: Instant? = null,

    var updateTimeStamp: Instant? = null,

    var name: String? = null,

    var city: String? = null,

    var country: String? = null,

    var user: UserDTO? = null
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DonorDTO) return false
        val donorDTO = other
        if (this.id == null) {
            return false
        }
        return Objects.equals(this.id, donorDTO.id)
    }

    override fun hashCode() = Objects.hash(this.id)
}
