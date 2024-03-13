package org.aydm.danak.repository

import org.aydm.danak.domain.FileShare
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

/**
 * Spring Data JPA repository for the FileShare entity.
 */
@Suppress("unused")
@Repository
interface FileShareRepository : JpaRepository<FileShare, Long>, JpaSpecificationExecutor<FileShare> {

    @Query("select fileShare from FileShare fileShare where fileShare.user.login = ?#{principal.username}")
    fun findByUserIsCurrentUser(): MutableList<FileShare>
}
