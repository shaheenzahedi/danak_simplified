package org.aydm.danak.service.dto

import java.time.LocalDate

data class DashboardDTO(
    val numberOfTablets: Long = 0,
    val numberOfUsers: Long = 0,
    val numberOfCenters: Long = 0,
    val numberOfReports: Long = 0,
    val reports: Map<LocalDate, Long>
)
