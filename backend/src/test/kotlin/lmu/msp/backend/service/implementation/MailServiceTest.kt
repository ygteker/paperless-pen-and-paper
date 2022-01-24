package lmu.msp.backend.service.implementation

import lmu.msp.backend.model.Mail
import lmu.msp.backend.model.User
import lmu.msp.backend.repository.MailRepository
import lmu.msp.backend.repository.UserRepository
import lmu.msp.backend.service.IMailService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.web.WebAppConfiguration
import javax.persistence.EntityManager

@SpringBootTest
@WebAppConfiguration
internal class MailServiceTest(@Autowired private val mailService: IMailService) {


    private val testMsg = "msg"
    private val auth0U1 = "u1"
    private val auth0U2 = "u2"
    private val auth0U3 = "u3"

    private var u2Id = 0L

    private var mail1Id = 0L
    private var mail4Id = 0L

    @BeforeEach
    fun initTest(
        @Autowired userRepository: UserRepository,
        @Autowired mailRepository: MailRepository
    ) {
        /**
         * setup:
         * 1. u1 -> u2: msg
         * 2. u1 -> u3: msg
         * 3. u2 -> u1: msg
         * 4. u2 -> u3: msg
         */

        val u1 = userRepository.save(User(auth0U1))
        val u2 = userRepository.save(User(auth0U2))

        val u3 = userRepository.save(User(auth0U3))

        mail1Id = mailRepository.save(Mail(u1, u2, testMsg)).id
        mailRepository.save(Mail(u1, u3, testMsg))
        mailRepository.save(Mail(u2, u1, testMsg))
        mail4Id = mailRepository.save(Mail(u2, u3, testMsg)).id

        u2Id = u2.id
    }

    @AfterEach
    fun tearDown(
        @Autowired entityManager: EntityManager,
        @Autowired userRepository: UserRepository,
        @Autowired mailRepository: MailRepository
    ) {
        entityManager.clear()
        userRepository.deleteAll()
        mailRepository.deleteAll()
    }


    @Test
    fun getMails() {
        assertThat(mailService.getMails(auth0U1).size).isEqualTo(3)
        assertThat(mailService.getMails(auth0U1, u2Id).size).isEqualTo(2)
    }

    @Test
    fun sendMail() {

        val mail = mailService.sendMail(auth0U1, u2Id, testMsg)
        assertThat(mail).isNotNull
        assertThat(mailService.getMails(auth0U1).size).isEqualTo(4)

        mailService.getMails(auth0U1).forEach { assertThat(it.message).isEqualTo(testMsg) }
    }

    @Test
    fun sendMailWithWrongReceiver() {
        val mail = mailService.sendMail(auth0U1, -1, testMsg)
        assertThat(mail).isNull()
    }

    @Test
    fun delMail() {
        assertThat(mailService.delMail(auth0U1, mail1Id)).isTrue
        assertThat(mailService.delMail(auth0U1, mail1Id)).isFalse
        assertThat(mailService.delMail(auth0U2, mail1Id)).isFalse //deleting mail as receiver
        assertThat(mailService.delMail(auth0U1, mail4Id)).isFalse //deleting mail without being part of the mail
        assertThat(mailService.getMails(auth0U1).size).isEqualTo(2) //confirm mail was deleted
        assertThat(mailService.getMails(auth0U3).size).isEqualTo(2) //confirm mail was not deleted
    }
}