package com.github.diogodelima.authorizationserver.dto

data class UserForgotPasswordDto(

    val username: String = "",

    val email: String = "",

    val url: String = "",

    val subject: String = "Tasks - Reset Password",

    val template: String = "RESET_PASSWORD_TEMPLATE"

)
