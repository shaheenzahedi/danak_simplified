package org.aydm.danak.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.aydm.danak.domain.enumeration.DonorType
import java.io.Serializable
import java.time.Instant
import javax.persistence.*

/**
 * A CenterDonor.
 */

@Entity
@Table(name = "center_donor")
@SuppressWarnings("common-java:DuplicatedBlocks")
data class CenterDonor(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long? = null,

    @Column(name = "joined_time_stamp")
    var joinedTimeStamp: Instant? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "donor_type")
    var donorType: DonorType? = null,

    // jhipster-needle-entity-add-field - JHipster will add fields here
) : Serializable {

    @ManyToOne
    @JsonIgnoreProperties(
        value = [
            "centerDonors",
            "centerImages",
            "centerWatchLists",
            "tablets",
            "archivedBy",
            "createdBy",
            "modifiedBy",
        ],
        allowSetters = true
    )
    var center: Center? = null

    @ManyToOne
    @JsonIgnoreProperties(
        value = [
            "centerDonors",
            "donorImages",
            "donorWatchLists",
            "tablets",
            "user",
            "archivedBy",
            "createdBy",
            "modifiedBy",
        ],
        allowSetters = true
    )
    var donor: Donor? = null

    fun center(center: Center?): CenterDonor {
        this.center = center
        return this
    }

    fun donor(donor: Donor?): CenterDonor {
        this.donor = donor
        return this
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CenterDonor) return false
        return id != null && other.id != null && id == other.id
    }

    override fun toString(): String {
        return "CenterDonor{" +
            "id=" + id +
            ", joinedTimeStamp='" + joinedTimeStamp + "'" +
            ", donorType='" + donorType + "'" +
            "}"
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}
