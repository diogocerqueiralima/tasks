package com.github.diogodelima.authorizationserver.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/auth")
class AuthenticationController {

    @GetMapping("/login")
    fun login() = "login"

}