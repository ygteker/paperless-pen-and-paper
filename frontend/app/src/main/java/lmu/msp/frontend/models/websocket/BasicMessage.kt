package lmu.msp.frontend.models.websocket

import com.google.gson.annotations.SerializedName

data class BasicMessage(
    @SerializedName("messageType")
    val messageType: MessageType,
    @SerializedName("chatMessage")
    val chatMessage: List<ChatMessage>?,
    @SerializedName("drawMessage")
    val drawMessage: List<DrawMessage>?
)

