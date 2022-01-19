package lmu.msp.frontend.api.model


import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("campaignMember")
    val campaignMember: List<CampaignMember>,
    @SerializedName("campaignOwner")
    val campaignOwner: List<CampaignOwner>,
    @SerializedName("id")
    val id: Int,
    @SerializedName("receivedMails")
    val receivedMails: List<Int>,
    @SerializedName("sendMails")
    val sendMails: List<Int>
)