package org.aydm.danak.service.dto

data class OverallUserActivities(
    val firstName: String?,
    val lastName: String?,
    val tabletName: String?,
    val userActivities: List<AggregatedUserActivity>?
)

data class AggregatedUserActivity(
    val tabletUserId: Long?,
    val displayListName: String?,
    val listName: String?,
    val totals: Long?,
    val completes: Long?
)
