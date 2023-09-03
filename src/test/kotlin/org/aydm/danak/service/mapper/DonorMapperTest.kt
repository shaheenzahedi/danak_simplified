package org.aydm.danak.service.mapper

import org.junit.jupiter.api.BeforeEach

class DonorMapperTest {

    private lateinit var donorMapper: DonorMapper

    @BeforeEach
    fun setUp() {
        donorMapper = DonorMapperImpl()
    }
}
