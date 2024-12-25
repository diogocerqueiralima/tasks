package com.github.diogodelima.authorizationserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class AuthorizationServerApplication

fun main(args: Array<String>) {
	runApplication<AuthorizationServerApplication>(*args)
}
