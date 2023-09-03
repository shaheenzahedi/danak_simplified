package org.aydm.danak.service.dto

import org.assertj.core.api.Assertions.assertThat
import org.aydm.danak.web.rest.equalsVerifier
import org.junit.jupiter.api.Test

class TabletDTOTest {

    @Test
    fun dtoEqualsVerifier() {
        equalsVerifier(TabletDTO::class)
        val tabletDTO1 = TabletDTO()
        tabletDTO1.id = 1L
        val tabletDTO2 = TabletDTO()
        assertThat(tabletDTO1).isNotEqualTo(tabletDTO2)
        tabletDTO2.id = tabletDTO1.id
        assertThat(tabletDTO1).isEqualTo(tabletDTO2)
        tabletDTO2.id = 2L
        assertThat(tabletDTO1).isNotEqualTo(tabletDTO2)
        tabletDTO1.id = null
        assertThat(tabletDTO1).isNotEqualTo(tabletDTO2)
    }
}
