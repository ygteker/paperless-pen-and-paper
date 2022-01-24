package lmu.msp.backend.security

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get


@AutoConfigureMockMvc
@SpringBootTest
@WebAppConfiguration
internal class RestApiSecurityTest(@Autowired val mockMvc: MockMvc) {

    //ignore swagger-ui here in the mock it's not enabled. The endpoints would return 404
    private val publicEndpoints = arrayOf("/hello-world")

    //todo add all future endpoints
    private val privateEndpoints = arrayOf("/hello-world/authentication")

    @Test
    fun injectedComponentsAreNotNull() {
        Assertions.assertThat(mockMvc).isNotNull
    }

    @Test
    fun publicEndpointExists() {
        publicEndpoints.forEach {
            mockMvc.get(it)
                .andExpect {
                    status { isOk() }
                }
        }
    }

    @Test
    fun privateEndpointNoUserUnauthorized() {
        privateEndpoints.forEach {
            mockMvc.get(it).andExpect {
                status { isUnauthorized() }
            }
        }
    }

    @Test
    @WithMockUser(username = "testUser")
    fun privateEndpointWithUserOkay() {
        privateEndpoints.forEach {
            mockMvc.get(it)
                .andExpect {
                    status { isOk() }
                }
        }
    }
}