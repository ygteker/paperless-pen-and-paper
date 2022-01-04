package lmu.msp.backend.repository

import lmu.msp.backend.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findUserByAuth0Id(auth0Id: String): User?

}
