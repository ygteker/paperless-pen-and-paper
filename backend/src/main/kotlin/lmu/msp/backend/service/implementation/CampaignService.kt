package lmu.msp.backend.service.implementation

import lmu.msp.backend.model.Campaign
import lmu.msp.backend.model.CampaignMember
import lmu.msp.backend.repository.CampaignRepository
import lmu.msp.backend.service.ICampaignService
import lmu.msp.backend.service.IUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class CampaignService(
    @Autowired private val campaignRepository: CampaignRepository,
    @Autowired private val userService: IUserService
) : ICampaignService {

    override fun getCampaign(auth0Id: String, campaignId: Long): Campaign? {
        val campaign = campaignRepository.findByIdOrNull(campaignId) ?: return null
        return if (isMemberOrOwner(auth0Id, campaign)) {
            campaign
        } else {
            null
        }
    }

    override fun createCampaign(auth0Id: String, campaignName: String): Campaign {
        val user = userService.getUserByAuth0Id(auth0Id)
        return campaignRepository.save(Campaign(user, campaignName))
    }

    override fun deleteCampaign(auth0Id: String, campaignId: Long): Boolean {
        val campaign = campaignRepository.findByIdOrNull(campaignId) ?: return false
        if (!isOwner(auth0Id, campaign)) {
            return false
        }
        userService.removeCampaignFromUser(campaign.owner, campaign)
        return true
    }

    override fun getMembers(auth0Id: String, campaignId: Long): List<CampaignMember>? {
        val campaign = campaignRepository.findByIdOrNull(campaignId) ?: return null
        return if (isMemberOrOwner(auth0Id, campaign)) {
            campaign.campaignMembers
        } else {
            null
        }
    }

    override fun inviteMember(auth0Id: String, campaignId: Long, memberId: Long) {
        TODO("Not yet implemented")
    }

    override fun acceptMember() {
        TODO("Not yet implemented")
    }

    override fun removeMember() {
        TODO("Not yet implemented")
    }


    private fun isOwner(userId: Long, campaign: Campaign): Boolean {
        return campaign.owner.id == userId
    }

    private fun isMember(userId: Long, campaign: Campaign): Boolean {
        return campaign.campaignMembers.find { it.user.id == userId } != null
    }

    private fun isMemberOrOwner(userId: Long, campaign: Campaign): Boolean {
        return isOwner(userId, campaign) or isMember(userId, campaign)
    }

    private fun isOwner(auth0Id: String, campaign: Campaign): Boolean {
        return campaign.owner.auth0Id == auth0Id
    }

    private fun isMember(auth0Id: String, campaign: Campaign): Boolean {
        return campaign.campaignMembers.find { it.user.auth0Id == auth0Id } != null
    }

    private fun isMemberOrOwner(auth0Id: String, campaign: Campaign): Boolean {
        return isOwner(auth0Id, campaign) or isMember(auth0Id, campaign)
    }
}