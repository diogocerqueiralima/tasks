package com.github.diogodelima.gateway.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler
import org.springframework.web.cors.CorsConfiguration
import reactor.core.publisher.Mono

@Configuration
@EnableWebFluxSecurity
class SecurityConfig {

    @Bean
    fun defaultSecurityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain =
        http
            .cors { cors ->
                cors.configurationSource {
                    val corsConfiguration = CorsConfiguration()
                    corsConfiguration.allowedOrigins = listOf("http://localhost:3000")
                    corsConfiguration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE")
                    corsConfiguration.allowedHeaders = listOf("*")
                    corsConfiguration.exposedHeaders = listOf("*")
                    corsConfiguration.allowCredentials = true
                    corsConfiguration
                }
            }
            .csrf { it.disable() }
            .authorizeExchange {
                it.anyExchange().authenticated()
            }
            .oauth2Login { oauth2 ->
                oauth2.authenticationSuccessHandler(authenticationSuccessHandler())
            }
            .build()

    @Bean
    fun authenticationSuccessHandler(): ServerAuthenticationSuccessHandler =
        ServerAuthenticationSuccessHandler { webFilterExchange, _ ->

            val redirectUri = "http://localhost:3000"

            webFilterExchange.exchange.response.statusCode = org.springframework.http.HttpStatus.FOUND
            webFilterExchange.exchange.response.headers.location = java.net.URI.create(redirectUri)

            Mono.empty()
        }

}