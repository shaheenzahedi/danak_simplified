package org.aydm.danak.repository

import org.aydm.danak.domain.DonorWatchList
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

/**
 * Spring Data JPA repository for the DonorWatchList entity.
 */
@Suppress("unused")
@Repository
interface DonorWatchListRepository : JpaRepository<DonorWatchList, Long>, JpaSpecificationExecutor<DonorWatchList> {

    @Query("select donorWatchList from DonorWatchList donorWatchList where donorWatchList.user.login = ?#{principal.username}")
    fun findByUserIsCurrentUser(): MutableList<DonorWatchList>
}
