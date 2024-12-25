package com.github.diogodelima.authorizationserver.tasks

import com.github.diogodelima.authorizationserver.domain.LIFETIME
import com.github.diogodelima.authorizationserver.repositories.ResetPasswordRepository
import jakarta.transaction.Transactional
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class ResetPasswordTask(

    private val resetPasswordRepository: ResetPasswordRepository

) {

    @Scheduled(fixedRate = 1000L * 60 * 60)
    @Transactional
    fun deleteResetPasswordTask() {
        resetPasswordRepository.deleteResetPasswordByCreatedAtBefore(Instant.now().toEpochMilli() - LIFETIME)
        println("Deleted reset password")
    }

}