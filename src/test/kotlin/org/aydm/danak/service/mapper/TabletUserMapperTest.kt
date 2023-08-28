package org.aydm.danak.service.mapper

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat

class TabletUserMapperTest {

    private lateinit var tabletUserMapper: TabletUserMapper

    @BeforeEach
    fun setUp() {
        tabletUserMapper = TabletUserMapperImpl()
    }
}
