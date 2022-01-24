package lmu.msp.frontend.helpers.websockets

import lmu.msp.frontend.models.websocket.ChatMessage
import lmu.msp.frontend.models.websocket.DrawImage
import lmu.msp.frontend.models.websocket.DrawMessage

interface WebSocketCallback {

    fun receiveChatMessages(chatMessages: List<ChatMessage>)

    fun receiveDrawMessages(drawMessages: List<DrawMessage>)

    fun receiveDrawMessageReset()

    fun receivedDrawImage(drawImage: DrawImage)
}