package org.aydm.danak.service.mapper

import org.aydm.danak.domain.File
import org.aydm.danak.domain.Version
import org.aydm.danak.service.dto.FileDTO
import org.aydm.danak.service.dto.VersionDTO
import org.mapstruct.*

/**
 * Mapper for the entity [File] and its DTO [FileDTO].
 */
@Mapper(componentModel = "spring")
interface FileMapper :
    EntityMapper<FileDTO, File> {

    @Mappings(
        Mapping(target = "placement", source = "placement", qualifiedByName = ["versionId"])
    )
    override fun toDto(s: File): FileDTO

    @Named("versionId")
    @BeanMapping(ignoreByDefault = true)

    @Mappings(
        Mapping(target = "id", source = "id")
    )
    fun toDtoVersionId(version: Version): VersionDTO
}
