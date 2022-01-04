package lmu.msp.backend.controller.api.v1

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import lmu.msp.backend.model.Campaign
import lmu.msp.backend.model.CampaignMember
import lmu.msp.backend.service.ICampaignService
import lmu.msp.backend.utility.getAuth0IdFromAuthentication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import javax.validation.constraints.Size

@RestController
@RequestMapping("/api/v1/campaign/member")
@SecurityRequirement(name = "bearer-key")
class CampaignMemberController(@Autowired private val campaignService: ICampaignService) {

    @Operation(summary = "load and return all campaignMembers. Requesting user must be owner or member")
    @GetMapping
    fun getMembers(authentication: Authentication, @RequestParam campaignId: Long): List<CampaignMember>? {
        val auth0Id = getAuth0IdFromAuthentication(authentication)
        return campaignService.getMembers(auth0Id, campaignId)
    }

    @Operation(summary = "removes user from the campaign. If Requesting user is still in the campaign returns Campaign obj (when owner deletes member)")
    @DeleteMapping
    fun deleteMember(authentication: Authentication, @RequestBody campaignId: Long): Campaign {
        val auth0Id = getAuth0IdFromAuthentication(authentication)
        TODO("Not yet implemented")
    }

    @Operation(summary = "change member information. For now character name only. User must be a member of the campaign")
    @PutMapping
    fun updateMember(
        authentication: Authentication,
        @RequestBody campaignId: Long,
        @Size(max = 45)
        @RequestBody name: String
    ): Boolean {
        val auth0Id = getAuth0IdFromAuthentication(authentication)
        TODO("Not yet implemented")
    }

    @Operation(summary = "user needs to accept an invite to join a campaing as a member. Owner is not allowed to join as a member. For now invite is not needed. Only campaignId.")
    @PostMapping("/invite/accept")
    fun acceptInvite(authentication: Authentication, @RequestBody campaignId: Long): Campaign {
        val auth0Id = getAuth0IdFromAuthentication(authentication)
        TODO("Not yet implemented")
    }

    @Operation(summary ="Not yet implemented")
    @GetMapping("/invite/link")
    fun getInviteLink(authentication: Authentication,@RequestBody campaignId: Long){
        TODO("Not yet implemented")
    }
}