package com.github.diogodelima.authorizationserver.repositories

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.security.oauth2.core.oidc.OidcScopes
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings
import java.util.*

@Configuration
class ClientRepository(

    @Value("\${client.postman.id}")
    private val postmanClientId: String,

    @Value("\${client.postman.redirectUri}")
    private val postmanRedirectUri: String,

    @Value("\${client.react.id}")
    private val reactClientId: String,

    @Value("\${client.react.redirectUri}")
    private val reactRedirectUri: String

) {

    @Bean
    fun registeredClientRepository(): RegisteredClientRepository {

        val postmanClient = RegisteredClient.withId(UUID.randomUUID().toString())
            .clientId(postmanClientId)
            .clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
            .redirectUri(postmanRedirectUri)
            .scope(OidcScopes.OPENID)
            .clientSettings(
                ClientSettings.builder()
                    .requireAuthorizationConsent(false)
                    .requireProofKey(true)
                    .build()
            )
            .build()

        val reactClient = RegisteredClient.withId(UUID.randomUUID().toString())
            .clientId(reactClientId)
            .clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
            .redirectUri(reactRedirectUri)
            .scope(OidcScopes.OPENID)
            .clientSettings(
                ClientSettings.builder()
                    .requireAuthorizationConsent(false)
                    .requireProofKey(true)
                    .build()
            )
            .build()

        return InMemoryRegisteredClientRepository(postmanClient, reactClient)
    }

}