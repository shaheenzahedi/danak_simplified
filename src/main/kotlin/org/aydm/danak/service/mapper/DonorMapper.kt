package org.aydm.danak.service.mapper

import org.aydm.danak.domain.Donor
import org.aydm.danak.domain.User
import org.aydm.danak.service.dto.DonorDTO
import org.aydm.danak.service.dto.UserDTO
import org.mapstruct.*

/**
 * Mapper for the entity [Donor] and its DTO [DonorDTO].
 */
@Mapper(componentModel = "spring")
interface DonorMapper :
    EntityMapper<DonorDTO, Donor> {

    @Mappings(
        Mapping(target = "user", source = "user", qualifiedByName = ["userId"]),
        Mapping(target = "archivedBy", source = "archivedBy", qualifiedByName = ["userId"]),
        Mapping(target = "createdBy", source = "createdBy", qualifiedByName = ["userId"]),
        Mapping(target = "modifiedBy", source = "modifiedBy", qualifiedByName = ["userId"])
    )
    override fun toDto(s: Donor): DonorDTO

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)

    @Mappings(
        Mapping(target = "id", source = "id")
    )
    fun toDtoUserId(user: User): UserDTO
}
