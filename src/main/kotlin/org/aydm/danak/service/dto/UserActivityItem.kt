package org.aydm.danak.service.dto

data class UserActivityItem(
    val activityId:Long?,
    val tabletUserId: Long?,
    val firstName: String?,
    val lastName: String?,
    val tabletId: String?,
    val tabletName: String?,

    val displayListName: String?,
    val listName: String?,
    val totals: Long?,
    val completes: Long?
)
