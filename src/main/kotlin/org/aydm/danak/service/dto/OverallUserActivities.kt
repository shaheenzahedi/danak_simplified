package org.aydm.danak.service.dto

import com.fasterxml.jackson.annotation.JsonIgnore

data class OverallUserActivities(
    val tabletUserId: Long?,
    val firstName: String?,
    val lastName: String?,
    val tabletName: String?,
    val tabletId: Long?,
    val tabletIdentifier: String?,
    val tabletDescription: String?,
    val center: CenterDTO?,
    val userActivities: List<UserActivityDTO>?
) {
    @JsonIgnore
    fun getTitle(delimiter: Char): String {
        if (userActivities.isNullOrEmpty()) return ""
        return userActivities.joinToString(separator = delimiter.toString()) { "${it.listName} (${it.total})" }
    }

    @JsonIgnore
    fun getActivitiesAsCSVLine(delimiter: Char): String {
        if (userActivities.isNullOrEmpty()) return ""
        return userActivities.joinToString(separator = delimiter.toString()) { "${it.completed}" }
    }

    @JsonIgnore
    fun getActivitiesTimeStamp(): String {
        if (userActivities.isNullOrEmpty()) return ""
        return (userActivities.first().createTimeStamp ?: "").toString()
    }
}

data class AggregatedUserActivity(
    val userActivityId: Long?,
    val displayListName: String?,
    val listName: String?,
    val totals: Long?,
    val completes: Long?
)
