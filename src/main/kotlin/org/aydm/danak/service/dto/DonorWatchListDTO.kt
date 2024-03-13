package org.aydm.danak.service.dto

import java.io.Serializable
import java.util.*

/**
 * A DTO for the [org.aydm.danak.domain.DonorWatchList] entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
data class DonorWatchListDTO(

    var id: Long? = null,

    var donor: DonorDTO? = null,

    var user: UserDTO? = null
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DonorWatchListDTO) return false
        val donorWatchListDTO = other
        if (this.id == null) {
            return false
        }
        return Objects.equals(this.id, donorWatchListDTO.id)
    }

    override fun hashCode() = Objects.hash(this.id)
}
