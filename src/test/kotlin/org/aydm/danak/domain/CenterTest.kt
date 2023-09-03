package org.aydm.danak.domain

import org.assertj.core.api.Assertions.assertThat
import org.aydm.danak.web.rest.equalsVerifier
import org.junit.jupiter.api.Test

class CenterTest {

    @Test
    fun equalsVerifier() {
        equalsVerifier(Center::class)
        val center1 = Center()
        center1.id = 1L
        val center2 = Center()
        center2.id = center1.id
        assertThat(center1).isEqualTo(center2)
        center2.id = 2L
        assertThat(center1).isNotEqualTo(center2)
        center1.id = null
        assertThat(center1).isNotEqualTo(center2)
    }
}
