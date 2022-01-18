package lmu.msp.frontend.api.model

import com.google.gson.annotations.SerializedName

data class Message (

    @SerializedName("sender") var sender: Long? = null,
    @SerializedName("receiver") var receiver: Long? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("id") var id: Long? = null,
    @SerializedName("time") var time: String? = null
)