package org.aydm.danak.domain

import org.aydm.danak.domain.enumeration.PanelLogType
import java.io.Serializable
import java.time.Instant
import javax.persistence.*

/**
 * A PanelLog.
 */

@Entity
@Table(name = "panel_log")
@SuppressWarnings("common-java:DuplicatedBlocks")
data class PanelLog(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long? = null,

    @Column(name = "create_time_stamp")
    var createTimeStamp: Instant? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "panel_log_type")
    var panelLogType: PanelLogType? = null,

    // jhipster-needle-entity-add-field - JHipster will add fields here
) : Serializable {

    @ManyToOne
    var user: User? = null

    fun user(user: User?): PanelLog {
        this.user = user
        return this
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PanelLog) return false
        return id != null && other.id != null && id == other.id
    }

    override fun toString(): String {
        return "PanelLog{" +
            "id=" + id +
            ", createTimeStamp='" + createTimeStamp + "'" +
            ", panelLogType='" + panelLogType + "'" +
            "}"
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}
