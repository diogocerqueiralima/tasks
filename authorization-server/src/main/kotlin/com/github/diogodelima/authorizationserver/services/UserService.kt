package com.github.diogodelima.authorizationserver.services

import com.github.diogodelima.authorizationserver.domain.User
import com.github.diogodelima.authorizationserver.dto.UserForgotPasswordDto
import com.github.diogodelima.authorizationserver.exception.PasswordNotMatchException
import com.github.diogodelima.authorizationserver.exception.UserAlreadyExistsException
import com.github.diogodelima.authorizationserver.repositories.UserRepository
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserService(

    private val userRepository: UserRepository,
    private val resetPasswordService: ResetPasswordService,
    private val passwordEncoder: PasswordEncoder,
    private val kafkaTemplate: KafkaTemplate<String, UserForgotPasswordDto>

) : UserDetailsService {

    override fun loadUserByUsername(username: String?): User =
        userRepository.findUserByUsername(username) ?: userRepository.findUserByEmail(username) ?: throw UsernameNotFoundException("User $username not found")

    fun create(username: String, email: String, password: String, confirmPassword: String): User {

        if (password != confirmPassword)
            throw PasswordNotMatchException()

        if (userRepository.findUserByUsername(username) != null || userRepository.findUserByEmail(email) != null)
            throw UserAlreadyExistsException()

        return userRepository.save(
            User(
                username = username,
                email = email,
                password = passwordEncoder.encode(password)
            )
        )

    }

    fun requestEmailToResetPassword(dto: UserForgotPasswordDto) {

        val user = userRepository.findUserByUsername(dto.username) ?: userRepository.findUserByEmail(dto.username) ?: throw UsernameNotFoundException("User ${dto.username} not found")
        val resetPassword = resetPasswordService.getResetPasswordByUser(user)

        kafkaTemplate.send("forgot-password", dto.copy(username = user.username, email = user.email, url = "http://localhost:9000/auth/reset?token=${resetPassword.token}"))
    }

    fun resetPassword(token: UUID, password: String): User {

        val resetPassword = resetPasswordService.getResetPasswordByToken(token)
        val user = resetPassword.user

        resetPasswordService.deleteResetPassword(resetPassword)

        return userRepository.save(
            user.copy(password = passwordEncoder.encode(password))
        )

    }

}