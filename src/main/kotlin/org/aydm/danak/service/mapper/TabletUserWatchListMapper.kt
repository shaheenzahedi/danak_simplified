package org.aydm.danak.service.mapper

import org.aydm.danak.domain.TabletUser
import org.aydm.danak.domain.TabletUserWatchList
import org.aydm.danak.domain.User
import org.aydm.danak.service.dto.TabletUserDTO
import org.aydm.danak.service.dto.TabletUserWatchListDTO
import org.aydm.danak.service.dto.UserDTO
import org.mapstruct.*

/**
 * Mapper for the entity [TabletUserWatchList] and its DTO [TabletUserWatchListDTO].
 */
@Mapper(componentModel = "spring")
interface TabletUserWatchListMapper :
    EntityMapper<TabletUserWatchListDTO, TabletUserWatchList> {

    @Mappings(
        Mapping(target = "tabletUser", source = "tabletUser", qualifiedByName = ["tabletUserId"]),
        Mapping(target = "user", source = "user", qualifiedByName = ["userId"])
    )
    override fun toDto(s: TabletUserWatchList): TabletUserWatchListDTO

    @Named("tabletUserId")
    @BeanMapping(ignoreByDefault = true)

    @Mappings(
        Mapping(target = "id", source = "id")
    )
    fun toDtoTabletUserId(tabletUser: TabletUser): TabletUserDTO

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)

    @Mappings(
        Mapping(target = "id", source = "id")
    )
    fun toDtoUserId(user: User): UserDTO
}
