package org.aydm.danak.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
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

    @OneToOne
    @JoinColumn(unique = true)
    var user: User? = null,
    @OneToMany(mappedBy = "donor")
    @JsonIgnoreProperties(
        value = [
            "tabletUsers",
            "center",
            "donor",
        ],
        allowSetters = true
    )
    var tablets: MutableSet<Tablet>? = mutableSetOf(),
    @OneToMany(mappedBy = "donor")
    @JsonIgnoreProperties(
        value = [
            "tabletUsers",
            "center",
            "donor",
        ],
        allowSetters = true
    )
    var centerDonors: MutableSet<CenterDonor>? = mutableSetOf()
    // jhipster-needle-entity-add-field - JHipster will add fields here
) : Serializable {

    fun user(user: User?): Donor {
        this.user = user
        return this
    }

    fun addTablet(tablet: Tablet): Donor {
        this.tablets?.add(tablet)
        tablet.donor = this
        return this
    }

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

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Donor) return false
        return id != null && other.id != null && id == other.id
    }

    @Override
    override fun toString(): String {
        return "Donor{" +
            "id=" + id +
            ", createTimeStamp='" + createTimeStamp + "'" +
            ", updateTimeStamp='" + updateTimeStamp + "'" +
            ", name='" + name + "'" +
            ", city='" + city + "'" +
            ", country='" + country + "'" +
            "}"
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}
