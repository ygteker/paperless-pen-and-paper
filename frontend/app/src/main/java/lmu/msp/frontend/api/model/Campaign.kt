package lmu.msp.frontend.api.model

import com.google.gson.annotations.SerializedName


data class Campaign (

  @SerializedName("owner"          ) var user           : User?                     = User(),
  @SerializedName("title"          ) var title          : String?                   = null,
  @SerializedName("campaignMember" ) var campaignMember : ArrayList<CampaignMember> = arrayListOf(),
  @SerializedName("id"             ) var id             : Long?                     = null

)