package org.aydm.danak.service.dto

import org.aydm.danak.domain.enumeration.CenterType
import java.io.Serializable
import java.time.Instant
import java.util.*

/**
 * A DTO for the [org.aydm.danak.domain.Center] entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
data class CenterDTO(

    var id: Long? = null,

    var createTimeStamp: Instant? = null,

    var updateTimeStamp: Instant? = null,

    var name: String? = null,

    var city: String? = null,

    var country: String? = null,

    var archived: Boolean? = null,

    var centerType: CenterType? = null,

    var latitude: Long? = null,

    var longitude: Long? = null,

    var archivedBy: UserDTO? = null,

    var createdBy: UserDTO? = null,

    var modifiedBy: UserDTO? = null,
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
