package org.aydm.danak.service.dto

import java.io.Serializable
import java.util.*

/**
 * A DTO for the [org.aydm.danak.domain.CenterWatchList] entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
data class CenterWatchListDTO(

    var id: Long? = null,

    var center: CenterDTO? = null,

    var user: UserDTO? = null
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CenterWatchListDTO) return false
        val centerWatchListDTO = other
        if (this.id == null) {
            return false
        }
        return Objects.equals(this.id, centerWatchListDTO.id)
    }

    override fun hashCode() = Objects.hash(this.id)
}
