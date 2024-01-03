package org.aydm.danak.repository

import org.aydm.danak.domain.Tablet
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

/**
 * Spring Data SQL repository for the [Tablet] entity.
 */
@Suppress("unused")
@Repository
interface TabletRepository : JpaRepository<Tablet, Long>, JpaSpecificationExecutor<Tablet> {
    fun findByName(tabletName: String): Optional<Tablet>
//    @Query("select t.id from Tablet t where t.androidId is not null")
    @Query("select t.id from Tablet t")
    fun findAllRegistered(): List<Long>?
    @Query("SELECT t FROM Tablet t WHERE t.name IN (SELECT name FROM Tablet GROUP BY name HAVING COUNT(name) > 1)")
    fun findAllDuplicates(): MutableList<Tablet>

    @Query("SELECT t FROM Tablet t WHERE t.identifier is null OR t.identifier NOT LIKE 'T%'")
    fun findAllTabletsWithoutIdentifier(): MutableList<Tablet>
    @Query("SELECT t.id FROM Tablet t JOIN t.center c JOIN c.centerDonors cd WHERE cd.donor.id = :donorId")
    fun findAllTabletsByDonorId(@Param("donorId") donorId: Long): MutableList<Long>
}
