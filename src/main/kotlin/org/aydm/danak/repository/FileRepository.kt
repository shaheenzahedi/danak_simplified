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
    fun findAllByPlacementId(@Param("placementId") placementId: Long): MutableList<File>

    @Query("SELECT f FROM File f WHERE f.id in (SELECT b.file.id FROM FileBelongings b WHERE b.version.id = :version)")
    fun findAllBelongsToVersion(@Param("version") version: Long): MutableList<File>

    @Query(
        "SELECT f FROM File f WHERE EXISTS " +
            "(SELECT b.file.id FROM FileBelongings b WHERE b.file.id = f.id AND b.version.id = :v1) " +
            "AND NOT EXISTS (SELECT b.file.id FROM FileBelongings b WHERE b.file.id = f.id AND b.version.id = :v2)"
    )
    fun findAllUpdates(@Param("v1") v1: Long, @Param("v2") v2: Long): MutableList<File>
}
