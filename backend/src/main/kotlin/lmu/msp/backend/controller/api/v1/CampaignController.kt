package lmu.msp.backend.controller.api.v1

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import lmu.msp.backend.model.Campaign
import lmu.msp.backend.service.ICampaignService
import lmu.msp.backend.utility.getAuth0IdFromAuthentication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import javax.validation.constraints.Size

/**
 * all rest calls which interacts with the campaigns (except members)
 *
 * @property campaignService
 */
@RestController
@RequestMapping("/api/v1/campaign")
@SecurityRequirement(name = "bearer-key")
class CampaignController(@Autowired private val campaignService: ICampaignService) {

    @Operation(summary = "load and return campaign obj. If the authenticated user don't exist or isn't a member/owner of the campaign return error code")
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
    fun getCampaign(authentication: Authentication, @RequestParam campaignId: Long): ResponseEntity<Campaign> {
        val auth0Id = getAuth0IdFromAuthentication(authentication)
        val campaign = campaignService.getCampaign(auth0Id, campaignId)
        return if (null == campaign) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        } else {
            ResponseEntity(campaign, HttpStatus.OK)
        }
    }

    @Operation(summary = "create a new campaign. The requesting user is the owner of the campaign")
    @ApiResponses(
        ApiResponse(responseCode = "200"),
        ApiResponse(responseCode = "401", description = "Invalid Authentication", content = [Content()])
    )
    @PostMapping
    fun createCampaign(authentication: Authentication, @Size(max = 45) @RequestBody campaignName: String): Campaign {
        val auth0Id = getAuth0IdFromAuthentication(authentication)
        return campaignService.createCampaign(auth0Id, campaignName)
    }

    @Operation(summary = "delete campaign. The requesting user must be the owner")
    @ApiResponses(
        ApiResponse(responseCode = "200"),
        ApiResponse(responseCode = "401", description = "Invalid Authentication", content = [Content()])
    )
    @DeleteMapping
    fun deleteCampaign(authentication: Authentication, @RequestParam campaignId: Long): Boolean {
        val auth0Id = getAuth0IdFromAuthentication(authentication)
        return campaignService.deleteCampaign(auth0Id, campaignId)
    }
}