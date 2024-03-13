package org.aydm.danak.service.mapper

import org.aydm.danak.domain.Donor
import org.aydm.danak.domain.DonorWatchList
import org.aydm.danak.domain.User
import org.aydm.danak.service.dto.DonorDTO
import org.aydm.danak.service.dto.DonorWatchListDTO
import org.aydm.danak.service.dto.UserDTO
import org.mapstruct.*

/**
 * Mapper for the entity [DonorWatchList] and its DTO [DonorWatchListDTO].
 */
@Mapper(componentModel = "spring")
interface DonorWatchListMapper :
    EntityMapper<DonorWatchListDTO, DonorWatchList> {

    @Mappings(
        Mapping(target = "donor", source = "donor", qualifiedByName = ["donorId"]),
        Mapping(target = "user", source = "user", qualifiedByName = ["userId"])
    )
    override fun toDto(s: DonorWatchList): DonorWatchListDTO

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
