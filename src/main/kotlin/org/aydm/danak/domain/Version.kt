package org.aydm.danak.domain

import javax.persistence.*
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

import java.io.Serializable


/**
 * A Version.
 */
  
@Entity
@Table(name = "version")
data class Version(

    
      
  
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
        var id: Long? = null,
      
  
    @Column(name = "version")
        var version: Int? = null,
          
  
  
  
    @OneToMany(mappedBy = "version")
            @JsonIgnoreProperties(value = [
                "file",
                "version",
            ], allowSetters = true)
    var fileBelongings: MutableSet<FileBelongings>? = mutableSetOf(),
      
  
  
  
    @OneToMany(mappedBy = "placement")
            @JsonIgnoreProperties(value = [
                "fileBelongings",
                "placement",
            ], allowSetters = true)
    var files: MutableSet<File>? = mutableSetOf(),
    // jhipster-needle-entity-add-field - JHipster will add fields here
) : Serializable {

    
                fun addFileBelongings(fileBelongings: FileBelongings) : Version {
        this.fileBelongings?.add(fileBelongings)
        fileBelongings.version = this
        return this;
    }
                fun removeFileBelongings(fileBelongings: FileBelongings) : Version{
        this.fileBelongings?.remove(fileBelongings)
        fileBelongings.version = null
        return this;
    }
                    fun addFile(file: File) : Version {
        this.files?.add(file)
        file.placement = this
        return this;
    }
                fun removeFile(file: File) : Version{
        this.files?.remove(file)
        file.placement = null
        return this;
    }
    
        // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is Version) return false
      return id != null && other.id != null && id == other.id
    }

    @Override
    override fun toString(): String {
        return "Version{" +
            "id=" + id +
            ", version=" + version +
            "}";
    }

    companion object {
        private const val serialVersionUID = 1L
            }
}
