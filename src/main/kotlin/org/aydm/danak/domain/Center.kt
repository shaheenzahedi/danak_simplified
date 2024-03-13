package org.aydm.danak.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.aydm.danak.domain.enumeration.CenterType
import java.io.Serializable
import java.time.Instant
import javax.persistence.*

/**
 * A Center.
 */

@Entity
@Table(name = "center")
@SuppressWarnings("common-java:DuplicatedBlocks")
data class Center(

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

    @Column(name = "archived")
    var archived: Boolean? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "center_type")
    var centerType: CenterType? = null,

    @Column(name = "latitude")
    var latitude: Long? = null,

    @Column(name = "longitude")
    var longitude: Long? = null,

    // jhipster-needle-entity-add-field - JHipster will add fields here
) : Serializable {

    @OneToMany(mappedBy = "center")
    @JsonIgnoreProperties(
        value = [
            "center",
            "donor",
        ],
        allowSetters = true
    )
    var centerDonors: MutableSet<CenterDonor>? = mutableSetOf()

    @OneToMany(mappedBy = "center")
    @JsonIgnoreProperties(
        value = [
            "file",
            "center",
        ],
        allowSetters = true
    )
    var centerImages: MutableSet<CenterImage>? = mutableSetOf()

    @OneToMany(mappedBy = "center")
    @JsonIgnoreProperties(
        value = [
            "center",
            "user",
        ],
        allowSetters = true
    )
    var centerWatchLists: MutableSet<CenterWatchList>? = mutableSetOf()

    @OneToMany(mappedBy = "center")
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
    var archivedBy: User? = null

    @ManyToOne
    var createdBy: User? = null

    @ManyToOne
    var modifiedBy: User? = null

    fun addCenterDonor(centerDonor: CenterDonor): Center {
        this.centerDonors?.add(centerDonor)
        centerDonor.center = this
        return this
    }

    fun removeCenterDonor(centerDonor: CenterDonor): Center {
        this.centerDonors?.remove(centerDonor)
        centerDonor.center = null
        return this
    }

    fun addCenterImage(centerImage: CenterImage): Center {
        this.centerImages?.add(centerImage)
        centerImage.center = this
        return this
    }

    fun removeCenterImage(centerImage: CenterImage): Center {
        this.centerImages?.remove(centerImage)
        centerImage.center = null
        return this
    }

    fun addCenterWatchList(centerWatchList: CenterWatchList): Center {
        this.centerWatchLists?.add(centerWatchList)
        centerWatchList.center = this
        return this
    }

    fun removeCenterWatchList(centerWatchList: CenterWatchList): Center {
        this.centerWatchLists?.remove(centerWatchList)
        centerWatchList.center = null
        return this
    }

    fun addTablet(tablet: Tablet): Center {
        this.tablets?.add(tablet)
        tablet.center = this
        return this
    }

    fun removeTablet(tablet: Tablet): Center {
        this.tablets?.remove(tablet)
        tablet.center = null
        return this
    }

    fun archivedBy(user: User?): Center {
        this.archivedBy = user
        return this
    }

    fun createdBy(user: User?): Center {
        this.createdBy = user
        return this
    }

    fun modifiedBy(user: User?): Center {
        this.modifiedBy = user
        return this
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Center) return false
        return id != null && other.id != null && id == other.id
    }

    override fun toString(): String {
        return "Center{" +
            "id=" + id +
            ", createTimeStamp='" + createTimeStamp + "'" +
            ", updateTimeStamp='" + updateTimeStamp + "'" +
            ", name='" + name + "'" +
            ", city='" + city + "'" +
            ", country='" + country + "'" +
            ", archived='" + archived + "'" +
            ", centerType='" + centerType + "'" +
            ", latitude=" + latitude +
            ", longitude=" + longitude +
            "}"
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}
