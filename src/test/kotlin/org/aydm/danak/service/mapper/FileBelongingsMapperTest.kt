package org.aydm.danak.service.mapper

import org.junit.jupiter.api.BeforeEach

class FileBelongingsMapperTest {

    private lateinit var fileBelongingsMapper: FileBelongingsMapper

    @BeforeEach
    fun setUp() {
        fileBelongingsMapper = FileBelongingsMapperImpl()
    }
}
