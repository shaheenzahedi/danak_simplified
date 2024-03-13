package org.aydm.danak.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable
import java.time.Instant
import javax.persistence.*

/**
 * A Tablet.
 */

@Entity
@Table(name = "tablet")
@SuppressWarnings("common-java:DuplicatedBlocks")
data class Tablet(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long? = null,

    @Column(name = "create_time_stamp")
    var createTimeStamp: Instant? = null,

    @Column(name = "update_time_stamp")
    var updateTimeStamp: Instant? = null,

    @Column(name = "identifier")
    var identifier: String? = null,

    @Column(name = "tag")
    var tag: String? = null,

    @Column(name = "name")
    var name: String? = null,

    @Column(name = "android_id")
    var androidId: String? = null,

    @Column(name = "model")
    var model: String? = null,

    @Column(name = "description")
    var description: String? = null,

    @Column(name = "archived")
    var archived: Boolean? = null,

    // jhipster-needle-entity-add-field - JHipster will add fields here
) : Serializable {

    @OneToMany(mappedBy = "tablet")
    @JsonIgnoreProperties(
        value = [
            "tablet",
        ],
        allowSetters = true
    )
    var tabletLogs: MutableSet<TabletLog>? = mutableSetOf()

    @OneToMany(mappedBy = "tablet")
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
    var tabletUsers: MutableSet<TabletUser>? = mutableSetOf()

    @OneToMany(mappedBy = "tablet")
    @JsonIgnoreProperties(
        value = [
            "tablet",
            "user",
        ],
        allowSetters = true
    )
    var tabletWatchLists: MutableSet<TabletWatchList>? = mutableSetOf()

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

    @ManyToOne
    var archivedBy: User? = null

    @ManyToOne
    var modifiedBy: User? = null

    fun addTabletLog(tabletLog: TabletLog): Tablet {
        this.tabletLogs?.add(tabletLog)
        tabletLog.tablet = this
        return this
    }

    fun removeTabletLog(tabletLog: TabletLog): Tablet {
        this.tabletLogs?.remove(tabletLog)
        tabletLog.tablet = null
        return this
    }

    fun addTabletUser(tabletUser: TabletUser): Tablet {
        this.tabletUsers?.add(tabletUser)
        tabletUser.tablet = this
        return this
    }

    fun removeTabletUser(tabletUser: TabletUser): Tablet {
        this.tabletUsers?.remove(tabletUser)
        tabletUser.tablet = null
        return this
    }

    fun addTabletWatchList(tabletWatchList: TabletWatchList): Tablet {
        this.tabletWatchLists?.add(tabletWatchList)
        tabletWatchList.tablet = this
        return this
    }

    fun removeTabletWatchList(tabletWatchList: TabletWatchList): Tablet {
        this.tabletWatchLists?.remove(tabletWatchList)
        tabletWatchList.tablet = null
        return this
    }

    fun center(center: Center?): Tablet {
        this.center = center
        return this
    }

    fun donor(donor: Donor?): Tablet {
        this.donor = donor
        return this
    }

    fun archivedBy(user: User?): Tablet {
        this.archivedBy = user
        return this
    }

    fun modifiedBy(user: User?): Tablet {
        this.modifiedBy = user
        return this
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Tablet) return false
        return id != null && other.id != null && id == other.id
    }

    override fun toString(): String {
        return "Tablet{" +
            "id=" + id +
            ", createTimeStamp='" + createTimeStamp + "'" +
            ", updateTimeStamp='" + updateTimeStamp + "'" +
            ", identifier='" + identifier + "'" +
            ", tag='" + tag + "'" +
            ", name='" + name + "'" +
            ", androidId='" + androidId + "'" +
            ", model='" + model + "'" +
            ", description='" + description + "'" +
            ", archived='" + archived + "'" +
            "}"
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}
