package org.aydm.danak.service.mapper

import org.aydm.danak.domain.Center
import org.aydm.danak.domain.Donor
import org.aydm.danak.domain.Tablet
import org.aydm.danak.domain.User
import org.aydm.danak.service.dto.CenterDTO
import org.aydm.danak.service.dto.DonorDTO
import org.aydm.danak.service.dto.TabletDTO
import org.aydm.danak.service.dto.UserDTO
import org.mapstruct.*

/**
 * Mapper for the entity [Tablet] and its DTO [TabletDTO].
 */
@Mapper(componentModel = "spring")
interface TabletMapper :
    EntityMapper<TabletDTO, Tablet> {

    @Mappings(
        Mapping(target = "center", source = "center", qualifiedByName = ["centerId"]),
        Mapping(target = "donor", source = "donor", qualifiedByName = ["donorId"]),
        Mapping(target = "archivedBy", source = "archivedBy", qualifiedByName = ["userId"]),
        Mapping(target = "modifiedBy", source = "modifiedBy", qualifiedByName = ["userId"])
    )
    override fun toDto(s: Tablet): TabletDTO

    @Named("centerId")
    @BeanMapping(ignoreByDefault = true)

    @Mappings(
        Mapping(target = "id", source = "id")
    )
    fun toDtoCenterId(center: Center): CenterDTO

    @Named("donorId")
    @BeanMapping(ignoreByDefault = true)

    @Mappings(
        Mapping(target = "id", source = "id")
    )
    fun toDtoDonorId(donor: Donor): DonorDTO

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)

    @Mappings(
        Mapping(target = "id", source = "id")
    )
    fun toDtoUserId(user: User): UserDTO
}
