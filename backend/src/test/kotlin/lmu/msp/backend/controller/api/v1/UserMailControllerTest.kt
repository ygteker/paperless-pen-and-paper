package lmu.msp.backend.controller.api.v1

import lmu.msp.backend.model.Mail
import lmu.msp.backend.model.User
import lmu.msp.backend.repository.MailRepository
import lmu.msp.backend.repository.UserRepository
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import javax.persistence.EntityManager

@AutoConfigureMockMvc
@SpringBootTest
@WebAppConfiguration
internal class UserMailControllerTest(@Autowired private val mockMvc: MockMvc) {

    private val path = "/api/v1/user/mail"

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
    @WithMockUser(username = "u1")
    fun getMails() {
        mockMvc.perform(get(path))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$", hasSize<Mail>(3)))
        mockMvc.perform(get(path).param("contactId", u2Id.toString()))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$", hasSize<Mail>(2)))
    }

    @Test
    @WithMockUser(username = "u1")
    fun sendMail() {
        val message = "new title"
        mockMvc.perform(
            (MockMvcRequestBuilders.post(path).param("receiverId", u2Id.toString()))
                .content(message)
                .contentType("text/plain")
                .with(csrf().asHeader())
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.message").value(message))
        mockMvc.perform(
            (MockMvcRequestBuilders.post(path).param("receiverId", (-1).toString()))
                .content(message)
                .contentType("text/plain")
                .with(csrf().asHeader())
        )
            .andExpect(status().isNotFound)
    }

    @Test
    @WithMockUser(username = "u1")
    fun delMail() {
        mockMvc.perform(delete(path).param("mailId", mail1Id.toString()).with(csrf().asHeader()))
            .andExpect(status().isOk)
            .andExpect(content().string("true"))
        mockMvc.perform(delete(path).param("mailId", mail1Id.toString()).with(csrf().asHeader()))
            .andExpect(status().isOk)
            .andExpect(content().string("false"))
        mockMvc.perform(delete(path).param("mailId", mail4Id.toString()).with(csrf().asHeader()))
            .andExpect(status().isOk)
            .andExpect(content().string("false"))

        mockMvc.perform(get(path))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$", hasSize<Mail>(2)))

    }
}