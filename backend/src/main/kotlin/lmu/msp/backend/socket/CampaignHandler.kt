package lmu.msp.backend.socket

import com.fasterxml.jackson.databind.ObjectMapper
import lmu.msp.backend.socket.model.BaseMessage
import lmu.msp.backend.utility.getAuth0IdFromAttributes
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler


class CampaignHandler : TextWebSocketHandler() {
    public override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val auth0Id = getAuth0IdFromAttributes(session.attributes)
        val jsonString = message.payload

        session.sendMessage(TextMessage("Hello $auth0Id"))

        println(message.payload)
        val baseMessage = ObjectMapper().readValue(message.payload, BaseMessage::class.java)
    }

    @Throws(Exception::class)
    override fun afterConnectionEstablished(session: WebSocketSession) {
        super.afterConnectionEstablished(session)

    }

    @Throws(Exception::class)
    override fun handleTransportError(session: WebSocketSession, exception: Throwable) {
        super.handleTransportError(session, exception)
    }

    @Throws(Exception::class)
    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        super.afterConnectionClosed(session, status)
    }
}