package lmu.msp.backend.socket

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import lmu.msp.backend.service.ISessionCampaignService
import lmu.msp.backend.service.ISessionService
import lmu.msp.backend.socket.model.BaseMessage
import lmu.msp.backend.utility.getAuth0IdFromAttributes
import lmu.msp.backend.utility.getCampaignIdFromAttributes
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

/**
 * implementation of the websocket handler
 * this class collect all need information for the Websocket Services
 *
 */
class CampaignHandler() : TextWebSocketHandler() {
    @Value("\${lmu.msp.backend.maxTextMessageBytes}")
    private val maxTextMessageSize: Int? = null

    @Autowired
    private lateinit var sessionService: ISessionService.ISessionManagerService

    @Autowired
    private lateinit var sessionCampaignService: ISessionCampaignService

    private val objectMapper = jacksonObjectMapper()


    @Throws(Exception::class)
    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        try {
            val data = objectMapper.readValue<BaseMessage>(message.payload)
            val auth0Id = getAuth0IdFromAttributes(session.attributes)
            val campaignId = getCampaignIdFromAttributes(session.attributes)
            sessionCampaignService.handleMessage(auth0Id, campaignId, data)
        } catch (exception: JsonParseException) {
            session.close(CloseStatus.BAD_DATA)
        }
    }

    @Throws(Exception::class)
    override fun afterConnectionEstablished(session: WebSocketSession) {
        super.afterConnectionEstablished(session)
        println("afterConnectionEstablished")
        val added = sessionService.add(session)
        if (!added) {
            //The interceptor also checks if a user is member or owner of the campaign (similar to SessionService).
            //In this regard is a double check.
            //But we don't know the implementation details of ISessionService here. We don't know if there may be another
            //reason to not accept the session. Therefore, we can't say that the session will always be added
            session.close(CloseStatus.POLICY_VIOLATION)
        }
        //increase text message size (in Bytes) from config file. If nothing is found in the config use 1MB
        session.textMessageSizeLimit = maxTextMessageSize ?: (1000000)

        val auth0Id = getAuth0IdFromAttributes(session.attributes)
        val campaignId = getCampaignIdFromAttributes(session.attributes)

        sessionCampaignService.connected(auth0Id, campaignId)
    }

    @Throws(Exception::class)
    override fun handleTransportError(session: WebSocketSession, exception: Throwable) {
        super.handleTransportError(session, exception)
        println("handleTransportError ${exception.message}")
    }

    @Throws(Exception::class)
    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        super.afterConnectionClosed(session, status)
        println("afterConnectionClosed ${status.reason}")
        sessionService.remove(session)

        val auth0Id = getAuth0IdFromAttributes(session.attributes)
        val campaignId = getCampaignIdFromAttributes(session.attributes)
        sessionCampaignService.disconnected(auth0Id, campaignId)
    }
}
