package lmu.msp.frontend.models.websocket

import com.google.gson.annotations.SerializedName

/**
 * messageType = DRAW_PATH
 *
 * @property color
 * @property currentX
 * @property eventX
 * @property currentY
 * @property eventY
 */
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