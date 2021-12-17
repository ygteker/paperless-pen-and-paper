package lmu.msp.backend.controller

import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * this is a demo controller.
 * it creates a REST endpoint for get "/hello-world"
 */
@RestController
@RequestMapping("/hello-world")
class HelloWorldController {
    @GetMapping
    fun getHelloWorld(): String {
        return "Hello World!"
    }

    @GetMapping("/authentication")
    @SecurityRequirement(name = "bearer-key")
    fun getHelloWorldWithAuthentication(authentication: Authentication): String {
        return "Hello ${authentication.name}!"
    }

}