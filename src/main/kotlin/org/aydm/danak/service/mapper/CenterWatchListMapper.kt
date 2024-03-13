package org.aydm.danak.service.mapper

import org.aydm.danak.domain.Center
import org.aydm.danak.domain.CenterWatchList
import org.aydm.danak.domain.User
import org.aydm.danak.service.dto.CenterDTO
import org.aydm.danak.service.dto.CenterWatchListDTO
import org.aydm.danak.service.dto.UserDTO
import org.mapstruct.*

/**
 * Mapper for the entity [CenterWatchList] and its DTO [CenterWatchListDTO].
 */
@Mapper(componentModel = "spring")
interface CenterWatchListMapper :
    EntityMapper<CenterWatchListDTO, CenterWatchList> {

    @Mappings(
        Mapping(target = "center", source = "center", qualifiedByName = ["centerId"]),
        Mapping(target = "user", source = "user", qualifiedByName = ["userId"])
    )
    override fun toDto(s: CenterWatchList): CenterWatchListDTO

    @Named("centerId")
    @BeanMapping(ignoreByDefault = true)

    @Mappings(
        Mapping(target = "id", source = "id")
    )
    fun toDtoCenterId(center: Center): CenterDTO

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)

    @Mappings(
        Mapping(target = "id", source = "id")
    )
    fun toDtoUserId(user: User): UserDTO
}
