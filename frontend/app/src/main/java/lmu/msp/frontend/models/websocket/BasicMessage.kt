package lmu.msp.frontend.models.websocket

import com.google.gson.annotations.SerializedName

/**
 * basic message of the websocket
 *
 * @property messageType
 * @property chatMessage
 * @property drawMessage
 * @property groupMessage
 * @property drawImage
 */
data class BasicMessage(
    @SerializedName("messageType")
    val messageType: MessageType,
    @SerializedName("chatMessage")
    val chatMessage: List<ChatMessage>? = null,
    @SerializedName("drawMessage")
    val drawMessage: List<DrawMessage>? = null,
    @SerializedName("groupMessage")
    val groupMessage: List<GroupMessage>? = null,
    @SerializedName("drawImage")
    val drawImage: DrawImage? = null
)

