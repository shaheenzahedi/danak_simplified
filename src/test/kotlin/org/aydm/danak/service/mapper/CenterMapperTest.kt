package org.aydm.danak.service.mapper

import org.junit.jupiter.api.BeforeEach

class CenterMapperTest {

    private lateinit var centerMapper: CenterMapper

    @BeforeEach
    fun setUp() {
        centerMapper = CenterMapperImpl()
    }
}
