package lmu.msp.backend.controller

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get


@AutoConfigureMockMvc
@SpringBootTest
@WebAppConfiguration
internal class HelloWorldTest(@Autowired private val mockMvc: MockMvc) {

    @Test
    fun injectedComponentsAreNotNull() {
        Assertions.assertThat(mockMvc).isNotNull
    }

    @Test
    fun helloWorldReturnDefaultMessage() {
        mockMvc.get("/hello-world")
            .andExpect {
                status { isOk() }
                content { string("Hello World!") }
            }
    }

}