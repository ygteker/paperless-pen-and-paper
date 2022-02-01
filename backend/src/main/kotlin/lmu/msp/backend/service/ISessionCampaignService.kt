package lmu.msp.backend.service

import lmu.msp.backend.socket.model.BaseMessage

/**
 * service to handle incoming messages from a websocket
 *
 */
interface ISessionCampaignService : ISessionService {

    /**
     * analyse baseMessage
     * depending on that type handle the message
     *
     * @param auth0Id
     * @param campaignId
     * @param baseMessage
     */
    fun handleMessage(auth0Id: String, campaignId: Long, baseMessage: BaseMessage)

    /**
     * user connected to the websocket
     * handle this event as needed (e.g. send all current session data to the new user)
     *
     * @param auth0Id
     * @param campaignId
     */
    fun connected(auth0Id: String, campaignId: Long)

    /**
     * user disconnected from the websocket
     * handle this event as needed (e.g. send a notification to everyone else that user xy disconnected)
     *
     * @param auth0Id
     * @param campaignId
     */
    fun disconnected(auth0Id: String, campaignId: Long)
}