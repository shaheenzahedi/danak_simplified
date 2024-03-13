package org.aydm.danak.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable
import javax.persistence.*

/**
 * A CenterWatchList.
 */

@Entity
@Table(name = "center_watch_list")
@SuppressWarnings("common-java:DuplicatedBlocks")
data class CenterWatchList(

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
    var user: User? = null

    fun center(center: Center?): CenterWatchList {
        this.center = center
        return this
    }

    fun user(user: User?): CenterWatchList {
        this.user = user
        return this
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CenterWatchList) return false
        return id != null && other.id != null && id == other.id
    }

    override fun toString(): String {
        return "CenterWatchList{" +
            "id=" + id +
            "}"
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}
