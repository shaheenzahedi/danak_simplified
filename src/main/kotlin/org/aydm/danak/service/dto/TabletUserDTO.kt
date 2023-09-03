package org.aydm.danak.service.dto

import java.io.Serializable
import java.time.Instant
import java.util.Objects

/**
 * A DTO for the [org.aydm.danak.domain.TabletUser] entity.
 */
data class TabletUserDTO(

    var id: Long? = null,

    var createTimeStamp: Instant? = null,

    var updateTimeStamp: Instant? = null,

    var firstName: String? = null,

    var lastName: String? = null,

    var email: String? = null,

    var tablet: TabletDTO? = null
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TabletUserDTO) return false
        val tabletUserDTO = other
        if (this.id == null) {
            return false
        }
        return Objects.equals(this.id, tabletUserDTO.id)
    }

    override fun hashCode() = Objects.hash(this.id)
}
