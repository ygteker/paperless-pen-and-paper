package lmu.msp.backend.service

import lmu.msp.backend.model.Campaign
import lmu.msp.backend.model.CampaignMember

interface ICampaignService {

    /**
     * finds the campaign obj by its ID. The auth0Id must be a member or the owner of the campaign
     *
     * @param auth0Id must be a member or the owner of the campaign
     * @param campaignId the campaign
     * @return null if the auth0Id isn't a member or owner of the campaig or if campaign doesn't exist
     */
    fun getCampaign(auth0Id: String, campaignId: Long): Campaign?

    /**
     * create a new the campaign. If the user doesn't exist a new user will also be created
     *
     * @param auth0Id the user will be the owner
     * @param campaignName name of the campaign
     * @return
     */
    fun createCampaign(auth0Id: String, campaignName: String): Campaign

    /**
     * delete a campaign. The auth0Id must be the owner of the campaign
     *
     * @param auth0Id owner of the campaign
     * @param campaignId
     * @return true if deleted. false if auth0id isn't the owner or campaignId doesn't exist
     */
    fun deleteCampaign(auth0Id: String, campaignId: Long): Boolean

    /**
     * select all member of the campaign. the requesting user must be a member or owner
     *
     * @param auth0Id
     * @param campaignId
     * @return null if auth0Id is not a member or owner
     */
    fun getMembers(auth0Id: String,campaignId: Long): List<CampaignMember>?

    fun inviteMember(auth0Id: String, campaignId: Long, memberId: Long)

    fun acceptMember()

    fun removeMember()
}