package org.aydm.danak.repository

import org.aydm.danak.domain.TabletWatchList
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

/**
 * Spring Data JPA repository for the TabletWatchList entity.
 */
@Suppress("unused")
@Repository
interface TabletWatchListRepository : JpaRepository<TabletWatchList, Long>, JpaSpecificationExecutor<TabletWatchList> {

    @Query("select tabletWatchList from TabletWatchList tabletWatchList where tabletWatchList.user.login = ?#{principal.username}")
    fun findByUserIsCurrentUser(): MutableList<TabletWatchList>
}
