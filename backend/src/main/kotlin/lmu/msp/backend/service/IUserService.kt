package lmu.msp.backend.service

import lmu.msp.backend.model.Campaign
import lmu.msp.backend.model.User

interface IUserService {

    /**
     * find a user buy its id
     *
     * @param id
     * @return null if the user doesn't exist in the DB
     */
    fun getUserById(id: Long): User?

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

    /**
     * update the profile picture of a user. returns if save was successful
     *
     * @param authO
     * @param byteArray
     * @return
     */
    fun updateProfileImage(authO: String, byteArray: ByteArray): Boolean

    /**
     * finds and return the profile picture of a user (of a specific id). If the user doesn't exist returns null.
     *
     * @param authO
     * @return
     */
    fun getProfileImage(authO: String, userId: Long): ByteArray?
}