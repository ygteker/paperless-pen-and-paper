package lmu.msp.frontend.api

import io.reactivex.Maybe
import io.reactivex.Single
import lmu.msp.frontend.Constants.Companion.PATH_CAMPAIGN
import lmu.msp.frontend.Constants.Companion.PATH_MEMBER
import lmu.msp.frontend.Constants.Companion.PATH_MEMBER_INVITE_ACCEPT
import lmu.msp.frontend.Constants.Companion.PATH_USER
import lmu.msp.frontend.api.model.Campaign
import lmu.msp.frontend.api.model.CampaignMember
import lmu.msp.frontend.api.model.User
import retrofit2.http.*

interface PenAndPaperApiInterface {

    interface UserApi {

        @GET(PATH_USER)
        fun getUser(): Single<User>
    }

    interface CampaignApi {

        @GET(PATH_CAMPAIGN)
        fun getCampaign(@Query("campaignId") campaignId: Long): Single<Campaign>

        @POST(PATH_CAMPAIGN)
        fun createCampaign(@Body campaignName: String): Single<Campaign>

        @DELETE(PATH_CAMPAIGN)
        fun deleteCampaign(@Query("campaignId") campaignId: Long): Single<Boolean>

    }

    interface CampaignMemberApi {
        @GET(PATH_MEMBER)
        fun getMembers(): Single<List<CampaignMember>>

        @PUT(PATH_MEMBER)
        fun updateMember(
            @Query("campaignId ") campaignId: Long,
            @Query("name") name: String
        ): Single<List<CampaignMember>>

        @DELETE(PATH_MEMBER)
        fun removeMember(
            @Query("campaignId ") campaignId: Long,
            @Query("userToRemoveId") userToRemoveId: Long
        ): Single<List<CampaignMember>>

    }

    interface InviteCampaignApi {

        @POST(PATH_MEMBER_INVITE_ACCEPT)
        fun acceptInvite(
            @Query("campaignId ") campaignId: Long,
            @Query("name") name: String
        ): Maybe<Campaign>
    }
}