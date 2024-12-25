package com.github.diogodelima.authorizationserver.dto

data class UserResetPasswordDto(

    val token: String,
    val password: String = ""

)