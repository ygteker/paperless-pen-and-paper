package lmu.msp.backend.service.implementation

import lmu.msp.backend.model.Campaign
import lmu.msp.backend.model.CampaignMember
import lmu.msp.backend.repository.CampaignRepository
import lmu.msp.backend.repository.MemberRepository
import lmu.msp.backend.service.ICampaignService
import lmu.msp.backend.service.IUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


/**
 * implementation of ICampaignService
 * contains the "business logic"
 *
 * @property campaignRepository
 * @property memberRepository
 * @property userService
 */
@Service
class CampaignService(
    @Autowired private val campaignRepository: CampaignRepository,
    @Autowired private val memberRepository: MemberRepository,
    @Autowired private val userService: IUserService,
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

    override fun isMemberOrOwner(auth0Id: String, campaignId: Long): Boolean {
        val campaign = campaignRepository.findByIdOrNull(campaignId) ?: return false
        return isMemberOrOwner(auth0Id, campaign)
    }

    override fun getMembers(auth0Id: String, campaignId: Long): List<CampaignMember>? {
        val campaign = campaignRepository.findByIdOrNull(campaignId) ?: return null
        return if (isMemberOrOwner(auth0Id, campaign)) {
            campaign.campaignMember
        } else {
            null
        }
    }

    override fun addMember(auth0Id: String, campaignId: Long, name: String): CampaignMember? {
        val campaign = campaignRepository.findByIdOrNull(campaignId) ?: return null
        if (isMemberOrOwner(auth0Id, campaign)) {
            return null
        }

        val user = userService.getUserByAuth0Id(auth0Id)

        val campaignMember = CampaignMember(campaign, user, name)
        campaign.campaignMember.add(campaignMember)
        user.campaignMember.add(campaignMember)

        return memberRepository.save(campaignMember)

    }

    @Transactional
    override fun removeMember(auth0Id: String, campaignId: Long, userIdToRemove: Long): List<CampaignMember>? {
        val campaign = campaignRepository.findByIdOrNull(campaignId) ?: return null

        if (!isMember(userIdToRemove, campaign) /*user must be a member of the campaign*/) {
            return null
        }

        val requestingUser = userService.getUserByAuth0Id(auth0Id)

        if (userIdToRemove != requestingUser.id /*user not removes itself*/ && campaign.owner.id != requestingUser.id /*owner not removes user*/) {
            return null
        }

        val campaignMember = memberRepository.findByCampaignIdAndUserId(campaignId, userIdToRemove) ?: return null

        campaignMember.user.campaignMember.remove(campaignMember)
        campaignMember.campaign.campaignMember.remove(campaignMember)

        memberRepository.deleteById(campaignMember.id)
        return campaignMember.campaign.campaignMember
    }

    override fun renameMember(auth0Id: String, campaignId: Long, newName: String): CampaignMember? {
        val campaignMember = memberRepository.findByCampaignIdAndUserAuth0Id(campaignId, auth0Id) ?: return null

        campaignMember.characterName = newName

        memberRepository.save(campaignMember)

        return campaignMember
    }


    private fun isOwner(userId: Long, campaign: Campaign): Boolean {
        return campaign.owner.id == userId
    }

    private fun isMember(userId: Long, campaign: Campaign): Boolean {
        return campaign.campaignMember.find { it.user.id == userId } != null
    }

    private fun isMemberOrOwner(userId: Long, campaign: Campaign): Boolean {
        return isOwner(userId, campaign) or isMember(userId, campaign)
    }

    private fun isOwner(auth0Id: String, campaign: Campaign): Boolean {
        return campaign.owner.auth0Id == auth0Id
    }

    private fun isMember(auth0Id: String, campaign: Campaign): Boolean {
        return campaign.campaignMember.find { it.user.auth0Id == auth0Id } != null
    }

    private fun isMemberOrOwner(auth0Id: String, campaign: Campaign): Boolean {
        return isOwner(auth0Id, campaign) or isMember(auth0Id, campaign)
    }
}