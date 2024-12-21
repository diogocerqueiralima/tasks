package com.github.diogodelima.authorizationserver.exception.handler

import com.github.diogodelima.authorizationserver.exception.UserRegisterException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(UserRegisterException::class)
    fun handleRegisterError(e: UserRegisterException) =
        "redirect:/auth/register?error=${e.error}"

}