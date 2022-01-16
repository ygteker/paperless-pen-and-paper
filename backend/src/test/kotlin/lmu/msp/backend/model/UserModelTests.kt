package lmu.msp.backend.model

import lmu.msp.backend.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.dao.DataIntegrityViolationException

@DataJpaTest
internal class UserModelTests(
    @Autowired private val userRepository: UserRepository
) {

    @Test
    fun injectedComponentsAreNotNull() {
        assertThat(userRepository).isNotNull
    }

    @Test
    fun autoGenerateUserIdTest() {
        var u1 = User("u1")
        var u2 = User("u2")

        u1 = userRepository.save(u1)
        u2 = userRepository.save(u2)
        assertThat(u1.id).isNotEqualTo(0)
        assertThat(u2.id).isNotEqualTo(0)
    }

    @Test
    fun defaultProfilePictureIsEmpty() {
        val u1 = userRepository.save(User("u1"))
        assertThat(u1.image).isEmpty()
    }

    @Test
    fun uniqueAuth0IdTest() {
        val u1 = User("u")
        val u2 = User("u")

        userRepository.save(u1)
        assertThrows<DataIntegrityViolationException> { userRepository.saveAndFlush(u2) }
    }

}
