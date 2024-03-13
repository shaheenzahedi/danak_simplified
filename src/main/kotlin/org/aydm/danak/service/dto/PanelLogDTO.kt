package org.aydm.danak.service.dto

import org.aydm.danak.domain.enumeration.PanelLogType
import java.io.Serializable
import java.time.Instant
import java.util.*

/**
 * A DTO for the [org.aydm.danak.domain.PanelLog] entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
data class PanelLogDTO(

    var id: Long? = null,

    var createTimeStamp: Instant? = null,

    var panelLogType: PanelLogType? = null,

    var user: UserDTO? = null
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PanelLogDTO) return false
        val panelLogDTO = other
        if (this.id == null) {
            return false
        }
        return Objects.equals(this.id, panelLogDTO.id)
    }

    override fun hashCode() = Objects.hash(this.id)
}
