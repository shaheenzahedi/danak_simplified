package org.aydm.danak.repository

import org.aydm.danak.domain.Tablet
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

/**
 * Spring Data SQL repository for the [Tablet] entity.
 */
@Suppress("unused")
@Repository
interface TabletRepository : JpaRepository<Tablet, Long> {
    fun findByName(tabletName: String): Optional<Tablet>
//    @Query("select t.id from Tablet t where t.androidId is not null")
    @Query("select t.id from Tablet t")
    fun findAllRegistered(): List<Long>?
}
