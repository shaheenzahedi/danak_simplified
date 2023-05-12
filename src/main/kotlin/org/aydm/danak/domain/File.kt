package org.aydm.danak.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable
import javax.persistence.*

/**
 * A File.
 */

@Entity
@Table(name = "file")
data class File(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long? = null,

    @Column(name = "name")
    var name: String? = null,

    @Column(name = "checksum")
    var checksum: String? = null,

    @Column(name = "path")
    var path: String? = null,

    @OneToMany(mappedBy = "file")
    @JsonIgnoreProperties(
        value = [
            "file",
            "version",
        ],
        allowSetters = true
    )
    var fileBelongings: MutableSet<FileBelongings>? = mutableSetOf(),

    @ManyToOne
    @JsonIgnoreProperties(
        value = [
            "fileBelongings",
            "files",
        ],
        allowSetters = true
    )
    var placement: Version? = null,
    // jhipster-needle-entity-add-field - JHipster will add fields here
) : Serializable {

    fun addFileBelongings(fileBelongings: FileBelongings): File {
        this.fileBelongings?.add(fileBelongings)
        fileBelongings.file = this
        return this
    }
    fun removeFileBelongings(fileBelongings: FileBelongings): File {
        this.fileBelongings?.remove(fileBelongings)
        fileBelongings.file = null
        return this
    }
    fun placement(version: Version?): File {
        this.placement = version
        return this
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is File) return false
        return id != null && other.id != null && id == other.id
    }

    @Override
    override fun toString(): String {
        return "File{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", checksum='" + checksum + "'" +
            ", path='" + path + "'" +
            "}"
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}
