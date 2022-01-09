package lmu.msp.backend.controller.api.v1

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import lmu.msp.backend.model.User
import lmu.msp.backend.service.IUserService
import lmu.msp.backend.utility.getAuth0IdFromAuthentication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/user")
@SecurityRequirement(name = "bearer-key")
class UserController(@Autowired private val userService: IUserService) {

    @Operation(summary = "load and return user obj of the authenticated user")
    @GetMapping
    fun getUser(authentication: Authentication): User {
        val auth0Id = getAuth0IdFromAuthentication(authentication)
        return userService.getUserByAuth0Id(auth0Id)
    }

}