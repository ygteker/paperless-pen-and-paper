package lmu.msp.backend.socket.model

/**
 * message type GROUP_MESSAGE
 * a special chatroom a chat message will be sent to everyone in this chat room
 *
 * @property senderId
 * @property message
 */
data class GroupMessage(val senderId: Long,val message:String)