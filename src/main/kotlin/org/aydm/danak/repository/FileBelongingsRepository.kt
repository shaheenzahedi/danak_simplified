package org.aydm.danak.repository

import org.aydm.danak.domain.FileBelongings
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

/**
 * Spring Data SQL repository for the [FileBelongings] entity.
 */
@Suppress("unused")
@Repository
interface FileBelongingsRepository : JpaRepository<FileBelongings, Long> {
}
