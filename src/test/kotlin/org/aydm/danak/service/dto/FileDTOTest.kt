package org.aydm.danak.service.dto

import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat
import org.aydm.danak.web.rest.equalsVerifier

import java.util.UUID

class FileDTOTest {

    @Test
    fun dtoEqualsVerifier(){
        equalsVerifier(FileDTO::class)
        val fileDTO1 = FileDTO()
        fileDTO1.id = 1L
        val fileDTO2 = FileDTO()
        assertThat(fileDTO1).isNotEqualTo(fileDTO2)
        fileDTO2.id = fileDTO1.id
        assertThat(fileDTO1).isEqualTo(fileDTO2)
        fileDTO2.id = 2L
        assertThat(fileDTO1).isNotEqualTo(fileDTO2)
        fileDTO1.id = null
        assertThat(fileDTO1).isNotEqualTo(fileDTO2)
    }
}
