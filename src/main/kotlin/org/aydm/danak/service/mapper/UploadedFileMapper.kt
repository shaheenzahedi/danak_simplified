package org.aydm.danak.service.mapper

import org.aydm.danak.domain.UploadedFile
import org.aydm.danak.service.dto.UploadedFileDTO
import org.mapstruct.Mapper

/**
 * Mapper for the entity [UploadedFile] and its DTO [UploadedFileDTO].
 */
@Mapper(componentModel = "spring")
interface UploadedFileMapper :
    EntityMapper<UploadedFileDTO, UploadedFile>
