package lmu.msp.backend.socket.model


data class ChatMessage(val senderId: Long, val receiverId: Long, val message: String)