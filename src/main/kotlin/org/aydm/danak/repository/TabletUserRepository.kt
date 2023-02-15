package org.aydm.danak.repository

import org.aydm.danak.domain.TabletUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

/**
 * Spring Data SQL repository for the [TabletUser] entity.
 */
@Suppress("unused")
@Repository
interface TabletUserRepository : JpaRepository<TabletUser, Long> {

    @Query("select tu from TabletUser tu where tu.firstName=:firstName AND tu.lastName=:lastName")
    fun findByNameAndFamily(firstName: String?, lastName: String?): Optional<TabletUser>

    @Query("select tu from TabletUser tu group by tu.firstName, tu.lastName")
    fun findAllByFirstLastNameImplicit(): List<TabletUser>
}
