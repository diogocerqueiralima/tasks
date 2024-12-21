package com.github.diogodelima.authorizationserver.exception

import com.github.diogodelima.authorizationserver.error.RegisterError

open class UserRegisterException(

    val error: RegisterError,
    override val message: String

) : RuntimeException(message)

class PasswordNotMatchException(

    error: RegisterError = RegisterError.PASSWORD_NOT_MATCH,
    override val message: String = "Passwords not match"

) : UserRegisterException(error, message)

class UserAlreadyExistsException(

    error: RegisterError = RegisterError.USER_ALREADY_EXISTS,
    override val message: String = "User already exists"

) : UserRegisterException(error, message)