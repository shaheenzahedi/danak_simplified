package org.aydm.danak.service.mapper

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat

class FileMapperTest {

    private lateinit var fileMapper: FileMapper

    @BeforeEach
    fun setUp() {
        fileMapper = FileMapperImpl()
    }
}
