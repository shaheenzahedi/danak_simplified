package org.aydm.danak.service.mapper

import org.aydm.danak.domain.TabletUser
import org.aydm.danak.domain.UserActivity
import org.aydm.danak.service.dto.TabletUserDTO
import org.aydm.danak.service.dto.UserActivityDTO
import org.mapstruct.*

/**
 * Mapper for the entity [UserActivity] and its DTO [UserActivityDTO].
 */
@Mapper(componentModel = "spring")
interface UserActivityMapper :
    EntityMapper<UserActivityDTO, UserActivity> {

    @Mappings(
        Mapping(target = "activity", source = "activity", qualifiedByName = ["tabletUserId"])
    )
    override fun toDto(s: UserActivity): UserActivityDTO

    @Named("tabletUserId")
    @BeanMapping(ignoreByDefault = true)

    @Mappings(
        Mapping(target = "id", source = "id")
    )
    fun toDtoTabletUserId(tabletUser: TabletUser): TabletUserDTO
}
