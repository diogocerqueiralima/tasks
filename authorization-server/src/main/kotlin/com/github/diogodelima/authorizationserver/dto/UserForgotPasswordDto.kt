package com.github.diogodelima.authorizationserver.dto

data class UserForgotPasswordDto(

    val usernameOrEmail: String = "",
    val template: String = "RESET_PASSWORD_TEMPLATE"

)
