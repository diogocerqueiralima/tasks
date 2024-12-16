package com.github.diogodelima.authorizationserver.repositories

import com.github.diogodelima.authorizationserver.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {

    fun findUserByUsername(username: String?): User?

}