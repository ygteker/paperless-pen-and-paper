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
        /**
         * convert image data to a DrawImage message
         * the byteArray will be base64 encoded as a string
         *
         * @param byteArray image data
         * @return
         */
        fun create(byteArray: ByteArray) = DrawImage(Base64.getEncoder().encodeToString(byteArray))
    }

    fun getByteArray(): ByteArray = Base64.getDecoder().decode(imageBase64)
}