package lmu.msp.frontend.models.websocket

import com.google.gson.annotations.SerializedName

data class DrawMessage(
    @SerializedName("color")
    val color: Int?,
    @SerializedName("x")
    val x: Int?,
    @SerializedName("y")
    val y: Int?
)