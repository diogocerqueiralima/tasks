package com.github.diogodelima.authorizationserver.domain

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.Instant
import java.time.LocalDateTime

@Entity
@Table(name = "users")
data class User(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val email: String,

    @get:JvmName("username")
    @Column(nullable = false)
    val username: String,

    @get:JvmName("password")
    @Column(nullable = false)
    val password: String,

    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()

) : UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
        mutableListOf()

    override fun getPassword(): String =
        password

    override fun getUsername(): String =
        username

}