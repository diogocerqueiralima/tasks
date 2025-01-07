package com.github.diogodelima.authorizationserver.exception.handler

import com.github.alpha.authorizationserver.exception.ResetPasswordNotFoundException
import com.github.diogodelima.authorizationserver.exception.UserRegisterException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(UserRegisterException::class)
    fun handleRegisterError(e: UserRegisterException) =
        "redirect:/auth/register?error=${e.error}"

    @ExceptionHandler(ResetPasswordNotFoundException::class)
    fun handleInvalidTokenToResetPassword(e: ResetPasswordNotFoundException) =
        "redirect:/auth/reset?error"

}