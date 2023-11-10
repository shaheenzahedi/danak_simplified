package org.aydm.danak.service.dto

import java.io.Serializable
import java.time.Instant
import java.util.Objects

/**
 * A DTO for the [org.aydm.danak.domain.Center] entity.
 */
data class CenterDTO(

    var id: Long? = null,

    var createTimeStamp: Instant? = null,

    var updateTimeStamp: Instant? = null,

    var name: String? = null,

    var city: String? = null,

    var country: String? = null,
    var tabletCount: Int? = null,
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CenterDTO) return false
        val centerDTO = other
        if (this.id == null) {
            return false
        }
        return Objects.equals(this.id, centerDTO.id)
    }

    override fun hashCode() = Objects.hash(this.id)
}
