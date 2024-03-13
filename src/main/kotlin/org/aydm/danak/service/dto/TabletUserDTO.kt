package org.aydm.danak.service.dto

import java.io.Serializable
import java.time.Instant
import java.util.*

/**
 * A DTO for the [org.aydm.danak.domain.TabletUser] entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
data class TabletUserDTO(

    var id: Long? = null,

    var createTimeStamp: Instant? = null,

    var updateTimeStamp: Instant? = null,

    var firstName: String? = null,

    var lastName: String? = null,

    var email: String? = null,

    var description: String? = null,

    var recoveryPhrase: String? = null,

    var archived: Boolean? = null,

    var tablet: TabletDTO? = null,

    var archivedBy: UserDTO? = null,

    var modifiedBy: UserDTO? = null
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
