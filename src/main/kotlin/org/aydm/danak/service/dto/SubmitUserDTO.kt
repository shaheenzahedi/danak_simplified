package org.web.danak.service.dto

import org.aydm.danak.service.dto.SubmitActivityDTO

data class SubmitUserDTO(
    var userId:Long?=null,
    val firstName: String,
    val lastName: String,
    val activity: List<SubmitActivityDTO>
)
