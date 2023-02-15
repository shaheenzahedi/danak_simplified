package org.web.danak.service.dto

import org.aydm.danak.service.dto.SubmitActivityDTO

data class SubmitUserDTO(
    val firstName: String,
    val lastName: String,
    val activity: List<SubmitActivityDTO>
)
