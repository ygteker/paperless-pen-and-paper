package lmu.msp.backend.socket.model

class ChatMessage(val senderId: Long, val receiverId: Long, val message: String)