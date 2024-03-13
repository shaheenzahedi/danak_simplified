package org.aydm.danak.service.mapper

import org.aydm.danak.domain.PanelLog
import org.aydm.danak.domain.User
import org.aydm.danak.service.dto.PanelLogDTO
import org.aydm.danak.service.dto.UserDTO
import org.mapstruct.*

/**
 * Mapper for the entity [PanelLog] and its DTO [PanelLogDTO].
 */
@Mapper(componentModel = "spring")
interface PanelLogMapper :
    EntityMapper<PanelLogDTO, PanelLog> {

    @Mappings(
        Mapping(target = "user", source = "user", qualifiedByName = ["userId"])
    )
    override fun toDto(s: PanelLog): PanelLogDTO

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)

    @Mappings(
        Mapping(target = "id", source = "id")
    )
    fun toDtoUserId(user: User): UserDTO
}
