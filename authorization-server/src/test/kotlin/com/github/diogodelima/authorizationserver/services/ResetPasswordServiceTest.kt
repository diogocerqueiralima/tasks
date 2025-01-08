package com.github.diogodelima.authorizationserver.services

import com.github.diogodelima.authorizationserver.domain.ResetPassword
import com.github.diogodelima.authorizationserver.domain.User
import com.github.diogodelima.authorizationserver.repositories.ResetPasswordRepository
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.springframework.boot.test.context.SpringBootTest
import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertEquals

@SpringBootTest
class ResetPasswordServiceTest {

    @Mock
    private lateinit var resetPasswordRepository: ResetPasswordRepository

    @InjectMocks
    private lateinit var resetPasswordService: ResetPasswordService

    @Test
    fun `should create reset password if none exists for the user`() {

        val user = User(email = "test@test.com", password = "test123", username = "test123")
        val expected = ResetPassword(user = user)

        Mockito.`when`(resetPasswordRepository.save(Mockito.any(ResetPassword::class.java)))
            .thenReturn(expected)

        Mockito.`when`(resetPasswordRepository.findResetPasswordByUser(user))
            .thenReturn(null)

        val actual = resetPasswordService.getResetPasswordByUser(user)

        assertEquals(expected, actual)
    }

    @Test
    fun `should create a new reset password if the existing one is expired`() {

        val user = User(email = "test@test.com", password = "test123", username = "test123")
        val expected = ResetPassword(user = user)

        Mockito.`when`(resetPasswordRepository.findResetPasswordByUser(user))
            .thenReturn(ResetPassword(user = user, createdAt = Instant.now().minusSeconds(60 * 30).toEpochMilli()))

        Mockito.`when`(resetPasswordRepository.save(Mockito.any(ResetPassword::class.java)))
            .thenReturn(expected)

        val actual = resetPasswordService.getResetPasswordByUser(user)

        assertEquals(expected, actual)
    }

}