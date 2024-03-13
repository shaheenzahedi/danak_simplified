package org.aydm.danak.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.aydm.danak.domain.enumeration.DonorImageType
import java.io.Serializable
import javax.persistence.*

/**
 * A DonorImage.
 */

@Entity
@Table(name = "donor_image")
@SuppressWarnings("common-java:DuplicatedBlocks")
data class DonorImage(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "donor_image_type")
    var donorImageType: DonorImageType? = null,

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
            "donorImages",
            "donorWatchLists",
            "tablets",
            "user",
            "archivedBy",
            "createdBy",
            "modifiedBy",
        ],
        allowSetters = true
    )
    var donor: Donor? = null

    fun file(uploadedFile: UploadedFile?): DonorImage {
        this.file = uploadedFile
        return this
    }

    fun donor(donor: Donor?): DonorImage {
        this.donor = donor
        return this
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DonorImage) return false
        return id != null && other.id != null && id == other.id
    }

    override fun toString(): String {
        return "DonorImage{" +
            "id=" + id +
            ", donorImageType='" + donorImageType + "'" +
            "}"
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}
