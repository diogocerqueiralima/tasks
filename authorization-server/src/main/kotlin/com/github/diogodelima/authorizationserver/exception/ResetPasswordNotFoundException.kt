package com.github.alpha.authorizationserver.exception

class ResetPasswordNotFoundException(

    override val message: String? = "Reset password not found"

) : RuntimeException(message)