package lmu.msp.frontend.models.websocket

import com.google.gson.annotations.SerializedName

/**
 * messageType = GROUP_MESSAGE
 *
 * @property senderId
 * @property message
 */
class GroupMessage(
    @SerializedName("senderId")
    val senderId: Long,
    @SerializedName("message")
    val message: String
)