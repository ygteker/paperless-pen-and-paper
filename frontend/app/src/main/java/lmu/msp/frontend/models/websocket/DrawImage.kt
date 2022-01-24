package lmu.msp.frontend.models.websocket

import com.google.gson.annotations.SerializedName
import java.util.*

data class DrawImage(
    @SerializedName("imageBase64")
    val imageBase64: String = ""

) {
    companion object {
        fun create(byteArray: ByteArray) = DrawImage(Base64.getEncoder().encodeToString(byteArray))
    }

    fun getByteArray(): ByteArray = Base64.getDecoder().decode(imageBase64)
}