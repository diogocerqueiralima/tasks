package com.github.diogodelima.authorizationserver.config

import com.github.diogodelima.authorizationserver.domain.User
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer

@Configuration
class JwtTokenCustomizerConfig {

    @Bean
    fun tokenCustomizer(): OAuth2TokenCustomizer<JwtEncodingContext> = OAuth2TokenCustomizer<JwtEncodingContext> { context ->

        if (OAuth2TokenType.ACCESS_TOKEN == context.tokenType) {

            val user = context.getPrincipal<Authentication>().principal as User

            context.claims.claims { claims ->
                claims["user_id"] = user.id
            }

        }

    }

}