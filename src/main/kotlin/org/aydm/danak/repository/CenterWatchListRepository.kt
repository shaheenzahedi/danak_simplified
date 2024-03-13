package org.aydm.danak.repository

import org.aydm.danak.domain.CenterWatchList
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

/**
 * Spring Data JPA repository for the CenterWatchList entity.
 */
@Suppress("unused")
@Repository
interface CenterWatchListRepository : JpaRepository<CenterWatchList, Long>, JpaSpecificationExecutor<CenterWatchList> {

    @Query("select centerWatchList from CenterWatchList centerWatchList where centerWatchList.user.login = ?#{principal.username}")
    fun findByUserIsCurrentUser(): MutableList<CenterWatchList>
}
