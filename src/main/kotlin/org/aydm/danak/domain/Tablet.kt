package org.aydm.danak.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable
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

    @Column(name = "name")
    var name: String? = null,

    @Column(name = "android_id")
    var androidId: String? = null,

    @Column(name = "mac_id")
    var macId: String? = null,

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
            ", name='" + name + "'" +
            ", androidId='" + androidId + "'" +
            ", macId='" + macId + "'" +
            ", model='" + model + "'" +
            "}"
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}
