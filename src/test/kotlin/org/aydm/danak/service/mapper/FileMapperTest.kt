package org.aydm.danak.service.mapper

import org.junit.jupiter.api.BeforeEach

class FileMapperTest {

    private lateinit var fileMapper: FileMapper

    @BeforeEach
    fun setUp() {
        fileMapper = FileMapperImpl()
    }
}
