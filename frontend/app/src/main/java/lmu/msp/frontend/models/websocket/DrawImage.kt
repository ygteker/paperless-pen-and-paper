package lmu.msp.frontend.models.websocket

import com.google.gson.annotations.SerializedName

data class DrawImage(
    @SerializedName("color")
    val image: ByteArray = ByteArray(0)
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DrawImage

        if (!image.contentEquals(other.image)) return false

        return true
    }

    override fun hashCode(): Int {
        return image.contentHashCode()
    }
}