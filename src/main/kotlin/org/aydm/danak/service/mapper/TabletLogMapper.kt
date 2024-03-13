package org.aydm.danak.service.mapper

import org.aydm.danak.domain.Tablet
import org.aydm.danak.domain.TabletLog
import org.aydm.danak.service.dto.TabletDTO
import org.aydm.danak.service.dto.TabletLogDTO
import org.mapstruct.*

/**
 * Mapper for the entity [TabletLog] and its DTO [TabletLogDTO].
 */
@Mapper(componentModel = "spring")
interface TabletLogMapper :
    EntityMapper<TabletLogDTO, TabletLog> {

    @Mappings(
        Mapping(target = "tablet", source = "tablet", qualifiedByName = ["tabletId"])
    )
    override fun toDto(s: TabletLog): TabletLogDTO

    @Named("tabletId")
    @BeanMapping(ignoreByDefault = true)

    @Mappings(
        Mapping(target = "id", source = "id")
    )
    fun toDtoTabletId(tablet: Tablet): TabletDTO
}
