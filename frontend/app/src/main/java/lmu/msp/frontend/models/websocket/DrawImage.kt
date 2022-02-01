package lmu.msp.frontend.models.websocket

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * messageType = DRAW_IMAGE
 *
 * stores a base64 encoded image
 *
 * @property imageBase64
 */
data class DrawImage(
    @SerializedName("imageBase64")
    val imageBase64: String = ""

) {
    companion object {
        fun create(byteArray: ByteArray) = DrawImage(Base64.getEncoder().encodeToString(byteArray))
    }

    fun getByteArray(): ByteArray = Base64.getDecoder().decode(imageBase64)
}