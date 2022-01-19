package lmu.msp.frontend.api.model


import com.google.gson.annotations.SerializedName

data class Campaign(
    @SerializedName("campaignMember")
    val campaignMember: List<Long>,
    @SerializedName("id")
    val id: Long,
    @SerializedName("owner")
    val owner: Long,
    @SerializedName("title")
    val title: String
)