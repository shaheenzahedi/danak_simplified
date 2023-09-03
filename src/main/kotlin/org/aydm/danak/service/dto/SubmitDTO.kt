package org.web.danak.service.dto

data class SubmitDTO(
    var tabletId: Long? = null,
    val tablet: String = "",
    var users: List<SubmitUserDTO> = emptyList()
) {
    constructor(tabletId: Long?, tablet: String) : this(tabletId, tablet, emptyList())
}
