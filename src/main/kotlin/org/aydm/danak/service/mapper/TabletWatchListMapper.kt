package org.aydm.danak.service.mapper

import org.aydm.danak.domain.Tablet
import org.aydm.danak.domain.TabletWatchList
import org.aydm.danak.domain.User
import org.aydm.danak.service.dto.TabletDTO
import org.aydm.danak.service.dto.TabletWatchListDTO
import org.aydm.danak.service.dto.UserDTO
import org.mapstruct.*

/**
 * Mapper for the entity [TabletWatchList] and its DTO [TabletWatchListDTO].
 */
@Mapper(componentModel = "spring")
interface TabletWatchListMapper :
    EntityMapper<TabletWatchListDTO, TabletWatchList> {

    @Mappings(
        Mapping(target = "tablet", source = "tablet", qualifiedByName = ["tabletId"]),
        Mapping(target = "user", source = "user", qualifiedByName = ["userId"])
    )
    override fun toDto(s: TabletWatchList): TabletWatchListDTO

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
