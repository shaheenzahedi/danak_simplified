package org.aydm.danak.domain

import org.assertj.core.api.Assertions.assertThat
import org.aydm.danak.web.rest.equalsVerifier
import org.junit.jupiter.api.Test

class DonorTest {

    @Test
    fun equalsVerifier() {
        equalsVerifier(Donor::class)
        val donor1 = Donor()
        donor1.id = 1L
        val donor2 = Donor()
        donor2.id = donor1.id
        assertThat(donor1).isEqualTo(donor2)
        donor2.id = 2L
        assertThat(donor1).isNotEqualTo(donor2)
        donor1.id = null
        assertThat(donor1).isNotEqualTo(donor2)
    }
}
