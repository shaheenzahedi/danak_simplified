package org.aydm.danak.repository

import org.aydm.danak.domain.Version
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.Optional

/**
 * Spring Data SQL repository for the [Version] entity.
 */
@Suppress("unused")
@Repository
interface VersionRepository : JpaRepository<Version, Long> {
    fun findByVersion(@Param("version") fromVersion: Int): Version
    fun findByTag(tag: String): Optional<Version>

    @Query("SELECT e FROM Version e WHERE " +
        "e.id = (SELECT MAX(e2.id) FROM Version e2 " +
        "WHERE e2.tag NOT LIKE '%qa%' " +
        "AND e2.tag NOT LIKE '%alpha%' " +
        "AND e2.tag NOT LIKE '%beta%' " +
        "AND e2.tag NOT LIKE '%rc%')")
    fun findLastVersion(): Optional<Version>
}
