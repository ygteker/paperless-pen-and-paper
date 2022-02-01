package lmu.msp.backend.repository

import lmu.msp.backend.model.User
import org.springframework.data.jpa.repository.JpaRepository

/**
 * jpa repository
 * used to generate queries to the DB
 * jpa will generate queries from the functions
 *
 */
interface UserRepository : JpaRepository<User, Long> {
    fun findUserByAuth0Id(auth0Id: String): User?

}
