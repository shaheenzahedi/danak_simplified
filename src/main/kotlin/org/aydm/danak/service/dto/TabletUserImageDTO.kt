package org.aydm.danak.service.dto

import org.aydm.danak.domain.enumeration.TabletUserImageType
import java.io.Serializable
import java.util.*

/**
 * A DTO for the [org.aydm.danak.domain.TabletUserImage] entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
data class TabletUserImageDTO(

    var id: Long? = null,

    var tabletUserImageType: TabletUserImageType? = null,

    var file: UploadedFileDTO? = null,

    var tabletUser: TabletUserDTO? = null
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TabletUserImageDTO) return false
        val tabletUserImageDTO = other
        if (this.id == null) {
            return false
        }
        return Objects.equals(this.id, tabletUserImageDTO.id)
    }

    override fun hashCode() = Objects.hash(this.id)
}
