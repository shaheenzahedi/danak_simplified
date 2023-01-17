package org.aydm.danak.service.mapper

import org.aydm.danak.domain.Tablet
import org.aydm.danak.service.dto.TabletDTO
import org.mapstruct.*

/**
 * Mapper for the entity [Tablet] and its DTO [TabletDTO].
 */
@Mapper(componentModel = "spring")
interface TabletMapper :
    EntityMapper<TabletDTO, Tablet>
