package org.aydm.danak.service.mapper

import org.junit.jupiter.api.BeforeEach

class TabletMapperTest {

    private lateinit var tabletMapper: TabletMapper

    @BeforeEach
    fun setUp() {
        tabletMapper = TabletMapperImpl()
    }
}
