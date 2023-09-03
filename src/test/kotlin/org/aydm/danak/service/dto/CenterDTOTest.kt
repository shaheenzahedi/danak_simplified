package org.aydm.danak.service.dto

import org.assertj.core.api.Assertions.assertThat
import org.aydm.danak.web.rest.equalsVerifier
import org.junit.jupiter.api.Test

class CenterDTOTest {

    @Test
    fun dtoEqualsVerifier() {
        equalsVerifier(CenterDTO::class)
        val centerDTO1 = CenterDTO()
        centerDTO1.id = 1L
        val centerDTO2 = CenterDTO()
        assertThat(centerDTO1).isNotEqualTo(centerDTO2)
        centerDTO2.id = centerDTO1.id
        assertThat(centerDTO1).isEqualTo(centerDTO2)
        centerDTO2.id = 2L
        assertThat(centerDTO1).isNotEqualTo(centerDTO2)
        centerDTO1.id = null
        assertThat(centerDTO1).isNotEqualTo(centerDTO2)
    }
}
