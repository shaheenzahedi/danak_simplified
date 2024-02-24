package org.aydm.danak.service.dto

data class OverallUserActivities(
    val tabletUserId: Long?,
    val firstName: String?,
    val lastName: String?,
    val tabletName: String?,
    val tabletId: Long?,
    val tabletIdentifier: String?,
    val center: CenterDTO?,
    val userActivities: List<UserActivityDTO>?
)

data class AggregatedUserActivity(
    val userActivityId: Long?,
    val displayListName: String?,
    val listName: String?,
    val totals: Long?,
    val completes: Long?
)
