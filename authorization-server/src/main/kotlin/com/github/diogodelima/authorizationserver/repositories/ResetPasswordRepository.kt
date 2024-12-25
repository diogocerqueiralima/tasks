package com.github.diogodelima.authorizationserver.repositories

import com.github.diogodelima.authorizationserver.domain.ResetPassword
import com.github.diogodelima.authorizationserver.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import java.time.Instant

interface ResetPasswordRepository : JpaRepository<ResetPassword, Int> {

    fun findResetPasswordByUserAndCreatedAtBefore(user: User, createdAtBefore: Long = Instant.now().toEpochMilli()): ResetPassword?

    fun findByToken(token: String): ResetPassword?

    fun deleteResetPasswordByCreatedAtBefore(createdAtBefore: Long)

}