package org.aydm.danak.service.dto

import org.assertj.core.api.Assertions.assertThat
import org.aydm.danak.web.rest.equalsVerifier
import org.junit.jupiter.api.Test

class VersionDTOTest {

    @Test
    fun dtoEqualsVerifier() {
        equalsVerifier(VersionDTO::class)
        val versionDTO1 = VersionDTO()
        versionDTO1.id = 1L
        val versionDTO2 = VersionDTO()
        assertThat(versionDTO1).isNotEqualTo(versionDTO2)
        versionDTO2.id = versionDTO1.id
        assertThat(versionDTO1).isEqualTo(versionDTO2)
        versionDTO2.id = 2L
        assertThat(versionDTO1).isNotEqualTo(versionDTO2)
        versionDTO1.id = null
        assertThat(versionDTO1).isNotEqualTo(versionDTO2)
    }
}
