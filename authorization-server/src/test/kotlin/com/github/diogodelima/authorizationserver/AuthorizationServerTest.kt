package com.github.diogodelima.authorizationserver

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.*

@SpringBootTest
@AutoConfigureMockMvc
class AuthorizationServerTest(

	@Value("\${client.id}")
	private val clientId: String,

	@Value("\${client.redirect.uri}")
	private val redirectUri: String

) {

	@Autowired
	private lateinit var mockMvc: MockMvc

	@Test
	fun `context loads`() {}

	fun generateCodeVerifier(): String {
		val random = SecureRandom()
		val verifierBytes = ByteArray(32)
		random.nextBytes(verifierBytes)
		return Base64.getUrlEncoder().withoutPadding().encodeToString(verifierBytes)
	}

	fun generateCodeChallenge(codeVerifier: String): String {
		val messageDigest = MessageDigest.getInstance("SHA-256")
		val hashBytes = messageDigest.digest(codeVerifier.toByteArray(StandardCharsets.UTF_8))
		return Base64.getUrlEncoder().withoutPadding().encodeToString(hashBytes)
	}

	@Test
	fun `requesting the authorization endpoint without being authenticated should redirect to the login page`() {

		val codeVerifier = generateCodeVerifier()
		val codeChallenge = generateCodeChallenge(codeVerifier)

		mockMvc.perform(
			get("/oauth2/authorize")
				.queryParam("response_type", "code")
				.queryParam("client_id", clientId)
				.queryParam("redirect_uri", redirectUri)
				.queryParam("scope", "openid")
				.queryParam("code_challenge", codeChallenge)
				.queryParam("code_challenge_method", "S256")
		)
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("http://localhost/auth/login"))

	}

	@Test
	@WithMockUser(username = "test")
	fun `make request to authorization endpoint when logged in should redirect to redirect uri`() {

		val codeVerifier = generateCodeVerifier()
		val codeChallenge = generateCodeChallenge(codeVerifier)

		mockMvc.perform(
			get("/oauth2/authorize")
				.queryParam("response_type", "code")
				.queryParam("client_id", clientId)
				.queryParam("redirect_uri", redirectUri)
				.queryParam("scope", "openid")
				.queryParam("code_challenge", codeChallenge)
				.queryParam("code_challenge_method", "S256")
		)
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrlPattern("${redirectUri}?code=*"))

	}

}
