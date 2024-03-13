package org.aydm.danak.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable
import java.time.Instant
import javax.persistence.*

/**
 * A TabletUser.
 */

@Entity
@Table(name = "tablet_user")
@SuppressWarnings("common-java:DuplicatedBlocks")
data class TabletUser(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long? = null,

    @Column(name = "create_time_stamp")
    var createTimeStamp: Instant? = null,

    @Column(name = "update_time_stamp")
    var updateTimeStamp: Instant? = null,

    @Column(name = "first_name")
    var firstName: String? = null,

    @Column(name = "last_name")
    var lastName: String? = null,

    @Column(name = "email")
    var email: String? = null,

    @Column(name = "description")
    var description: String? = null,

    @Column(name = "recovery_phrase")
    var recoveryPhrase: String? = null,

    @Column(name = "archived")
    var archived: Boolean? = null,

    // jhipster-needle-entity-add-field - JHipster will add fields here
) : Serializable {

    @OneToMany(mappedBy = "tabletUser")
    @JsonIgnoreProperties(
        value = [
            "file",
            "tabletUser",
        ],
        allowSetters = true
    )
    var tabletUserImages: MutableSet<TabletUserImage>? = mutableSetOf()

    @OneToMany(mappedBy = "tabletUser")
    @JsonIgnoreProperties(
        value = [
            "tabletUser",
            "user",
        ],
        allowSetters = true
    )
    var tabletUserWatchLists: MutableSet<TabletUserWatchList>? = mutableSetOf()

    @OneToMany(mappedBy = "activity")
    @JsonIgnoreProperties(
        value = [
            "activity",
        ],
        allowSetters = true
    )
    var userActivities: MutableSet<UserActivity>? = mutableSetOf()

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
    var archivedBy: User? = null

    @ManyToOne
    var modifiedBy: User? = null

    fun addTabletUserImage(tabletUserImage: TabletUserImage): TabletUser {
        this.tabletUserImages?.add(tabletUserImage)
        tabletUserImage.tabletUser = this
        return this
    }

    fun removeTabletUserImage(tabletUserImage: TabletUserImage): TabletUser {
        this.tabletUserImages?.remove(tabletUserImage)
        tabletUserImage.tabletUser = null
        return this
    }

    fun addTabletUserWatchList(tabletUserWatchList: TabletUserWatchList): TabletUser {
        this.tabletUserWatchLists?.add(tabletUserWatchList)
        tabletUserWatchList.tabletUser = this
        return this
    }

    fun removeTabletUserWatchList(tabletUserWatchList: TabletUserWatchList): TabletUser {
        this.tabletUserWatchLists?.remove(tabletUserWatchList)
        tabletUserWatchList.tabletUser = null
        return this
    }

    fun addUserActivity(userActivity: UserActivity): TabletUser {
        this.userActivities?.add(userActivity)
        userActivity.activity = this
        return this
    }

    fun removeUserActivity(userActivity: UserActivity): TabletUser {
        this.userActivities?.remove(userActivity)
        userActivity.activity = null
        return this
    }

    fun tablet(tablet: Tablet?): TabletUser {
        this.tablet = tablet
        return this
    }

    fun archivedBy(user: User?): TabletUser {
        this.archivedBy = user
        return this
    }

    fun modifiedBy(user: User?): TabletUser {
        this.modifiedBy = user
        return this
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TabletUser) return false
        return id != null && other.id != null && id == other.id
    }

    override fun toString(): String {
        return "TabletUser{" +
            "id=" + id +
            ", createTimeStamp='" + createTimeStamp + "'" +
            ", updateTimeStamp='" + updateTimeStamp + "'" +
            ", firstName='" + firstName + "'" +
            ", lastName='" + lastName + "'" +
            ", email='" + email + "'" +
            ", description='" + description + "'" +
            ", recoveryPhrase='" + recoveryPhrase + "'" +
            ", archived='" + archived + "'" +
            "}"
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}
