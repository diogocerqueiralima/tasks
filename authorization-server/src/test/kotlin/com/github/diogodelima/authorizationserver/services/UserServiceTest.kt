package com.github.diogodelima.authorizationserver.services

import com.github.alpha.authorizationserver.exception.ResetPasswordNotFoundException
import com.github.diogodelima.authorizationserver.domain.ResetPassword
import com.github.diogodelima.authorizationserver.domain.User
import com.github.diogodelima.authorizationserver.dto.UserForgotPasswordDto
import com.github.diogodelima.authorizationserver.exception.PasswordNotMatchException
import com.github.diogodelima.authorizationserver.exception.UserAlreadyExistsException
import com.github.diogodelima.authorizationserver.repositories.UserRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.UUID
import kotlin.test.assertEquals

@SpringBootTest
class UserServiceTest {

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var passwordEncoder: PasswordEncoder

    @Mock
    private lateinit var kafkaTemplate: KafkaTemplate<String, UserForgotPasswordDto>

    @Mock
    private lateinit var resetPasswordService: ResetPasswordService

    @InjectMocks
    private lateinit var userService: UserService

    @Test
    fun `create user with different passwords should fail`() {

        assertThrows<PasswordNotMatchException> {
            userService.create("username", "username@email.com", "test", "test123")
        }

    }

    @Test
    fun `create user with an existing username should fail`() {

        Mockito.`when`(userRepository.findUserByUsername("username"))
            .thenReturn(User(username = "username", email = "username@email.com", password = "test"))

        assertThrows<UserAlreadyExistsException> {
            userService.create("username", "user@email.com", "test", "test")
        }

    }

    @Test
    fun `create user with an existing email should fail`() {

        Mockito.`when`(userRepository.findUserByEmail("username@email.com"))
            .thenReturn(User(username = "username", email = "username@email.com", password = "test"))

        assertThrows<UserAlreadyExistsException> {
            userService.create("user", "username@email.com", "test", "test")
        }

    }

    @Test
    fun `create user should succeed`() {

        val password = "test"
        val encoded = "test_password_encoded"
        val expected = User(id = 1, email = "username@email.com", username = "username", password = encoded)

        Mockito.`when`(userRepository.save(Mockito.any(User::class.java)))
            .thenReturn(expected)

        Mockito.`when`(passwordEncoder.encode(Mockito.any(String::class.java)))
            .thenReturn(encoded)

        val actual = userService.create("username", "username@email.com", password, password)
        assertEquals(expected, actual)
    }

    @Test
    fun `the email request to reset the password with an username that does not exists should fail`() {

        Mockito.`when`(userRepository.findUserByEmail("username@email.com"))
            .thenReturn(User(username = "username", email = "username@email.com", password = "test"))

        assertThrows<UsernameNotFoundException> {
            userService.requestEmailToResetPassword(UserForgotPasswordDto(username = "username"))
        }

    }

    @Test
    fun `the email request to reset the password with an email that does not exists should fail`() {

        Mockito.`when`(userRepository.findUserByUsername("username"))
            .thenReturn(User(username = "username", email = "username@email.com", password = "test"))

        assertThrows<UsernameNotFoundException> {
            userService.requestEmailToResetPassword(UserForgotPasswordDto(email = "username@email.com"))
        }

    }

    @Test
    fun `the email request to reset the password should succeed`() {

        val user = User(username = "username", email = "username@email.com", password = "test")
        val resetPassword = ResetPassword(user = user)

        Mockito.`when`(userRepository.findUserByUsername("username"))
            .thenReturn(user)

        Mockito.`when`(resetPasswordService.getResetPasswordByUser(user))
            .thenReturn(resetPassword)

        userService.requestEmailToResetPassword(UserForgotPasswordDto(username = "username"))
    }

    @Test
    fun `reset password with invalid token should fail`() {

        val token = UUID.randomUUID()

        Mockito.`when`(resetPasswordService.getResetPasswordByToken(token))
            .thenThrow(ResetPasswordNotFoundException())

        assertThrows<ResetPasswordNotFoundException> {
            userService.resetPassword(token, "password")
        }

    }

    @Test
    fun `reset password should succeed`() {

        val passwordEncoded = "password_encoded"
        val expected = User(username = "username", email = "username@email.com", password = passwordEncoded)
        val token = UUID.randomUUID()
        val resetPassword = ResetPassword(user = expected.copy(password = "old_password"), token = token)

        Mockito.`when`(resetPasswordService.getResetPasswordByToken(token))
            .thenReturn(resetPassword)

        Mockito.`when`(passwordEncoder.encode("password"))
            .thenReturn(passwordEncoded)

        Mockito.`when`(userRepository.save(expected))
            .thenReturn(expected)

        val actual = userService.resetPassword(token, "password")
        assertEquals(expected, actual)
    }

}