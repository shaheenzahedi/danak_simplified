package org.aydm.danak.service.dto

import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat
import org.aydm.danak.web.rest.equalsVerifier

import java.util.UUID

class FileBelongingsDTOTest {

    @Test
    fun dtoEqualsVerifier(){
        equalsVerifier(FileBelongingsDTO::class)
        val fileBelongingsDTO1 = FileBelongingsDTO()
        fileBelongingsDTO1.id = 1L
        val fileBelongingsDTO2 = FileBelongingsDTO()
        assertThat(fileBelongingsDTO1).isNotEqualTo(fileBelongingsDTO2)
        fileBelongingsDTO2.id = fileBelongingsDTO1.id
        assertThat(fileBelongingsDTO1).isEqualTo(fileBelongingsDTO2)
        fileBelongingsDTO2.id = 2L
        assertThat(fileBelongingsDTO1).isNotEqualTo(fileBelongingsDTO2)
        fileBelongingsDTO1.id = null
        assertThat(fileBelongingsDTO1).isNotEqualTo(fileBelongingsDTO2)
    }
}
