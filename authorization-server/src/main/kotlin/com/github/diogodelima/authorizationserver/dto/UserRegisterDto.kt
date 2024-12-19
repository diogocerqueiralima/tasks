package com.github.diogodelima.authorizationserver.dto

data class UserRegisterDto(

    val username: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = ""

)