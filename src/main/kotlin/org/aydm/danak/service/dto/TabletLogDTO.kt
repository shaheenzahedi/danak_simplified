package org.aydm.danak.service.dto

import java.io.Serializable
import java.time.Instant
import java.util.*

/**
 * A DTO for the [org.aydm.danak.domain.TabletLog] entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
data class TabletLogDTO(

    var id: Long? = null,

    var createTimeStamp: Instant? = null,

    var tablet: TabletDTO? = null
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TabletLogDTO) return false
        val tabletLogDTO = other
        if (this.id == null) {
            return false
        }
        return Objects.equals(this.id, tabletLogDTO.id)
    }

    override fun hashCode() = Objects.hash(this.id)
}
