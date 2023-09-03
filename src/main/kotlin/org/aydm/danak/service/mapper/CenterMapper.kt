package org.aydm.danak.service.mapper

import org.aydm.danak.domain.Center
import org.aydm.danak.service.dto.CenterDTO
import org.mapstruct.*

/**
 * Mapper for the entity [Center] and its DTO [CenterDTO].
 */
@Mapper(componentModel = "spring")
interface CenterMapper :
    EntityMapper<CenterDTO, Center>
