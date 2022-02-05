package lmu.msp.frontend.helpers.websockets

import lmu.msp.frontend.models.websocket.ChatMessage
import lmu.msp.frontend.models.websocket.DrawImage
import lmu.msp.frontend.models.websocket.DrawMessage
import lmu.msp.frontend.models.websocket.GroupMessage

/**
 * callback interface (e.g. websocket listener sends new data to the viewmodel)
 *
 */
interface WebSocketCallback {

    fun receiveChatMessages(chatMessages: List<ChatMessage>)

    fun receiveGroupMessages(groupMessage: List<GroupMessage>)

    fun receiveDrawMessages(drawMessages: List<DrawMessage>)

    fun receiveDrawMessageReset()

    fun receivedDrawImage(drawImage: DrawImage)
}