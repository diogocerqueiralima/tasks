package com.github.diogodelima.emailservice.dto

data class UserForgotPasswordDto(

    val username: String = "",

    val email: String = "",

    val subject: String = "",

    val url: String = "",

    val template: String = ""

)