package org.aydm.danak.service.mapper

import org.aydm.danak.domain.Center
import org.aydm.danak.domain.User
import org.aydm.danak.service.dto.CenterDTO
import org.aydm.danak.service.dto.UserDTO
import org.mapstruct.*

/**
 * Mapper for the entity [Center] and its DTO [CenterDTO].
 */
@Mapper(componentModel = "spring")
interface CenterMapper :
    EntityMapper<CenterDTO, Center> {

    @Mappings(
        Mapping(target = "archivedBy", source = "archivedBy", qualifiedByName = ["userId"]),
        Mapping(target = "createdBy", source = "createdBy", qualifiedByName = ["userId"]),
        Mapping(target = "modifiedBy", source = "modifiedBy", qualifiedByName = ["userId"])
    )
    override fun toDto(s: Center): CenterDTO

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)

    @Mappings(
        Mapping(target = "id", source = "id")
    )
    fun toDtoUserId(user: User): UserDTO
}
