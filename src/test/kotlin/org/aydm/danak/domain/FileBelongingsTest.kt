package org.aydm.danak.domain

import org.assertj.core.api.Assertions.assertThat
import org.aydm.danak.web.rest.equalsVerifier
import org.junit.jupiter.api.Test

class FileBelongingsTest {

    @Test
    fun equalsVerifier() {
        equalsVerifier(FileBelongings::class)
        val fileBelongings1 = FileBelongings()
        fileBelongings1.id = 1L
        val fileBelongings2 = FileBelongings()
        fileBelongings2.id = fileBelongings1.id
        assertThat(fileBelongings1).isEqualTo(fileBelongings2)
        fileBelongings2.id = 2L
        assertThat(fileBelongings1).isNotEqualTo(fileBelongings2)
        fileBelongings1.id = null
        assertThat(fileBelongings1).isNotEqualTo(fileBelongings2)
    }
}
