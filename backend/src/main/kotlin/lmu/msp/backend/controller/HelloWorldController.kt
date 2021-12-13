package lmu.msp.backend.controller

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

}