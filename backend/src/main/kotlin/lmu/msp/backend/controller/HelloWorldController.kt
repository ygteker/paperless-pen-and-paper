package lmu.msp.backend.controller

import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * this is a demo controller.
 * it creates a REST endpoint for get "/hello-world"
 * you can use this endpoint to check if the backend is online and that the backend accepts your JWT token (/hello-world/authentication)
 */
@RestController
@RequestMapping("/hello-world")
class HelloWorldController {

    @ApiResponses(ApiResponse(responseCode = "200"))
    @GetMapping
    fun getHelloWorld(): String {
        return "Hello World!"
    }


    @ApiResponses(
        ApiResponse(responseCode = "200"),
        ApiResponse(responseCode = "401", description = "Invalid Authentication", content = [Content()])
    )
    @GetMapping("/authentication")
    @SecurityRequirement(name = "bearer-key")
    fun getHelloWorldWithAuthentication(authentication: Authentication): String {
        return "Hello ${authentication.name}!"
    }

}