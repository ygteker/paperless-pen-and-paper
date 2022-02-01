package lmu.msp.backend.service.implementation

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import lmu.msp.backend.service.ICampaignService
import lmu.msp.backend.service.ISessionService
import lmu.msp.backend.socket.model.BaseMessage
import lmu.msp.backend.utility.getAuth0IdFromAttributes
import lmu.msp.backend.utility.getCampaignIdFromAttributes
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession


/**
 * implementation of ISessionManagerService and ISessionWorkerService
 * contains the "business logic"
 *
 * we use two services here to reduce the access the classes have which uses these interfaces
 *
 * @property campaignService
 */
@Service
class SessionManagerService(@Autowired private val campaignService: ICampaignService) :
    ISessionService.ISessionManagerService,
    ISessionService.ISessionWorkerService {
    private val sessionMap = HashMap<Long, HashMap<String, WebSocketSession>>()

    /**
     * only stores if the session attributes contains an auth0Id which is a owner or member of the campaignId
     *
     * @param session
     * @return
     */
    override fun add(session: WebSocketSession): Boolean {
        val auth0Id = getAuth0IdFromAttributes(session.attributes)
        val campaignId = getCampaignIdFromAttributes(session.attributes)

        if (!campaignService.isMemberOrOwner(auth0Id, campaignId)) {
            return false
        }

        if (!sessionMap.containsKey(campaignId)) {
            sessionMap[campaignId] = HashMap()
        }
        if (null != sessionMap[campaignId]!![auth0Id]) {
            sessionMap[campaignId]!![auth0Id]!!.close(CloseStatus.SESSION_NOT_RELIABLE)
        }
        sessionMap[campaignId]!![auth0Id] = session
        return true
    }

    override fun remove(session: WebSocketSession): Boolean {
        val auth0Id = getAuth0IdFromAttributes(session.attributes)
        val campaignId = getCampaignIdFromAttributes(session.attributes)

        //since remove is only allowed to be called from a "logged in" session
        //sessionMap always contains the key
        //
        if (!sessionMap.containsKey(campaignId)) {
            return false
        }

        if (session == sessionMap[campaignId]!![auth0Id]) {
            sessionMap[campaignId]!!.remove(auth0Id)
            return true
        }
        return false
    }

    override fun sendTo(message: BaseMessage, campaignId: Long, vararg auth0Ids: String) {
        sessionMap[campaignId]?.let { map ->
            val jsonString = jacksonObjectMapper().writeValueAsString(message)
            val textMessage = TextMessage(jsonString)

            if (auth0Ids.isEmpty()) {
                map.values.forEach { it.sendMessage(textMessage) }
            } else {
                auth0Ids.forEach {
                    map[it]?.sendMessage(textMessage)
                }
            }
        }
    }

    override fun sendToFiltered(message: BaseMessage, campaignId: Long, vararg auth0IdsToIgnore: String) {
        sessionMap[campaignId]?.let { map ->
            val jsonString = jacksonObjectMapper().writeValueAsString(message)
            val textMessage = TextMessage(jsonString)
            map.keys.filter { !auth0IdsToIgnore.contains(it) }.forEach { map[it]!!.sendMessage(textMessage) }
        }

    }

    override fun getActive(campaignId: Long): Collection<String> {
        return sessionMap[campaignId]?.keys ?: emptyList()
    }
}