package lmu.msp.frontend.models.websocket

import com.google.gson.annotations.SerializedName

class GroupMessage(
    @SerializedName("senderId")
    val senderId: Long,
    @SerializedName("message")
    val message: String
)