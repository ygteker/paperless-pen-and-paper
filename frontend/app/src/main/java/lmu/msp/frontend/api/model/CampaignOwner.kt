package lmu.msp.frontend.api.model


import com.google.gson.annotations.SerializedName

data class CampaignOwner(
    @SerializedName("campaignMember")
    val campaignMember: List<Int>,
    @SerializedName("id")
    val id: Int,
    @SerializedName("owner")
    val owner: Int,
    @SerializedName("title")
    val title: String
)