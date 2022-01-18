package lmu.msp.backend.service

import lmu.msp.backend.socket.model.BaseMessage

interface ISessionCampaignService : ISessionService {

    fun handleMessage(auth0Id: String, campaignId: Long, baseMessage: BaseMessage)

    fun connected(auth0Id: String, campaignId: Long)

    fun disconnected(auth0Id: String, campaignId: Long)
}