package lmu.msp.frontend.api.model

import com.google.gson.annotations.SerializedName


data class User (

  @SerializedName("campaignOwner"  ) var campaignOwner  : ArrayList<Campaign>       = arrayListOf(),
  @SerializedName("campaignMember" ) var campaignMember : ArrayList<CampaignMember> = arrayListOf(),
  @SerializedName("id"             ) var id             : Long?                     = null

)