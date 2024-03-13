package org.aydm.danak.service.dto

import java.io.Serializable
import java.util.*

/**
 * A DTO for the [org.aydm.danak.domain.TabletUserWatchList] entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
data class TabletUserWatchListDTO(

    var id: Long? = null,

    var tabletUser: TabletUserDTO? = null,

    var user: UserDTO? = null
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TabletUserWatchListDTO) return false
        val tabletUserWatchListDTO = other
        if (this.id == null) {
            return false
        }
        return Objects.equals(this.id, tabletUserWatchListDTO.id)
    }

    override fun hashCode() = Objects.hash(this.id)
}
