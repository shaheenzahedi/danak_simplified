package org.aydm.danak.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable
import java.time.Instant
import javax.persistence.*

/**
 * A TabletLog.
 */

@Entity
@Table(name = "tablet_log")
@SuppressWarnings("common-java:DuplicatedBlocks")
data class TabletLog(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long? = null,

    @Column(name = "create_time_stamp")
    var createTimeStamp: Instant? = null,

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

    fun tablet(tablet: Tablet?): TabletLog {
        this.tablet = tablet
        return this
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TabletLog) return false
        return id != null && other.id != null && id == other.id
    }

    override fun toString(): String {
        return "TabletLog{" +
            "id=" + id +
            ", createTimeStamp='" + createTimeStamp + "'" +
            "}"
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}
