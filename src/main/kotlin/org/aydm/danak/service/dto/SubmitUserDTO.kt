package org.web.danak.service.dto

data class SubmitUserDTO(
    val firstName: String,
    val lastName: String,
    val activity: List<SubmitActivityDTO>
)
