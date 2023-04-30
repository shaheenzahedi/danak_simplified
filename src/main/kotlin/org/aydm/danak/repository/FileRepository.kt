package org.aydm.danak.repository

import org.aydm.danak.domain.File
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

/**
 * Spring Data SQL repository for the [File] entity.
 */
@Suppress("unused")
@Repository
interface FileRepository : JpaRepository<File, Long> {
    fun findAllByPlacementId(@Param("placementId") placementId: Long):MutableList<File>
}
