package com.github.diogodelima.authorizationserver.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.MediaType
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.core.oidc.OidcUserInfo
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationContext
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationToken
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher


@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    @Order(1)
    fun authorizationServerSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {

        val userInfoMapper: (OidcUserInfoAuthenticationContext) -> OidcUserInfo = { context: OidcUserInfoAuthenticationContext ->

            val authentication = context.getAuthentication<OidcUserInfoAuthenticationToken>()
            val principal = authentication.principal as JwtAuthenticationToken

            OidcUserInfo(principal.token.claims )
        }

        val authorizationServerConfigurer = OAuth2AuthorizationServerConfigurer.authorizationServer()

        http
            .securityMatcher(authorizationServerConfigurer.endpointsMatcher)
            .with(authorizationServerConfigurer) { authorizationServer ->
                authorizationServer
                    .oidc { oidc ->
                        oidc.userInfoEndpoint { userInfo ->
                            userInfo.userInfoMapper(userInfoMapper)
                        }
                    }
            }
            .authorizeHttpRequests { request ->
                request.anyRequest().authenticated()
            }
            .exceptionHandling { exceptions ->
                exceptions
                    .defaultAuthenticationEntryPointFor(
                        LoginUrlAuthenticationEntryPoint("/auth/login"),
                        MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                    )
            }

        return http.build()
    }

    @Bean
    @Order(2)
    fun defaultSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {

        return http
            .authorizeHttpRequests { authorize ->
                authorize
                    .requestMatchers("/css/**", "/img/**", "/auth/**").permitAll()
                    .anyRequest().authenticated()
            }
            .formLogin { login ->
                login
                    .loginPage("/auth/login")
                    .permitAll()
            }
            .build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

}