package org.aydm.danak.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable
import javax.persistence.*

/**
 * A DonorWatchList.
 */

@Entity
@Table(name = "donor_watch_list")
@SuppressWarnings("common-java:DuplicatedBlocks")
data class DonorWatchList(

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

    @ManyToOne
    var user: User? = null

    fun donor(donor: Donor?): DonorWatchList {
        this.donor = donor
        return this
    }

    fun user(user: User?): DonorWatchList {
        this.user = user
        return this
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DonorWatchList) return false
        return id != null && other.id != null && id == other.id
    }

    override fun toString(): String {
        return "DonorWatchList{" +
            "id=" + id +
            "}"
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}
