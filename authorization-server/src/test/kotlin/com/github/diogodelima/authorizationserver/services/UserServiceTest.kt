package com.github.diogodelima.authorizationserver.services

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
import org.springframework.security.crypto.password.PasswordEncoder
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

}