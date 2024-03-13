package org.aydm.danak.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.aydm.danak.domain.enumeration.TabletUserImageType
import java.io.Serializable
import javax.persistence.*

/**
 * A TabletUserImage.
 */

@Entity
@Table(name = "tablet_user_image")
@SuppressWarnings("common-java:DuplicatedBlocks")
data class TabletUserImage(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "tablet_user_image_type")
    var tabletUserImageType: TabletUserImageType? = null,

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
            "tabletUserImages",
            "tabletUserWatchLists",
            "userActivities",
            "tablet",
            "archivedBy",
            "modifiedBy",
        ],
        allowSetters = true
    )
    var tabletUser: TabletUser? = null

    fun file(uploadedFile: UploadedFile?): TabletUserImage {
        this.file = uploadedFile
        return this
    }

    fun tabletUser(tabletUser: TabletUser?): TabletUserImage {
        this.tabletUser = tabletUser
        return this
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TabletUserImage) return false
        return id != null && other.id != null && id == other.id
    }

    override fun toString(): String {
        return "TabletUserImage{" +
            "id=" + id +
            ", tabletUserImageType='" + tabletUserImageType + "'" +
            "}"
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}
