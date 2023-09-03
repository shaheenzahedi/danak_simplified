package org.aydm.danak.domain

import org.assertj.core.api.Assertions.assertThat
import org.aydm.danak.web.rest.equalsVerifier
import org.junit.jupiter.api.Test

class TabletTest {

    @Test
    fun equalsVerifier() {
        equalsVerifier(Tablet::class)
        val tablet1 = Tablet()
        tablet1.id = 1L
        val tablet2 = Tablet()
        tablet2.id = tablet1.id
        assertThat(tablet1).isEqualTo(tablet2)
        tablet2.id = 2L
        assertThat(tablet1).isNotEqualTo(tablet2)
        tablet1.id = null
        assertThat(tablet1).isNotEqualTo(tablet2)
    }
}
