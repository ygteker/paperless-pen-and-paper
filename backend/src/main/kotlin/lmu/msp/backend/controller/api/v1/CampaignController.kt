package lmu.msp.backend.controller.api.v1

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import lmu.msp.backend.model.Campaign
import lmu.msp.backend.service.ICampaignService
import lmu.msp.backend.utility.getAuth0IdFromAuthentication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import javax.validation.constraints.Size

@RestController
@RequestMapping("/api/v1/campaign")
@SecurityRequirement(name = "bearer-key")
class CampaignController(@Autowired private val campaignService: ICampaignService) {

    @Operation(summary = "load and return campaign obj. If the authenticated user don't exist or isn't a member/owner of the campaign return error code")
    @GetMapping
    fun getCampaign(authentication: Authentication, @RequestParam campaignId: Long): Campaign? {
        val auth0Id = getAuth0IdFromAuthentication(authentication)
        return campaignService.getCampaign(auth0Id, campaignId)
    }

    @Operation(summary = "create a new campaign. The requesting user is the owner of the campaign")
    @PostMapping
    fun createCampaign(authentication: Authentication, @Size(max = 45) @RequestBody campaignName: String): Campaign {
        val auth0Id = getAuth0IdFromAuthentication(authentication)
        return campaignService.createCampaign(auth0Id, campaignName)
    }

    @Operation(summary = "delete campaign. The requesting user must be the owner")
    @DeleteMapping
    fun deleteCampaign(authentication: Authentication, @RequestParam campaignId: Long): Boolean {
        val auth0Id = getAuth0IdFromAuthentication(authentication)
        return campaignService.deleteCampaign(auth0Id, campaignId)
    }
}