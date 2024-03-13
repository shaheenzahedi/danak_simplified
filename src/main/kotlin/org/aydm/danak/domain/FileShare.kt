package org.aydm.danak.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable
import javax.persistence.*

/**
 * A FileShare.
 */

@Entity
@Table(name = "file_share")
@SuppressWarnings("common-java:DuplicatedBlocks")
data class FileShare(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long? = null,

    // jhipster-needle-entity-add-field - JHipster will add fields here
) : Serializable {

    @ManyToOne
    @JsonIgnoreProperties(
        value = [
            "fileShares",
        ],
        allowSetters = true
    )
    var file: UploadedFile? = null

    @ManyToOne
    var user: User? = null

    fun file(uploadedFile: UploadedFile?): FileShare {
        this.file = uploadedFile
        return this
    }

    fun user(user: User?): FileShare {
        this.user = user
        return this
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FileShare) return false
        return id != null && other.id != null && id == other.id
    }

    override fun toString(): String {
        return "FileShare{" +
            "id=" + id +
            "}"
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}
