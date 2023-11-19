package org.aydm.danak.service.dto

import java.io.Serializable
import java.time.Instant
import java.util.Objects

/**
 * A DTO for the [org.aydm.danak.domain.UserActivity] entity.
 */
data class UserActivityDTO(

    var id: Long? = null,

    var createTimeStamp: Instant? = null,

    var updateTimeStamp: Instant? = null,

    var deviceTimeStamp: Instant? = null,

    var listName: String? = null,

    var total: Long? = null,

    var completed: Long? = null,

    var uniqueName: String? = null,

    var activity: TabletUserDTO? = null
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is UserActivityDTO) return false
        val userActivityDTO = other
        if (this.id == null) {
            return false
        }
        return Objects.equals(this.id, userActivityDTO.id)
    }

    override fun hashCode() = Objects.hash(this.id)

    companion object
}
