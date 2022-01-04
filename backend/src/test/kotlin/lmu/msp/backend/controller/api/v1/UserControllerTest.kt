package lmu.msp.backend.controller.api.v1

import lmu.msp.backend.model.User
import lmu.msp.backend.repository.UserRepository
import org.hamcrest.CoreMatchers.`is`
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@AutoConfigureMockMvc
@SpringBootTest
internal class UserControllerTest(
    @Autowired private val mockMvc: MockMvc
) {

    private companion object {
        private const val path = "/api/v1/user"

        private lateinit var user1: User
        private lateinit var user2: User

        @JvmStatic
        @BeforeAll
        fun init(@Autowired userRepository: UserRepository) {
            user1 = userRepository.save(User("user1"))
            user2 = userRepository.save(User("user2"))
        }

        @JvmStatic
        @AfterAll
        fun tearDown(@Autowired userRepository: UserRepository) {
            userRepository.deleteAll()
        }
    }

    @Test
    @WithMockUser(username = "user1")
    fun getUser() {
        mockMvc.get(path)
            .andExpect {
                status { isOk() }
                content { jsonPath("$.id", `is`(user1.id.toInt())) }
            }
    }

    @Test
    @WithMockUser(username = "user2")
    fun getUserDifferentId() {
        mockMvc.get(path)
            .andExpect {
                status { isOk() }
                content { jsonPath("$.id", `is`(user2.id.toInt())) }
            }
    }

    @Test
    @WithMockUser(username = "user3")
    fun getNewUser(@Autowired userRepository: UserRepository) {
        mockMvc.get(path)
            .andExpect {
                status { isOk() }
                content { jsonPath("$.id", `is`(userRepository.findUserByAuth0Id("user3")!!.id.toInt())) }
            }
        //check if second call == same user obj
        mockMvc.get(path)
            .andExpect {
                status { isOk() }
                content { jsonPath("$.id", `is`(userRepository.findUserByAuth0Id("user3")!!.id.toInt())) }
            }
    }
}