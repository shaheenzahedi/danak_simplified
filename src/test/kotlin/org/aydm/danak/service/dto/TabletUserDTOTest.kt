package org.aydm.danak.service.dto

import org.assertj.core.api.Assertions.assertThat
import org.aydm.danak.web.rest.equalsVerifier
import org.junit.jupiter.api.Test

class TabletUserDTOTest {

    @Test
    fun dtoEqualsVerifier() {
        equalsVerifier(TabletUserDTO::class)
        val tabletUserDTO1 = TabletUserDTO()
        tabletUserDTO1.id = 1L
        val tabletUserDTO2 = TabletUserDTO()
        assertThat(tabletUserDTO1).isNotEqualTo(tabletUserDTO2)
        tabletUserDTO2.id = tabletUserDTO1.id
        assertThat(tabletUserDTO1).isEqualTo(tabletUserDTO2)
        tabletUserDTO2.id = 2L
        assertThat(tabletUserDTO1).isNotEqualTo(tabletUserDTO2)
        tabletUserDTO1.id = null
        assertThat(tabletUserDTO1).isNotEqualTo(tabletUserDTO2)
    }
}
