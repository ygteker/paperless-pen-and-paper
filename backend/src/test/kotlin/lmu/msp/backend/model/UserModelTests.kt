package lmu.msp.backend.model

import lmu.msp.backend.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.dao.DataIntegrityViolationException

@DataJpaTest
class UserModelTests(
    @Autowired private val userRepository: UserRepository
) {

    @Test
    fun injectedComponentsAreNotNull() {
        assertThat(userRepository).isNotNull
    }

    @Test
    fun autoGenerateUserIdTest() {
        val u1 = User("u1")
        val u2 = User("u2")

        userRepository.save(u1)
        userRepository.save(u2)
        assertThat(userRepository.findById(1).get().auth0Id).isEqualTo("u1")
        assertThat(userRepository.findById(2).get().auth0Id).isEqualTo("u2")
    }

    @Test
    fun uniqueAuth0IdTest() {
        val u1 = User("u")
        val u2 = User("u")

        userRepository.save(u1)
        assertThrows<DataIntegrityViolationException> { userRepository.saveAndFlush(u2) }
    }


}
