package org.aydm.danak.service.mapper

import org.aydm.danak.domain.Donor
import org.aydm.danak.domain.DonorImage
import org.aydm.danak.domain.UploadedFile
import org.aydm.danak.service.dto.DonorDTO
import org.aydm.danak.service.dto.DonorImageDTO
import org.aydm.danak.service.dto.UploadedFileDTO
import org.mapstruct.*

/**
 * Mapper for the entity [DonorImage] and its DTO [DonorImageDTO].
 */
@Mapper(componentModel = "spring")
interface DonorImageMapper :
    EntityMapper<DonorImageDTO, DonorImage> {

    @Mappings(
        Mapping(target = "file", source = "file", qualifiedByName = ["uploadedFileId"]),
        Mapping(target = "donor", source = "donor", qualifiedByName = ["donorId"])
    )
    override fun toDto(s: DonorImage): DonorImageDTO

    @Named("uploadedFileId")
    @BeanMapping(ignoreByDefault = true)

    @Mappings(
        Mapping(target = "id", source = "id")
    )
    fun toDtoUploadedFileId(uploadedFile: UploadedFile): UploadedFileDTO

    @Named("donorId")
    @BeanMapping(ignoreByDefault = true)

    @Mappings(
        Mapping(target = "id", source = "id")
    )
    fun toDtoDonorId(donor: Donor): DonorDTO
}
