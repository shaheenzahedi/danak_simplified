package org.aydm.danak.service.mapper

import org.aydm.danak.domain.Tablet
import org.aydm.danak.domain.TabletUser
import org.aydm.danak.domain.User
import org.aydm.danak.service.dto.TabletDTO
import org.aydm.danak.service.dto.TabletUserDTO
import org.aydm.danak.service.dto.UserDTO
import org.mapstruct.*

/**
 * Mapper for the entity [TabletUser] and its DTO [TabletUserDTO].
 */
@Mapper(componentModel = "spring")
interface TabletUserMapper :
    EntityMapper<TabletUserDTO, TabletUser> {

    @Mappings(
        Mapping(target = "tablet", source = "tablet", qualifiedByName = ["tabletId"]),
        Mapping(target = "archivedBy", source = "archivedBy", qualifiedByName = ["userId"]),
        Mapping(target = "modifiedBy", source = "modifiedBy", qualifiedByName = ["userId"])
    )
    override fun toDto(s: TabletUser): TabletUserDTO

    @Named("tabletId")
    @BeanMapping(ignoreByDefault = true)

    @Mappings(
        Mapping(target = "id", source = "id")
    )
    fun toDtoTabletId(tablet: Tablet): TabletDTO

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)

    @Mappings(
        Mapping(target = "id", source = "id")
    )
    fun toDtoUserId(user: User): UserDTO
}
