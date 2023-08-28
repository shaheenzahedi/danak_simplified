package org.aydm.danak.domain

import javax.persistence.*
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

import java.io.Serializable
import java.time.Instant


/**
 * A UserActivity.
 */
  
@Entity
@Table(name = "user_activity")
data class UserActivity(

    
      
  
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
        var id: Long? = null,
      
  
    @Column(name = "create_time_stamp")
        var createTimeStamp: Instant? = null,
      
  
    @Column(name = "update_time_stamp")
        var updateTimeStamp: Instant? = null,
      
  
    @Column(name = "device_time_stamp")
        var deviceTimeStamp: Instant? = null,
      
  
    @Column(name = "list_name")
        var listName: String? = null,
      
  
    @Column(name = "total")
        var total: Long? = null,
      
  
    @Column(name = "completed")
        var completed: Long? = null,
      
  
    @Column(name = "unique_name")
        var uniqueName: String? = null,
          
  
  
  
    @ManyToOne
    @JsonIgnoreProperties(value = [
        "userActivities",
        "tablet",
      ], allowSetters = true)
    var activity: TabletUser? = null,
    // jhipster-needle-entity-add-field - JHipster will add fields here
) : Serializable {

    
        fun activity(tabletUser: TabletUser?): UserActivity {
        this.activity = tabletUser
        return this
    }

        // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is UserActivity) return false
      return id != null && other.id != null && id == other.id
    }

    @Override
    override fun toString(): String {
        return "UserActivity{" +
            "id=" + id +
            ", createTimeStamp='" + createTimeStamp + "'" +
            ", updateTimeStamp='" + updateTimeStamp + "'" +
            ", deviceTimeStamp='" + deviceTimeStamp + "'" +
            ", listName='" + listName + "'" +
            ", total=" + total +
            ", completed=" + completed +
            ", uniqueName='" + uniqueName + "'" +
            "}";
    }

    companion object {
        private const val serialVersionUID = 1L
            }
}
