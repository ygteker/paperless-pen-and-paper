package lmu.msp.frontend.api.model


import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("campaignMember")
    val campaignMember: List<CampaignMember>,
    @SerializedName("campaignOwner")
    val campaignOwner: List<CampaignOwner>,
    @SerializedName("id")
    val id: Long,
    @SerializedName("receivedMails")
    val receivedMails: List<Long>,
    @SerializedName("sendMails")
    val sendMails: List<Long>
)