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

    @OneToMany(mappedBy = "activity")
    @JsonIgnoreProperties(
        value = [
            "activity",
        ],
        allowSetters = true
    )
    var userActivities: MutableSet<UserActivity>? = mutableSetOf(),

    @ManyToOne
    @JsonIgnoreProperties(
        value = [
            "tabletUsers",
        ],
        allowSetters = true
    )
    var tablet: Tablet? = null,
    // jhipster-needle-entity-add-field - JHipster will add fields here
) : Serializable {

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

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TabletUser) return false
        return id != null && other.id != null && id == other.id
    }

    @Override
    override fun toString(): String {
        return "TabletUser{" +
            "id=" + id +
            ", createTimeStamp='" + createTimeStamp + "'" +
            ", updateTimeStamp='" + updateTimeStamp + "'" +
            ", firstName='" + firstName + "'" +
            ", lastName='" + lastName + "'" +
            ", email='" + email + "'" +
            "}"
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}
