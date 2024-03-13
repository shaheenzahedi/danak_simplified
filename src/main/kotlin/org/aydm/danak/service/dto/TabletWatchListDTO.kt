package org.aydm.danak.service.dto

import java.io.Serializable
import java.util.*

/**
 * A DTO for the [org.aydm.danak.domain.TabletWatchList] entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
data class TabletWatchListDTO(

    var id: Long? = null,

    var tablet: TabletDTO? = null,

    var user: UserDTO? = null
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TabletWatchListDTO) return false
        val tabletWatchListDTO = other
        if (this.id == null) {
            return false
        }
        return Objects.equals(this.id, tabletWatchListDTO.id)
    }

    override fun hashCode() = Objects.hash(this.id)
}
