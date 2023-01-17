package org.aydm.danak.service.mapper

import org.junit.jupiter.api.BeforeEach

class UserActivityMapperTest {

    private lateinit var userActivityMapper: UserActivityMapper

    @BeforeEach
    fun setUp() {
        userActivityMapper = UserActivityMapperImpl()
    }
}
