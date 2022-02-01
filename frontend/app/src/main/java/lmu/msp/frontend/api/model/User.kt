package lmu.msp.frontend.api.model


import com.google.gson.annotations.SerializedName

/**
 * data model for the user obj
 *
 * @property campaignMember
 * @property campaignOwner
 * @property id
 * @property receivedMails
 * @property sendMails
 */
data class User(
    @SerializedName("campaignMember")
    val campaignMember: List<CampaignMember>,
    @SerializedName("campaignOwner")
    val campaignOwner: List<Campaign>,
    @SerializedName("id")
    val id: Long,
    @SerializedName("receivedMails")
    val receivedMails: List<Long>,
    @SerializedName("sendMails")
    val sendMails: List<Long>
)