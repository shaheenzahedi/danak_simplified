package org.aydm.danak.service.mapper


import org.aydm.danak.domain.Version
import org.aydm.danak.service.dto.VersionDTO

    

import org.mapstruct.*

/**
 * Mapper for the entity [Version] and its DTO [VersionDTO].
 */
@Mapper(componentModel = "spring")
interface VersionMapper :
    EntityMapper<VersionDTO, Version> {



      

}