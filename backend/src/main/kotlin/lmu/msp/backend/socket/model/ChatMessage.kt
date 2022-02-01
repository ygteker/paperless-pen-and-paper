package lmu.msp.backend.socket.model


/**
 * message type CHAT_MESSAGE
 * this message is used for direct chat message
 *
 * @property senderId
 * @property receiverId
 * @property message
 */
data class ChatMessage(val senderId: Long, val receiverId: Long, val message: String)