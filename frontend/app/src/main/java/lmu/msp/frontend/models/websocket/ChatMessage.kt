package lmu.msp.frontend.models.websocket

import com.google.gson.annotations.SerializedName

data class ChatMessage(
    @SerializedName("message")
    val message: String?,
    @SerializedName("receiverId")
    val receiverId: Int?,
    @SerializedName("senderId")
    val senderId: Int?
)