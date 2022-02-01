package lmu.msp.backend.socket.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * base message of the websocket
 *
 * @property messageType
 * @property chatMessage
 * @property drawMessage
 * @property groupMessage
 * @property drawImage
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class BaseMessage(
    val messageType: MessageType,
    val chatMessage: List<ChatMessage>? = null,
    val drawMessage: List<DrawMessage>? = null,
    val groupMessage: List<GroupMessage>? = null,
    val drawImage: DrawImage? = null
)