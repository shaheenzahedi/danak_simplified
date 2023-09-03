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
data class Tablet(

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

    @Column(name = "identifier")
    var identifier: String? = null,

    @Column(name = "model")
    var model: String? = null,

    @OneToMany(mappedBy = "tablet")
    @JsonIgnoreProperties(
        value = [
            "userActivities",
            "tablet",
        ],
        allowSetters = true
    )
    var tabletUsers: MutableSet<TabletUser>? = mutableSetOf(),

    @ManyToOne
    @JsonIgnoreProperties(
        value = [
            "tablets",
        ],
        allowSetters = true
    )
    var center: Center? = null,

    @ManyToOne
    @JsonIgnoreProperties(
        value = [
            "user",
            "tablets",
        ],
        allowSetters = true
    )
    var donor: Donor? = null,
    // jhipster-needle-entity-add-field - JHipster will add fields here
) : Serializable {

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
    fun center(center: Center?): Tablet {
        this.center = center
        return this
    }
    fun donor(donor: Donor?): Tablet {
        this.donor = donor
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

    @Override
    override fun toString(): String {
        return "Tablet{" +
            "id=" + id +
            ", createTimeStamp='" + createTimeStamp + "'" +
            ", updateTimeStamp='" + updateTimeStamp + "'" +
            ", name='" + name + "'" +
            ", identifier='" + identifier + "'" +
            ", model='" + model + "'" +
            "}"
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}
