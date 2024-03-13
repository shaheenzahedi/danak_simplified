package org.aydm.danak.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable
import javax.persistence.*

/**
 * A TabletWatchList.
 */

@Entity
@Table(name = "tablet_watch_list")
@SuppressWarnings("common-java:DuplicatedBlocks")
data class TabletWatchList(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long? = null,

    // jhipster-needle-entity-add-field - JHipster will add fields here
) : Serializable {

    @ManyToOne
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
    var tablet: Tablet? = null

    @ManyToOne
    var user: User? = null

    fun tablet(tablet: Tablet?): TabletWatchList {
        this.tablet = tablet
        return this
    }

    fun user(user: User?): TabletWatchList {
        this.user = user
        return this
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TabletWatchList) return false
        return id != null && other.id != null && id == other.id
    }

    override fun toString(): String {
        return "TabletWatchList{" +
            "id=" + id +
            "}"
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}
