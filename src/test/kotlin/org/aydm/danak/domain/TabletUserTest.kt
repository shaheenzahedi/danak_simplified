package org.aydm.danak.domain

import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat
import org.aydm.danak.web.rest.equalsVerifier

import java.util.UUID

class TabletUserTest {

    @Test
    fun equalsVerifier() {
        equalsVerifier(TabletUser::class)
        val tabletUser1 = TabletUser()
        tabletUser1.id = 1L
        val tabletUser2 = TabletUser()
        tabletUser2.id = tabletUser1.id
        assertThat(tabletUser1).isEqualTo(tabletUser2)
        tabletUser2.id = 2L
        assertThat(tabletUser1).isNotEqualTo(tabletUser2)
        tabletUser1.id = null
        assertThat(tabletUser1).isNotEqualTo(tabletUser2)
    }
}
