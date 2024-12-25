package com.github.diogodelima.authorizationserver.exception

open class UserRegisterException(

    val error: UserRegisterError,
    override val message: String

) : RuntimeException(message)

class PasswordNotMatchException(

    error: UserRegisterError = UserRegisterError.PASSWORD_NOT_MATCH,
    override val message: String = "Passwords not match"

) : UserRegisterException(error, message)

class UserAlreadyExistsException(

    error: UserRegisterError = UserRegisterError.USER_ALREADY_EXISTS,
    override val message: String = "User already exists"

) : UserRegisterException(error, message)

enum class UserRegisterError {

    PASSWORD_NOT_MATCH,
    USER_ALREADY_EXISTS

}