package org.aydm.danak.service.mapper

import org.aydm.danak.domain.Center
import org.aydm.danak.domain.CenterDonor
import org.aydm.danak.domain.Donor
import org.aydm.danak.service.dto.CenterDTO
import org.aydm.danak.service.dto.CenterDonorDTO
import org.aydm.danak.service.dto.DonorDTO
import org.mapstruct.*

/**
 * Mapper for the entity [CenterDonor] and its DTO [CenterDonorDTO].
 */
@Mapper(componentModel = "spring")
interface CenterDonorMapper :
    EntityMapper<CenterDonorDTO, CenterDonor> {

    @Mappings(
        Mapping(
            target = "center",
            source = "center",
            qualifiedByName = ["centerId"]
        ),
        Mapping(
            target = "donor",
            source = "donor",
            qualifiedByName = ["donorId"]
        )
    )
    override fun toDto(s: CenterDonor): CenterDonorDTO

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
}
