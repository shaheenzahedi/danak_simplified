package org.aydm.danak.domain

import org.assertj.core.api.Assertions.assertThat
import org.aydm.danak.web.rest.equalsVerifier
import org.junit.jupiter.api.Test

class VersionTest {

    @Test
    fun equalsVerifier() {
        equalsVerifier(Version::class)
        val version1 = Version()
        version1.id = 1L
        val version2 = Version()
        version2.id = version1.id
        assertThat(version1).isEqualTo(version2)
        version2.id = 2L
        assertThat(version1).isNotEqualTo(version2)
        version1.id = null
        assertThat(version1).isNotEqualTo(version2)
    }
}
