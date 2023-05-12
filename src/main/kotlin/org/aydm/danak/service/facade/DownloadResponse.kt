package org.aydm.danak.service.facade

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class DownloadResponse @JsonCreator constructor(
    @JsonProperty("files") val files: List<FileResponse>,
) {
}
