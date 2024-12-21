package com.github.diogodelima.authorizationserver.controller

import com.github.diogodelima.authorizationserver.dto.UserRegisterDto
import com.github.diogodelima.authorizationserver.services.UserService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

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
    fun forgotPassword() = "forgot-password"

}