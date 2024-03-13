package org.aydm.danak.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable
import java.time.Instant
import javax.persistence.*

/**
 * A UploadedFile.
 */

@Entity
@Table(name = "uploaded_file")
@SuppressWarnings("common-java:DuplicatedBlocks")
data class UploadedFile(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long? = null,

    @Column(name = "create_time_stamp")
    var createTimeStamp: Instant? = null,

    @Column(name = "delete_time_stamp")
    var deleteTimeStamp: Instant? = null,

    @Column(name = "is_public")
    var isPublic: Boolean? = null,

    @Column(name = "name")
    var name: String? = null,

    @Column(name = "path")
    var path: String? = null,

    @Column(name = "update_time_stamp")
    var updateTimeStamp: Instant? = null,

    // jhipster-needle-entity-add-field - JHipster will add fields here
) : Serializable {

    @OneToMany(mappedBy = "file")
    @JsonIgnoreProperties(
        value = [
            "file",
            "user",
        ],
        allowSetters = true
    )
    var fileShares: MutableSet<FileShare>? = mutableSetOf()

    fun addFileShare(fileShare: FileShare): UploadedFile {
        this.fileShares?.add(fileShare)
        fileShare.file = this
        return this
    }

    fun removeFileShare(fileShare: FileShare): UploadedFile {
        this.fileShares?.remove(fileShare)
        fileShare.file = null
        return this
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is UploadedFile) return false
        return id != null && other.id != null && id == other.id
    }

    override fun toString(): String {
        return "UploadedFile{" +
            "id=" + id +
            ", createTimeStamp='" + createTimeStamp + "'" +
            ", deleteTimeStamp='" + deleteTimeStamp + "'" +
            ", isPublic='" + isPublic + "'" +
            ", name='" + name + "'" +
            ", path='" + path + "'" +
            ", updateTimeStamp='" + updateTimeStamp + "'" +
            "}"
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}
