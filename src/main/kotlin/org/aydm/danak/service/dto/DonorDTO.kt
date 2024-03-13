package org.aydm.danak.service.dto

import org.aydm.danak.domain.enumeration.EducationType
import java.io.Serializable
import java.time.Instant
import java.util.*

/**
 * A DTO for the [org.aydm.danak.domain.Donor] entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
data class DonorDTO(

    var id: Long? = null,

    var createTimeStamp: Instant? = null,

    var updateTimeStamp: Instant? = null,

    var name: String? = null,

    var city: String? = null,

    var country: String? = null,

    var nationalCode: String? = null,

    var educationType: EducationType? = null,

    var education: String? = null,

    var occupation: String? = null,

    var workPlace: String? = null,

    var workPlacePhone: String? = null,

    var archived: Boolean? = null,

    var otpPhoneCode: Long? = null,

    var otpPhoneEnable: Boolean? = null,

    var otpPhoneSentTimeStamp: Instant? = null,

    var latitude: Long? = null,

    var longitude: Long? = null,

    var uid: String? = null,

    var user: UserDTO? = null,

    var archivedBy: UserDTO? = null,

    var createdBy: UserDTO? = null,

    var modifiedBy: UserDTO? = null
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DonorDTO) return false
        val donorDTO = other
        if (this.id == null) {
            return false
        }
        return Objects.equals(this.id, donorDTO.id)
    }

    override fun hashCode() = Objects.hash(this.id)
}
