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

@Service
class UserService(

    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val kafkaTemplate: KafkaTemplate<String, UserForgotPasswordDto>

) : UserDetailsService {

    override fun loadUserByUsername(username: String?): User =
        userRepository.findUserByUsername(username) ?: userRepository.findUserByEmail(username) ?: throw UsernameNotFoundException("User $username not found")

    fun create(username: String, email: String, password: String, confirmPassword: String) {

        if (password != confirmPassword)
            throw PasswordNotMatchException()

        if (userRepository.findUserByUsername(username) != null || userRepository.findUserByEmail(email) != null)
            throw UserAlreadyExistsException()

        userRepository.save(
            User(
                username = username,
                email = email,
                password = passwordEncoder.encode(password)
            )
        )

    }

    fun requestEmailToResetPassword(dto: UserForgotPasswordDto) {

        userRepository.findUserByUsername(dto.usernameOrEmail) ?: userRepository.findUserByEmail(dto.usernameOrEmail) ?: throw UsernameNotFoundException("User ${dto.usernameOrEmail} not found")

        kafkaTemplate.send("forgot-password", dto)
    }

}