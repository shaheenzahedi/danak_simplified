package org.aydm.danak.service.mapper

import org.aydm.danak.domain.TabletUser
import org.aydm.danak.domain.TabletUserImage
import org.aydm.danak.domain.UploadedFile
import org.aydm.danak.service.dto.TabletUserDTO
import org.aydm.danak.service.dto.TabletUserImageDTO
import org.aydm.danak.service.dto.UploadedFileDTO
import org.mapstruct.*

/**
 * Mapper for the entity [TabletUserImage] and its DTO [TabletUserImageDTO].
 */
@Mapper(componentModel = "spring")
interface TabletUserImageMapper :
    EntityMapper<TabletUserImageDTO, TabletUserImage> {

    @Mappings(
        Mapping(target = "file", source = "file", qualifiedByName = ["uploadedFileId"]),
        Mapping(target = "tabletUser", source = "tabletUser", qualifiedByName = ["tabletUserId"])
    )
    override fun toDto(s: TabletUserImage): TabletUserImageDTO

    @Named("uploadedFileId")
    @BeanMapping(ignoreByDefault = true)

    @Mappings(
        Mapping(target = "id", source = "id")
    )
    fun toDtoUploadedFileId(uploadedFile: UploadedFile): UploadedFileDTO

    @Named("tabletUserId")
    @BeanMapping(ignoreByDefault = true)

    @Mappings(
        Mapping(target = "id", source = "id")
    )
    fun toDtoTabletUserId(tabletUser: TabletUser): TabletUserDTO
}
