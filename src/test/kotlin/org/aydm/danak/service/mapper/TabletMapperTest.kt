package org.aydm.danak.service.mapper

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat

class TabletMapperTest {

    private lateinit var tabletMapper: TabletMapper

    @BeforeEach
    fun setUp() {
        tabletMapper = TabletMapperImpl()
    }
}
