package org.aydm.danak.service.facade

import org.aydm.danak.service.dto.FileDTO

data class UpdateResponse(
    val updates: List<FileDTO>,
    val deletes: List<FileDTO>
)
