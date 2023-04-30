package org.aydm.danak.service.facade

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import org.aydm.danak.service.dto.FileDTO

data class DownloadResponse @JsonCreator constructor(
    @JsonProperty("files")  val files: List<FileResponse>,
){

}
