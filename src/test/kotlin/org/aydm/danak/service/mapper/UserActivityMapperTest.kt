package org.aydm.danak.service.mapper

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat

class UserActivityMapperTest {

    private lateinit var userActivityMapper: UserActivityMapper

    @BeforeEach
    fun setUp() {
        userActivityMapper = UserActivityMapperImpl()
    }
}
