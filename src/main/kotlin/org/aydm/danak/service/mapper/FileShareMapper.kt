package org.aydm.danak.service.mapper

import org.aydm.danak.domain.FileShare
import org.aydm.danak.domain.UploadedFile
import org.aydm.danak.domain.User
import org.aydm.danak.service.dto.FileShareDTO
import org.aydm.danak.service.dto.UploadedFileDTO
import org.aydm.danak.service.dto.UserDTO
import org.mapstruct.*

/**
 * Mapper for the entity [FileShare] and its DTO [FileShareDTO].
 */
@Mapper(componentModel = "spring")
interface FileShareMapper :
    EntityMapper<FileShareDTO, FileShare> {

    @Mappings(
        Mapping(target = "file", source = "file", qualifiedByName = ["uploadedFileId"]),
        Mapping(target = "user", source = "user", qualifiedByName = ["userId"])
    )
    override fun toDto(s: FileShare): FileShareDTO

    @Named("uploadedFileId")
    @BeanMapping(ignoreByDefault = true)

    @Mappings(
        Mapping(target = "id", source = "id")
    )
    fun toDtoUploadedFileId(uploadedFile: UploadedFile): UploadedFileDTO

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)

    @Mappings(
        Mapping(target = "id", source = "id")
    )
    fun toDtoUserId(user: User): UserDTO
}
