package lmu.msp.backend.controller

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get


@WebMvcTest
class HelloWorldTest(@Autowired private val mockMvc: MockMvc) {

    @Test
    fun helloWorldReturnDefaultMessage() {
        mockMvc.get("/hello-world")
            .andExpect {
                status { isOk() }
                content { string("Hello World!") }
            }
    }

}