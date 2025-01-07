package com.github.diogodelima.authorizationserver.services

import com.github.alpha.authorizationserver.exception.ResetPasswordNotFoundException
import com.github.diogodelima.authorizationserver.domain.LIFETIME
import com.github.diogodelima.authorizationserver.domain.ResetPassword
import com.github.diogodelima.authorizationserver.domain.User
import com.github.diogodelima.authorizationserver.repositories.ResetPasswordRepository
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

@Service
class ResetPasswordService(

    private val resetPasswordRepository: ResetPasswordRepository

) {

    fun getResetPasswordByUser(user: User): ResetPassword {

        var resetPassword = resetPasswordRepository.findResetPasswordByUserAndCreatedAtBefore(user) ?: resetPasswordRepository.save(
            ResetPassword(user = user)
        )

        if (resetPassword.createdAt < Instant.now().toEpochMilli() - LIFETIME)
            resetPassword = resetPasswordRepository.save(
                resetPassword.copy(
                    createdAt = Instant.now().toEpochMilli(), token = UUID.randomUUID()
                )
            )

        return resetPassword
    }

    fun deleteResetPassword(resetPassword: ResetPassword) =
        resetPasswordRepository.delete(resetPassword)

    fun getResetPasswordByToken(token: UUID) =
        resetPasswordRepository.findByToken(token) ?: throw ResetPasswordNotFoundException()

}