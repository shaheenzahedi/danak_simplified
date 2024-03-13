package org.aydm.danak.service.mapper

import org.aydm.danak.domain.Center
import org.aydm.danak.domain.CenterImage
import org.aydm.danak.domain.UploadedFile
import org.aydm.danak.service.dto.CenterDTO
import org.aydm.danak.service.dto.CenterImageDTO
import org.aydm.danak.service.dto.UploadedFileDTO
import org.mapstruct.*

/**
 * Mapper for the entity [CenterImage] and its DTO [CenterImageDTO].
 */
@Mapper(componentModel = "spring")
interface CenterImageMapper :
    EntityMapper<CenterImageDTO, CenterImage> {

    @Mappings(
        Mapping(target = "file", source = "file", qualifiedByName = ["uploadedFileId"]),
        Mapping(target = "center", source = "center", qualifiedByName = ["centerId"])
    )
    override fun toDto(s: CenterImage): CenterImageDTO

    @Named("uploadedFileId")
    @BeanMapping(ignoreByDefault = true)

    @Mappings(
        Mapping(target = "id", source = "id")
    )
    fun toDtoUploadedFileId(uploadedFile: UploadedFile): UploadedFileDTO

    @Named("centerId")
    @BeanMapping(ignoreByDefault = true)

    @Mappings(
        Mapping(target = "id", source = "id")
    )
    fun toDtoCenterId(center: Center): CenterDTO
}
