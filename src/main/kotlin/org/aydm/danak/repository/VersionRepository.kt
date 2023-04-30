package org.aydm.danak.repository

import org.aydm.danak.domain.Version
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

/**
 * Spring Data SQL repository for the [Version] entity.
 */
@Suppress("unused")
@Repository
interface VersionRepository : JpaRepository<Version, Long> {
    fun findByVersion(@Param("version") fromVersion: Int):Version
}
