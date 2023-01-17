package org.aydm.danak.service.mapper

import org.aydm.danak.domain.Tablet
import org.aydm.danak.domain.TabletUser
import org.aydm.danak.service.dto.TabletDTO
import org.aydm.danak.service.dto.TabletUserDTO
import org.mapstruct.*

/**
 * Mapper for the entity [TabletUser] and its DTO [TabletUserDTO].
 */
@Mapper(componentModel = "spring")
interface TabletUserMapper :
    EntityMapper<TabletUserDTO, TabletUser> {

    @Mappings(
        Mapping(target = "tablet", source = "tablet", qualifiedByName = ["tabletId"])
    )
    override fun toDto(s: TabletUser): TabletUserDTO @Named("tabletId")
    @BeanMapping(ignoreByDefault = true)

    @Mappings(
        Mapping(target = "id", source = "id")
    )
    fun toDtoTabletId(tablet: Tablet): TabletDTO }
