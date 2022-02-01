package lmu.msp.frontend.models.websocket

import com.google.gson.annotations.SerializedName

/**
 * messageType = CHAT_MESSAGE
 *
 * chat message of the websocket
 *
 * @property message
 * @property receiverId
 * @property senderId
 */
data class ChatMessage(
    @SerializedName("message")
    val message: String?,
    @SerializedName("receiverId")
    val receiverId: Int?,
    @SerializedName("senderId")
    val senderId: Int?
)