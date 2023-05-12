package org.aydm.danak.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable
import javax.persistence.*

/**
 * A FileBelongings.
 */

@Entity
@Table(name = "file_belongings")
data class FileBelongings(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long? = null,

    @ManyToOne
    @JsonIgnoreProperties(
        value = [
            "fileBelongings",
            "placement",
        ],
        allowSetters = true
    )
    var file: File? = null,

    @ManyToOne
    @JsonIgnoreProperties(
        value = [
            "fileBelongings",
            "files",
        ],
        allowSetters = true
    )
    var version: Version? = null,
    // jhipster-needle-entity-add-field - JHipster will add fields here
) : Serializable {

    fun file(file: File?): FileBelongings {
        this.file = file
        return this
    }
    fun version(version: Version?): FileBelongings {
        this.version = version
        return this
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FileBelongings) return false
        return id != null && other.id != null && id == other.id
    }

    @Override
    override fun toString(): String {
        return "FileBelongings{" +
            "id=" + id +
            "}"
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}
