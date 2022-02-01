package lmu.msp.backend.controller.api.v1

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import lmu.msp.backend.model.User
import lmu.msp.backend.service.IUserService
import lmu.msp.backend.utility.getAuth0IdFromAuthentication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

/**
 * all rest calls which interacts with the user data
 *
 * @property userService
 */
@RestController
@RequestMapping("/api/v1/user")
@SecurityRequirement(name = "bearer-key")
class UserController(@Autowired private val userService: IUserService) {

    @Operation(summary = "load and return user obj of the authenticated user")
    @ApiResponses(
        ApiResponse(responseCode = "200"),
        ApiResponse(responseCode = "401", description = "Invalid Authentication", content = [Content()])
    )
    @GetMapping
    fun getUser(authentication: Authentication): User {
        val auth0Id = getAuth0IdFromAuthentication(authentication)
        return userService.getUserByAuth0Id(auth0Id)
    }

    @ApiResponses(
        ApiResponse(responseCode = "200"),
        ApiResponse(responseCode = "401", description = "Invalid Authentication", content = [Content()])
    )
    @PostMapping("/{id}/avatar", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun setUserProfilePicture(
        authentication: Authentication,
        @PathVariable id: String,
        @RequestParam("image") picture: MultipartFile
    ): ResponseEntity<Boolean> {
        val auth0Id = getAuth0IdFromAuthentication(authentication)

        return ResponseEntity
            .ok()
            .body(userService.updateProfileImage(auth0Id, picture.bytes))

    }

    @ApiResponses(
        ApiResponse(responseCode = "200"),
        ApiResponse(responseCode = "401", description = "Invalid Authentication", content = [Content()]),
        ApiResponse(responseCode = "404", content = [Content()])
    )
    @GetMapping("/{id}/avatar")
    fun getUserProfilePicture(authentication: Authentication, @PathVariable id: Long): ResponseEntity<ByteArray> {
        val auth0Id = getAuth0IdFromAuthentication(authentication)

        val byteArray = userService.getProfileImage(auth0Id, id)

        return if (null == byteArray) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        } else {
            ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(MediaType.IMAGE_PNG_VALUE))
                .body(byteArray)
        }
    }

}