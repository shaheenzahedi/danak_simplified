package org.aydm.danak.service.dto

import org.assertj.core.api.Assertions.assertThat
import org.aydm.danak.web.rest.equalsVerifier
import org.junit.jupiter.api.Test

class DonorDTOTest {

    @Test
    fun dtoEqualsVerifier() {
        equalsVerifier(DonorDTO::class)
        val donorDTO1 = DonorDTO()
        donorDTO1.id = 1L
        val donorDTO2 = DonorDTO()
        assertThat(donorDTO1).isNotEqualTo(donorDTO2)
        donorDTO2.id = donorDTO1.id
        assertThat(donorDTO1).isEqualTo(donorDTO2)
        donorDTO2.id = 2L
        assertThat(donorDTO1).isNotEqualTo(donorDTO2)
        donorDTO1.id = null
        assertThat(donorDTO1).isNotEqualTo(donorDTO2)
    }
}
