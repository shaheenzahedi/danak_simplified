package org.aydm.danak.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable
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

    // jhipster-needle-entity-add-field - JHipster will add fields here
) : Serializable {

    @ManyToOne
    @JsonIgnoreProperties(
        value = [
            "centerDonors",
        ],
        allowSetters = true
    )
    var center: Center? = null

    @ManyToOne
    @JsonIgnoreProperties(
        value = [
            "centerDonors",
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
            "}"
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}
