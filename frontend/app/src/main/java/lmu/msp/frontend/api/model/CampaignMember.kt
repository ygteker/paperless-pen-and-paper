package lmu.msp.frontend.api.model


import com.google.gson.annotations.SerializedName

data class CampaignMember(
    @SerializedName("campaign")
    val campaign: Campaign,
    @SerializedName("characterName")
    val characterName: String,
    @SerializedName("id")
    val id: Long,
    @SerializedName("user")
    val user: Long
)