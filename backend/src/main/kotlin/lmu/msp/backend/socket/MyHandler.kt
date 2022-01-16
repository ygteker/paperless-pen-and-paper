package lmu.msp.backend.socket

import lmu.msp.backend.utility.getAuth0IdFromAttributes
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler


class MyHandler : TextWebSocketHandler() {
    public override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val auth0Id = getAuth0IdFromAttributes(session.attributes)
        session.sendMessage(TextMessage("Hello $auth0Id"))
    }
}