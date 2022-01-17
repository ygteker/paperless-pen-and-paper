package lmu.msp.backend.socket.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class BaseMessage(
    val messageType: MessageType,
    val chatMessage: List<ChatMessage>?,
    val drawMessage: List<DrawMessage>?
)