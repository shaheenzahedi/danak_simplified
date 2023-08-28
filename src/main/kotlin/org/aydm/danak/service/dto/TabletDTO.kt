package org.aydm.danak.service.dto

import java.util.Objects
import java.time.Instant
import java.io.Serializable


/**
 * A DTO for the [org.aydm.danak.domain.Tablet] entity.
 */
data class TabletDTO(

    var id: Long? = null,

    var createTimeStamp: Instant? = null,

    var updateTimeStamp: Instant? = null,

    var name: String? = null,

    var androidId: String? = null,

    var macId: String? = null,

    var model: String? = null
) : Serializable {


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TabletDTO) return false
        val tabletDTO = other
        if (this.id == null){
            return false;
        }
        return Objects.equals(this.id, tabletDTO.id);
    }

    override fun hashCode() =        Objects.hash(this.id)
}
