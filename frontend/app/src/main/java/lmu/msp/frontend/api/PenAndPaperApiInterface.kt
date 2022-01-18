package lmu.msp.frontend.api

import io.reactivex.Maybe
import io.reactivex.Single
import lmu.msp.frontend.Constants.Companion.PATH_CAMPAIGN
import lmu.msp.frontend.Constants.Companion.PATH_MAIL
import lmu.msp.frontend.Constants.Companion.PATH_MEMBER
import lmu.msp.frontend.Constants.Companion.PATH_MEMBER_INVITE_ACCEPT
import lmu.msp.frontend.Constants.Companion.PATH_USER
import lmu.msp.frontend.api.model.Campaign
import lmu.msp.frontend.api.model.CampaignMember
import lmu.msp.frontend.api.model.Message
import lmu.msp.frontend.api.model.User
import retrofit2.http.*

interface PenAndPaperApiInterface {
    /**
     * TODO
     * check what calls can return null
     * these calls must have a maybe
     * all other calls can have a single
     *
     */

    interface UserApi {

        @GET(PATH_USER)
        fun getUser(): Single<User>
    }

    interface CampaignApi {

        @GET(PATH_CAMPAIGN)
        fun getCampaign(): Maybe<Campaign>

        @POST(PATH_CAMPAIGN)
        fun createCampaign(@Body campaignName: String): Maybe<Campaign>

        @DELETE(PATH_CAMPAIGN)
        fun deleteCampaign(@Query("campaignId") campaignId: Long): Single<Boolean>

    }

    interface CampaignMemberApi {
        @GET(PATH_MEMBER)
        fun getMembers(): Maybe<List<CampaignMember>>

        @PUT(PATH_MEMBER)
        fun updateMember(
            @Query("campaignId ") campaignId: Long,
            @Query("name") name: String
        ): Maybe<CampaignMember>

        @DELETE(PATH_MEMBER)
        fun removeMember(
            @Query("campaignId ") campaignId: Long,
            @Query("userToRemoveId") userToRemoveId: Long
        ): Maybe<Campaign>

    }

    interface InviteCampaignApi {

        @POST(PATH_MEMBER_INVITE_ACCEPT)
        fun acceptInvite(
            @Query("campaignId ") campaignId: Long,
            @Query("name") name: String
        ): Maybe<Campaign>
    }

    interface MessageApi {
        @POST(PATH_MAIL)
        fun sendMessage(
            @Query("receiverId") receiverId: Long,
            @Body message: String
        ): Maybe<Message>
    }
}