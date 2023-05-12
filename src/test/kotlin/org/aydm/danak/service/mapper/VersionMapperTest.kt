package org.aydm.danak.service.mapper

import org.junit.jupiter.api.BeforeEach

class VersionMapperTest {

    private lateinit var versionMapper: VersionMapper

    @BeforeEach
    fun setUp() {
        versionMapper = VersionMapperImpl()
    }
}
