package lmu.msp.backend.service

import lmu.msp.backend.model.Campaign
import lmu.msp.backend.model.CampaignMember

interface ICampaignService {

    /**
     * finds the campaign obj by its ID. The auth0Id must be a member or the owner of the campaign
     *
     * @param auth0Id must be a member or the owner of the campaign
     * @param campaignId the campaign
     * @return null if the auth0Id isn't a member or owner of the campaign or if campaign doesn't exist
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
     * checks if the auth0Id is a member or an owner in the campaign
     *
     * @param auth0Id
     * @param campaignId
     * @return
     */
    fun isMemberOrOwner(auth0Id: String, campaignId: Long): Boolean

    /**
     * select all member of the campaign. the requesting user must be a member or owner
     *
     * @param auth0Id
     * @param campaignId
     * @return null if auth0Id is not a member or owner
     */
    fun getMembers(auth0Id: String, campaignId: Long): List<CampaignMember>?

    /**
     * add a member to a campaign. The member will only be added if the member isn't already an owner or member of the campaign
     * TODO for now there is no restriction who can join a campaign (no invite system yet)
     *
     * @param auth0Id
     * @param campaignId
     * @param name
     * @return null if campaign doesn't exist otherwise new campaign member obj
     */
    fun addMember(auth0Id: String, campaignId: Long, name: String): CampaignMember?

    /**
     * remove a member from a campaign. The auth0Id user must be the campaign owner or the user to remove (user can remove itself).
     *
     * @param auth0Id
     * @param campaignId
     * @param userIdToRemove
     * @return null if campaign doesn't exist, userToRemove not a member of the campaign, auth0Id isn't the campaign owner neither the userToRemove, otherwise return updated member list obj
     */
    fun removeMember(auth0Id: String, campaignId: Long, userIdToRemove: Long): List<CampaignMember>?

    /**
     * update the name of a campaign member
     *
     * @param auth0Id
     * @param campaignId
     * @param newName
     * @return null if auth0Id not a member of the campaign or campaign doesn't exist
     */
    fun renameMember(auth0Id: String, campaignId: Long, newName: String): CampaignMember?
}