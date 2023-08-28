package org.aydm.danak.service.dto

import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat
import org.aydm.danak.web.rest.equalsVerifier

import java.util.UUID

class UserActivityDTOTest {

    @Test
    fun dtoEqualsVerifier(){
        equalsVerifier(UserActivityDTO::class)
        val userActivityDTO1 = UserActivityDTO()
        userActivityDTO1.id = 1L
        val userActivityDTO2 = UserActivityDTO()
        assertThat(userActivityDTO1).isNotEqualTo(userActivityDTO2)
        userActivityDTO2.id = userActivityDTO1.id
        assertThat(userActivityDTO1).isEqualTo(userActivityDTO2)
        userActivityDTO2.id = 2L
        assertThat(userActivityDTO1).isNotEqualTo(userActivityDTO2)
        userActivityDTO1.id = null
        assertThat(userActivityDTO1).isNotEqualTo(userActivityDTO2)
    }
}
