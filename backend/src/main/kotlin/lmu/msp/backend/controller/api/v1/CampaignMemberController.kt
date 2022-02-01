package lmu.msp.backend.controller.api.v1

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import lmu.msp.backend.model.CampaignMember
import lmu.msp.backend.service.ICampaignService
import lmu.msp.backend.utility.getAuth0IdFromAuthentication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import javax.validation.constraints.Size

/**
 * all rest calls which interacts with the campaign members
 *
 * @property campaignService
 */
@RestController
@RequestMapping("/api/v1/campaign/member")
@SecurityRequirement(name = "bearer-key")
class CampaignMemberController(@Autowired private val campaignService: ICampaignService) {

    @Operation(summary = "load and return all campaignMembers. Requesting user must be owner or member")
    @ApiResponses(
        ApiResponse(responseCode = "200"),
        ApiResponse(responseCode = "401", description = "Invalid Authentication", content = [Content()]),
        ApiResponse(
            responseCode = "404",
            description = "Campaign not found or user is not a member nor a owner",
            content = [Content()]
        )
    )
    @GetMapping
    fun getMembers(
        authentication: Authentication,
        @RequestParam campaignId: Long
    ): ResponseEntity<List<CampaignMember>> {
        val auth0Id = getAuth0IdFromAuthentication(authentication)
        val memberList = campaignService.getMembers(auth0Id, campaignId)
        return if (null == memberList) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        } else {
            ResponseEntity(memberList, HttpStatus.OK)
        }
    }

    @Operation(summary = "removes user from the campaign. If the requesting user is still in the campaign returns List of remaining campaign members")
    @ApiResponses(
        ApiResponse(responseCode = "200"),
        ApiResponse(responseCode = "401", description = "Invalid Authentication", content = [Content()]),
        ApiResponse(
            responseCode = "404",
            description = "Campaign not found, User not a Member, User not the Owner of the Campaign or UserToRemoveId not a Member",
            content = [Content()]
        )
    )
    @DeleteMapping
    fun deleteMember(
        authentication: Authentication,
        @RequestParam campaignId: Long,
        @RequestParam userToRemoveId: Long
    ): ResponseEntity<List<CampaignMember>> {
        val auth0Id = getAuth0IdFromAuthentication(authentication)
        val campaignMember = campaignService.removeMember(auth0Id, campaignId, userToRemoveId)
        return if (null == campaignMember) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        } else {
            ResponseEntity(campaignMember, HttpStatus.OK)
        }
    }

    @Operation(summary = "change member information. For now character name only. User must be a member of the campaign")
    @ApiResponses(
        ApiResponse(responseCode = "200"),
        ApiResponse(responseCode = "401", description = "Invalid Authentication", content = [Content()]),
        ApiResponse(
            responseCode = "404",
            description = "Campaign not found or user not a Member",
            content = [Content()]
        )
    )
    @PutMapping
    fun updateMember(
        authentication: Authentication,
        @RequestParam campaignId: Long,
        @Size(max = 45)
        @RequestParam name: String
    ): ResponseEntity<CampaignMember> {
        val auth0Id = getAuth0IdFromAuthentication(authentication)
        val campaign = campaignService.renameMember(auth0Id, campaignId, name)
        return if (null == campaign) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        } else {
            ResponseEntity(campaign, HttpStatus.OK)
        }
    }

    @Operation(summary = "user needs to accept an invite to join a campaign as a member. Owner is not allowed to join as a member. For now invite is not needed. Only campaignId.")
    @ApiResponses(
        ApiResponse(responseCode = "200"),
        ApiResponse(responseCode = "401", description = "Invalid Authentication", content = [Content()]),
        ApiResponse(
            responseCode = "404",
            description = "Campaign not found, User already a Member",
            content = [Content()]
        )
    )
    @PostMapping("/invite/accept")
    fun acceptInvite(
        authentication: Authentication,
        @RequestParam campaignId: Long,
        @Size(max = 45)
        @RequestParam name: String
    ): ResponseEntity<CampaignMember> {
        val auth0Id = getAuth0IdFromAuthentication(authentication)
        val campaignMember = campaignService.addMember(auth0Id, campaignId, name)
        return if (null == campaignMember) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        } else {
            ResponseEntity(campaignMember, HttpStatus.OK)
        }
    }

    @Operation(summary = "Not yet implemented")
    @GetMapping("/invite/link")
    fun getInviteLink(authentication: Authentication, @RequestBody campaignId: Long) {
        TODO("Not yet implemented")
    }
}