package com.github.diogodelima.authorizationserver.controller

import com.github.diogodelima.authorizationserver.dto.UserForgotPasswordDto
import com.github.diogodelima.authorizationserver.dto.UserRegisterDto
import com.github.diogodelima.authorizationserver.dto.UserResetPasswordDto
import com.github.diogodelima.authorizationserver.services.UserService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
@RequestMapping("/auth")
class AuthenticationController(

    private val userService: UserService

) {

    @GetMapping("/login")
    fun login() = "login"

    @GetMapping("/register")
    fun register(model: Model): String {

        model.addAttribute("user", UserRegisterDto())

        return "register"
    }

    @PostMapping("/register")
    fun createAccount(@ModelAttribute dto: UserRegisterDto): String {

        userService.create(
            username = dto.username,
            email = dto.email,
            password = dto.password,
            confirmPassword = dto.confirmPassword
        )

        return "redirect:/auth/login"
    }

    @GetMapping("/forgot")
    fun forgotPassword(model: Model): String {

        model.addAttribute("user", UserForgotPasswordDto())

        return "forgot-password"
    }

    @PostMapping("/forgot")
    fun requestForgotEmail(@ModelAttribute dto: UserForgotPasswordDto): String {

        userService.requestEmailToResetPassword(dto)

        return "redirect:/auth/login"
    }

    @GetMapping("/reset")
    fun resetPassword(@RequestParam token: String, model: Model): String {

        model.addAttribute("user", UserResetPasswordDto(token = token))

        return "reset-password"
    }

    @PostMapping("/reset")
    fun requestResetPassword(@ModelAttribute dto: UserResetPasswordDto): String {

        userService.resetPassword(token = dto.token, password = dto.password)

        return "redirect:/auth/login"
    }

}