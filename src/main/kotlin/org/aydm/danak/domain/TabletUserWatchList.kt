package org.aydm.danak.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable
import javax.persistence.*

/**
 * A TabletUserWatchList.
 */

@Entity
@Table(name = "tablet_user_watch_list")
@SuppressWarnings("common-java:DuplicatedBlocks")
data class TabletUserWatchList(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long? = null,

    // jhipster-needle-entity-add-field - JHipster will add fields here
) : Serializable {

    @ManyToOne
    @JsonIgnoreProperties(
        value = [
            "tabletUserImages",
            "tabletUserWatchLists",
            "userActivities",
            "tablet",
            "archivedBy",
            "modifiedBy",
        ],
        allowSetters = true
    )
    var tabletUser: TabletUser? = null

    @ManyToOne
    var user: User? = null

    fun tabletUser(tabletUser: TabletUser?): TabletUserWatchList {
        this.tabletUser = tabletUser
        return this
    }

    fun user(user: User?): TabletUserWatchList {
        this.user = user
        return this
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TabletUserWatchList) return false
        return id != null && other.id != null && id == other.id
    }

    override fun toString(): String {
        return "TabletUserWatchList{" +
            "id=" + id +
            "}"
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}
