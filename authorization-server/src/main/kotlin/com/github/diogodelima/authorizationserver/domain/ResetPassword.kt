package com.github.diogodelima.authorizationserver.domain

import jakarta.persistence.*
import java.time.Instant
import java.util.*

const val LIFETIME = 1000L * 60 * 10

@Entity
@Table(name = "reset_password")
data class ResetPassword(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    val token: String = UUID.randomUUID().toString(),

    val createdAt: Long = Instant.now().toEpochMilli(),

    @OneToOne
    val user: User

)