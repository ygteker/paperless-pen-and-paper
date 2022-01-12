package lmu.msp.backend.controller.api.v1

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import lmu.msp.backend.model.Mail
import lmu.msp.backend.utility.getAuth0IdFromAuthentication
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/user/mail")
@SecurityRequirement(name = "bearer-key")
class UserMailController {

    @Operation(summary = "load and return all mails a user send and received. If an contactId is provided it will only return Mails to and from this contact")
    @GetMapping
    fun getMails(authentication: Authentication, @RequestParam(required = false) contactId: Long): List<Mail> {
        val auth0Id = getAuth0IdFromAuthentication(authentication)
        TODO("not yet implemented")
    }

    @Operation(summary = "send a mail to a user. The saved mail obj will be returned. If the Mail was invalid it will return null")
    @PostMapping
    fun sendMail(authentication: Authentication, @RequestBody mail: Mail): Mail? {
        val auth0Id = getAuth0IdFromAuthentication(authentication)
        TODO("not yet implemented")
    }

    @Operation(summary = "delete a mail. Return true if delete was successful")
    @DeleteMapping
    fun delMail(authentication: Authentication, @RequestParam mailId: Long): Boolean {
        val auth0Id = getAuth0IdFromAuthentication(authentication)
        TODO("not yet implemented")
    }
}