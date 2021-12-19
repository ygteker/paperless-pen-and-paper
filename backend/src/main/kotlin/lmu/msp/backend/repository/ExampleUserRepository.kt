package lmu.msp.backend.repository

import lmu.msp.backend.model.ExampleUser
import org.springframework.data.jpa.repository.JpaRepository

interface ExampleUserRepository : JpaRepository<ExampleUser, Long> {
    fun findUserByAuth0Id(auth0Id: String): ExampleUser?
}