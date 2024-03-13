package org.aydm.danak.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.aydm.danak.domain.enumeration.CenterImageType
import java.io.Serializable
import javax.persistence.*

/**
 * A CenterImage.
 */

@Entity
@Table(name = "center_image")
@SuppressWarnings("common-java:DuplicatedBlocks")
data class CenterImage(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "center_image_type")
    var centerImageType: CenterImageType? = null,

    // jhipster-needle-entity-add-field - JHipster will add fields here
) : Serializable {

    @JsonIgnoreProperties(
        value = [
            "fileShares",
        ],
        allowSetters = true
    )
    @OneToOne
    @JoinColumn(unique = true)
    var file: UploadedFile? = null

    @ManyToOne
    @JsonIgnoreProperties(
        value = [
            "centerDonors",
            "centerImages",
            "centerWatchLists",
            "tablets",
            "archivedBy",
            "createdBy",
            "modifiedBy",
        ],
        allowSetters = true
    )
    var center: Center? = null

    fun file(uploadedFile: UploadedFile?): CenterImage {
        this.file = uploadedFile
        return this
    }

    fun center(center: Center?): CenterImage {
        this.center = center
        return this
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CenterImage) return false
        return id != null && other.id != null && id == other.id
    }

    override fun toString(): String {
        return "CenterImage{" +
            "id=" + id +
            ", centerImageType='" + centerImageType + "'" +
            "}"
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}
