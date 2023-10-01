package org.aydm.danak.service.dto

import org.aydm.danak.domain.User

/**
 * A DTO representing a user, with only the public attributes.
 */
open class UserDTO(
    var id: Long? = null,
    var login: String? = null,
    val email: String? = null,
    val password: String? = null,
    var firstName: String? = null,
    var lastName: String? = null
) {



    constructor(user: User) : this(user.id, user.login)

    override fun toString() = "UserDTO{" +
        "login='" + login + '\'' +
        "}"
}
