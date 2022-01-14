package lmu.msp.backend.model

import lmu.msp.backend.repository.MailRepository
import lmu.msp.backend.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import java.sql.Timestamp
import java.util.*
import javax.persistence.EntityManager

@DataJpaTest
internal class MailTest(
    @Autowired private val mailRepository: MailRepository
) {

    val testMsg = "msg"
    val auth0U1 = "u1"
    val auth0U2 = "u2"
    val auth0U3 = "u3"

    var mail1Id = 0L

    /**
     * test:
     * - save mail
     * - get mail
     * - delete mail
     */

    @BeforeEach
    fun initTest(
        @Autowired userRepository: UserRepository
    ) {
        /**
         * setup:
         * u1 -> u2: msg
         * u1 -> u3: msg
         * u2 -> u1: msg
         * u2 -> u3: msg
         */

        val u1 = userRepository.save(User(auth0U1))
        val u2 = userRepository.save(User(auth0U2))

        val u3 = userRepository.save(User(auth0U3))


        mail1Id = mailRepository.save(Mail(u1, u2, testMsg)).id
        mailRepository.save(Mail(u1, u3, testMsg))
        mailRepository.save(Mail(u2, u1, testMsg))
        mailRepository.save(Mail(u2, u3, testMsg))
    }

    @AfterEach
    fun tearDown(
        @Autowired entityManager: EntityManager,
        @Autowired userRepository: UserRepository
    ) {
        entityManager.clear()
        userRepository.deleteAll()
        mailRepository.deleteAll()
    }

    @Test
    fun getMail(@Autowired userRepository: UserRepository) {
        val u1 = userRepository.findUserByAuth0Id(auth0U1)!!
        val u2 = userRepository.findUserByAuth0Id(auth0U2)!!
        val u3 = userRepository.findUserByAuth0Id(auth0U3)!!

        assertThat(mailRepository.findBySender_IdOrReceiver_Id(u1.id).size).isEqualTo(3)
        assertThat(mailRepository.findBySender_IdOrReceiver_Id(u3.id).size).isEqualTo(2)
        assertThat(
            mailRepository.findBySender_IdAndReceiver_IdOrSender_IdAndReceiver_Id(u1.id, u2.id).size
        ).isEqualTo(2)

        mailRepository.findBySender_IdOrReceiver_Id(u1.id).forEach {
            assertThat(it.sender.id == u1.id || it.receiver.id == u1.id)
        }
    }

    @Test
    fun saveMail(@Autowired userRepository: UserRepository) {
        val u1 = userRepository.findUserByAuth0Id(auth0U1)!!
        val u2 = userRepository.findUserByAuth0Id(auth0U2)!!

        val newMail = Mail(u1, u2, testMsg)
        val savedMail = mailRepository.save(newMail)

        assertThat(
            mailRepository.findBySender_IdAndReceiver_IdOrSender_IdAndReceiver_Id(
                u1.id,
                u2.id
            ).size
        ).isEqualTo(3)

        assertThat(savedMail.time).isAfter(Timestamp(0))


    }

    @Test
    fun deleteMail(@Autowired userRepository: UserRepository) {
        val u1 = userRepository.findUserByAuth0Id(auth0U1)!!

        assertThat(mailRepository.deleteBySender_IdAndIdAllIgnoreCase(u1.id, mail1Id)).isEqualTo(1)
        assertThat(mailRepository.findBySender_IdOrReceiver_Id(u1.id).size).isEqualTo(2)
    }

}