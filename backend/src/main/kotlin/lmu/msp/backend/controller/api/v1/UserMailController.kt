package lmu.msp.backend.controller.api.v1

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import lmu.msp.backend.model.Mail
import lmu.msp.backend.service.IMailService
import lmu.msp.backend.utility.getAuth0IdFromAuthentication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

/**
 * all rest calls which interacts with the user mails
 * you need to ask the server if there are new messages (get) and send new messages with (post)
 *
 * @property mailService
 */
@RestController
@RequestMapping("/api/v1/user/mail")
@SecurityRequirement(name = "bearer-key")
class UserMailController(@Autowired private val mailService: IMailService) {

    @Operation(summary = "load and return all mails a user send and received. If an contactId is provided it will only return Mails to and from this contact")
    @ApiResponses(
        ApiResponse(responseCode = "200"),
        ApiResponse(responseCode = "401", description = "Invalid Authentication", content = [Content()])
    )
    @GetMapping
    fun getMails(authentication: Authentication, @RequestParam(required = false) contactId: Long?): List<Mail> {
        val auth0Id = getAuth0IdFromAuthentication(authentication)
        return mailService.getMails(auth0Id, contactId)
    }

    @Operation(summary = "send a mail to a user. The saved mail obj will be returned. If the Mail was invalid it will return null")
    @ApiResponses(
        ApiResponse(responseCode = "200"),
        ApiResponse(responseCode = "401", description = "Invalid Authentication", content = [Content()]),
        ApiResponse(responseCode = "404", description = "Receiver not found", content = [Content()])
    )
    @PostMapping(consumes = ["text/plain"])
    fun sendMail(
        authentication: Authentication,
        @RequestParam receiverId: Long,
        @RequestBody message: String
    ): ResponseEntity<Mail> {
        val auth0Id = getAuth0IdFromAuthentication(authentication)

        val mail = mailService.sendMail(auth0Id, receiverId, message)
        return if (null == mail) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        } else {
            ResponseEntity(mail, HttpStatus.OK)
        }
    }

    @Operation(summary = "delete a mail. Return true if delete was successful")
    @ApiResponses(
        ApiResponse(responseCode = "200"),
        ApiResponse(responseCode = "401", description = "Invalid Authentication", content = [Content()])
    )
    @DeleteMapping
    fun delMail(authentication: Authentication, @RequestParam mailId: Long): Boolean {
        val auth0Id = getAuth0IdFromAuthentication(authentication)
        return mailService.delMail(auth0Id, mailId)
    }
}