package lmu.msp.backend.service

import lmu.msp.backend.model.Campaign
import lmu.msp.backend.model.User

interface IUserService {
    /**
     * Loads the user from the database.
     * If the user doesn't exist creates a new one and returns the created one.
     *
     * @param authO the user id from auth0. It can be obtained from the jwt authentication token
     * @return User obj from the DB
     *
     */
    fun getUserByAuth0Id(authO: String): User

    /**
     * Removes the campaign from the user (owner or member). Returns the new user obj.
     * If it's the owner the campaign will be deleted
     *
     * @param user
     * @param campaign
     * @return
     */
    fun removeCampaignFromUser(user: User, campaign: Campaign): User

}