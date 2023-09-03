package org.aydm.danak.domain

import org.assertj.core.api.Assertions.assertThat
import org.aydm.danak.web.rest.equalsVerifier
import org.junit.jupiter.api.Test

class UserActivityTest {

    @Test
    fun equalsVerifier() {
        equalsVerifier(UserActivity::class)
        val userActivity1 = UserActivity()
        userActivity1.id = 1L
        val userActivity2 = UserActivity()
        userActivity2.id = userActivity1.id
        assertThat(userActivity1).isEqualTo(userActivity2)
        userActivity2.id = 2L
        assertThat(userActivity1).isNotEqualTo(userActivity2)
        userActivity1.id = null
        assertThat(userActivity1).isNotEqualTo(userActivity2)
    }
}
