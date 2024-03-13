package org.aydm.danak.repository

import org.aydm.danak.domain.TabletUserWatchList
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

/**
 * Spring Data JPA repository for the TabletUserWatchList entity.
 */
@Suppress("unused")
@Repository
interface TabletUserWatchListRepository :
    JpaRepository<TabletUserWatchList, Long>,
    JpaSpecificationExecutor<TabletUserWatchList> {

    @Query("select tabletUserWatchList from TabletUserWatchList tabletUserWatchList where tabletUserWatchList.user.login = ?#{principal.username}")
    fun findByUserIsCurrentUser(): MutableList<TabletUserWatchList>
}
