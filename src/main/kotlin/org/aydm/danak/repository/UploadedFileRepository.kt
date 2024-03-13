package org.aydm.danak.repository

import org.aydm.danak.domain.UploadedFile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

/**
 * Spring Data JPA repository for the UploadedFile entity.
 */
@Suppress("unused")
@Repository
interface UploadedFileRepository : JpaRepository<UploadedFile, Long>, JpaSpecificationExecutor<UploadedFile>
