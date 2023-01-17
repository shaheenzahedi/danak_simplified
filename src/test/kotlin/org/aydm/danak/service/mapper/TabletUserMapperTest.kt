package org.aydm.danak.service.mapper

import org.junit.jupiter.api.BeforeEach

class TabletUserMapperTest {

    private lateinit var tabletUserMapper: TabletUserMapper

    @BeforeEach
    fun setUp() {
        tabletUserMapper = TabletUserMapperImpl()
    }
}
