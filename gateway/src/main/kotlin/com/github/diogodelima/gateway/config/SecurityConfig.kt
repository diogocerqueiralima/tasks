package com.github.diogodelima.gateway.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.cors.reactive.CorsWebFilter
import java.util.*

@Configuration
@EnableWebFluxSecurity
class SecurityConfig {

    @Bean
    fun defaultSecurityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain =
        http
            .cors {
                it.configurationSource {
                    val corsConfig = CorsConfiguration()
                    corsConfig.allowedOrigins = listOf("http://localhost:3000")
                    corsConfig.allowedMethods = listOf("*")
                    corsConfig.allowedHeaders = listOf("*")
                    corsConfig.allowCredentials = true
                    corsConfig
                }
            }
            .csrf { it.disable() }
            .oauth2ResourceServer { it.jwt(Customizer.withDefaults()) }
            .authorizeExchange { exchange ->
                exchange
                    .anyExchange().authenticated()
            }
            .build()

}