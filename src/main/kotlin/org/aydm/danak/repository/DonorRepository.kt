package org.aydm.danak.repository

import org.aydm.danak.domain.Donor
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

/**
 * Spring Data JPA repository for the Donor entity.
 */
@Suppress("unused")
@Repository
interface DonorRepository : JpaRepository<Donor, Long>, JpaSpecificationExecutor<Donor> {

    @Query("select donor from Donor donor where donor.user.login = ?#{principal.username}")
    fun findByUserIsCurrentUser(): MutableList<Donor>

    @Query("select donor from Donor donor where donor.archivedBy.login = ?#{principal.username}")
    fun findByArchivedByIsCurrentUser(): MutableList<Donor>

    @Query("select donor from Donor donor where donor.createdBy.login = ?#{principal.username}")
    fun findByCreatedByIsCurrentUser(): MutableList<Donor>

    @Query("select donor from Donor donor where donor.modifiedBy.login = ?#{principal.username}")
    fun findByModifiedByIsCurrentUser(): MutableList<Donor>
}
