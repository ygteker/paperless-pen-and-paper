package lmu.msp.frontend.models.websocket

import com.google.gson.annotations.SerializedName

data class DrawMessage(
    @SerializedName("color")
    val color: Int,
    @SerializedName("currentX")
    val currentX: Float,
    @SerializedName("eventX")
    val eventX: Float,
    @SerializedName("currentY")
    val currentY: Float,
    @SerializedName("eventY")
    val eventY: Float
)