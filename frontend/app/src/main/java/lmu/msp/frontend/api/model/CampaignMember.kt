package lmu.msp.frontend.api.model

import com.google.gson.annotations.SerializedName


data class CampaignMember (

  @SerializedName("campaign"      ) var campaign      : String? = null,
  @SerializedName("user"          ) var user          : String? = null,
  @SerializedName("characterName" ) var characterName : String? = null,
  @SerializedName("id"            ) var id            : Long?   = null

)