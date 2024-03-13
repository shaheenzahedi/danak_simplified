package org.aydm.danak.service.mapper

import org.aydm.danak.domain.File
import org.aydm.danak.domain.FileBelongings
import org.aydm.danak.domain.Version
import org.aydm.danak.service.dto.FileBelongingsDTO
import org.aydm.danak.service.dto.FileDTO
import org.aydm.danak.service.dto.VersionDTO
import org.mapstruct.*

/**
 * Mapper for the entity [FileBelongings] and its DTO [FileBelongingsDTO].
 */
@Mapper(componentModel = "spring")
interface FileBelongingsMapper :
    EntityMapper<FileBelongingsDTO, FileBelongings> {

    @Mappings(
        Mapping(target = "file", source = "file", qualifiedByName = ["fileId"]),
        Mapping(target = "version", source = "version", qualifiedByName = ["versionId"])
    )
    override fun toDto(s: FileBelongings): FileBelongingsDTO

    @Named("fileId")
    @BeanMapping(ignoreByDefault = true)

    @Mappings(
        Mapping(target = "id", source = "id")
    )
    fun toDtoFileId(file: File): FileDTO

    @Named("versionId")
    @BeanMapping(ignoreByDefault = true)

    @Mappings(
        Mapping(target = "id", source = "id")
    )
    fun toDtoVersionId(version: Version): VersionDTO
}
