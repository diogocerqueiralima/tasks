package com.github.diogodelima.authorizationserver.dto

import java.util.UUID

data class UserResetPasswordDto(

    val token: UUID,
    val password: String = ""

)