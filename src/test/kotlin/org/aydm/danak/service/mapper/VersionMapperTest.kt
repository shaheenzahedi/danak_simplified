package org.aydm.danak.service.mapper

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat

class VersionMapperTest {

    private lateinit var versionMapper: VersionMapper

    @BeforeEach
    fun setUp() {
        versionMapper = VersionMapperImpl()
    }
}