package org.aydm.danak.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.aydm.danak.domain.enumeration.EducationType
import java.io.Serializable
import java.time.Instant
import javax.persistence.*

/**
 * A Donor.
 */

@Entity
@Table(name = "donor")
@SuppressWarnings("common-java:DuplicatedBlocks")
data class Donor(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long? = null,

    @Column(name = "create_time_stamp")
    var createTimeStamp: Instant? = null,

    @Column(name = "update_time_stamp")
    var updateTimeStamp: Instant? = null,

    @Column(name = "name")
    var name: String? = null,

    @Column(name = "city")
    var city: String? = null,

    @Column(name = "country")
    var country: String? = null,

    @Column(name = "national_code")
    var nationalCode: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "education_type")
    var educationType: EducationType? = null,

    @Column(name = "education")
    var education: String? = null,

    @Column(name = "occupation")
    var occupation: String? = null,

    @Column(name = "work_place")
    var workPlace: String? = null,

    @Column(name = "work_place_phone")
    var workPlacePhone: String? = null,

    @Column(name = "archived")
    var archived: Boolean? = null,

    @Column(name = "otp_phone_code")
    var otpPhoneCode: Long? = null,

    @Column(name = "otp_phone_enable")
    var otpPhoneEnable: Boolean? = null,

    @Column(name = "otp_phone_sent_time_stamp")
    var otpPhoneSentTimeStamp: Instant? = null,

    @Column(name = "latitude")
    var latitude: Long? = null,

    @Column(name = "longitude")
    var longitude: Long? = null,

    @Column(name = "uid")
    var uid: String? = null,

    // jhipster-needle-entity-add-field - JHipster will add fields here
) : Serializable {

    @OneToMany(mappedBy = "donor")
    @JsonIgnoreProperties(
        value = [
            "center",
            "donor",
        ],
        allowSetters = true
    )
    var centerDonors: MutableSet<CenterDonor>? = mutableSetOf()

    @OneToMany(mappedBy = "donor")
    @JsonIgnoreProperties(
        value = [
            "file",
            "donor",
        ],
        allowSetters = true
    )
    var donorImages: MutableSet<DonorImage>? = mutableSetOf()

    @OneToMany(mappedBy = "donor")
    @JsonIgnoreProperties(
        value = [
            "donor",
            "user",
        ],
        allowSetters = true
    )
    var donorWatchLists: MutableSet<DonorWatchList>? = mutableSetOf()

    @OneToMany(mappedBy = "donor")
    @JsonIgnoreProperties(
        value = [
            "tabletLogs",
            "tabletUsers",
            "tabletWatchLists",
            "center",
            "donor",
            "archivedBy",
            "modifiedBy",
        ],
        allowSetters = true
    )
    var tablets: MutableSet<Tablet>? = mutableSetOf()

    @ManyToOne
    var user: User? = null

    @ManyToOne
    var archivedBy: User? = null

    @ManyToOne
    var createdBy: User? = null

    @ManyToOne
    var modifiedBy: User? = null

    fun addCenterDonor(centerDonor: CenterDonor): Donor {
        this.centerDonors?.add(centerDonor)
        centerDonor.donor = this
        return this
    }

    fun removeCenterDonor(centerDonor: CenterDonor): Donor {
        this.centerDonors?.remove(centerDonor)
        centerDonor.donor = null
        return this
    }

    fun addDonorImage(donorImage: DonorImage): Donor {
        this.donorImages?.add(donorImage)
        donorImage.donor = this
        return this
    }

    fun removeDonorImage(donorImage: DonorImage): Donor {
        this.donorImages?.remove(donorImage)
        donorImage.donor = null
        return this
    }

    fun addDonorWatchList(donorWatchList: DonorWatchList): Donor {
        this.donorWatchLists?.add(donorWatchList)
        donorWatchList.donor = this
        return this
    }

    fun removeDonorWatchList(donorWatchList: DonorWatchList): Donor {
        this.donorWatchLists?.remove(donorWatchList)
        donorWatchList.donor = null
        return this
    }

    fun addTablet(tablet: Tablet): Donor {
        this.tablets?.add(tablet)
        tablet.donor = this
        return this
    }

    fun removeTablet(tablet: Tablet): Donor {
        this.tablets?.remove(tablet)
        tablet.donor = null
        return this
    }

    fun user(user: User?): Donor {
        this.user = user
        return this
    }

    fun archivedBy(user: User?): Donor {
        this.archivedBy = user
        return this
    }

    fun createdBy(user: User?): Donor {
        this.createdBy = user
        return this
    }

    fun modifiedBy(user: User?): Donor {
        this.modifiedBy = user
        return this
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Donor) return false
        return id != null && other.id != null && id == other.id
    }

    override fun toString(): String {
        return "Donor{" +
            "id=" + id +
            ", createTimeStamp='" + createTimeStamp + "'" +
            ", updateTimeStamp='" + updateTimeStamp + "'" +
            ", name='" + name + "'" +
            ", city='" + city + "'" +
            ", country='" + country + "'" +
            ", nationalCode='" + nationalCode + "'" +
            ", educationType='" + educationType + "'" +
            ", education='" + education + "'" +
            ", occupation='" + occupation + "'" +
            ", workPlace='" + workPlace + "'" +
            ", workPlacePhone='" + workPlacePhone + "'" +
            ", archived='" + archived + "'" +
            ", otpPhoneCode=" + otpPhoneCode +
            ", otpPhoneEnable='" + otpPhoneEnable + "'" +
            ", otpPhoneSentTimeStamp='" + otpPhoneSentTimeStamp + "'" +
            ", latitude=" + latitude +
            ", longitude=" + longitude +
            ", uid='" + uid + "'" +
            "}"
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}
