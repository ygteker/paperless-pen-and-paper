package lmu.msp.frontend.api

import io.reactivex.Maybe
import io.reactivex.Single
import lmu.msp.frontend.Constants.Companion.PARAMETER_CAMPAIGN_ID
import lmu.msp.frontend.Constants.Companion.PARAMETER_NAME
import lmu.msp.frontend.Constants.Companion.PARAMETER_USER_ID_TO_REMOVE
import lmu.msp.frontend.Constants.Companion.PATH_CAMPAIGN
import lmu.msp.frontend.Constants.Companion.PATH_MAIL
import lmu.msp.frontend.Constants.Companion.PATH_MEMBER
import lmu.msp.frontend.Constants.Companion.PATH_MEMBER_INVITE_ACCEPT
import lmu.msp.frontend.Constants.Companion.PATH_USER
import lmu.msp.frontend.api.model.Campaign
import lmu.msp.frontend.api.model.CampaignMember
import lmu.msp.frontend.api.model.Message
import lmu.msp.frontend.api.model.User
import lmu.msp.frontend.models.MessageModel
import okhttp3.RequestBody
import retrofit2.http.*

interface PenAndPaperApiInterface {

    interface UserApi {

        @GET(PATH_USER)
        fun getUser(): Single<User>
    }

    interface CampaignApi {

        @GET(PATH_CAMPAIGN)
        fun getCampaign(@Query(PARAMETER_CAMPAIGN_ID) campaignId: Long): Single<Campaign>

        /**
         * to create a request body you can simply write "test-string".toRequestBody() or string.toRequestBody()
         * this way is needed, so that the string is not converted to a json string
         *
         * @param campaignName
         * @return
         */
        @POST(PATH_CAMPAIGN)
        fun createCampaign(@Body campaignName: RequestBody): Single<Campaign>

        @DELETE(PATH_CAMPAIGN)
        fun deleteCampaign(@Query(PARAMETER_CAMPAIGN_ID) campaignId: Long): Single<Boolean>

    }

    interface CampaignMemberApi {
        @GET(PATH_MEMBER)
        fun getMembers(@Query(PARAMETER_CAMPAIGN_ID) campaignId: Long): Single<List<CampaignMember>>

        @PUT(PATH_MEMBER)
        fun updateMember(
            @Query(PARAMETER_CAMPAIGN_ID) campaignId: Long,
            @Query(PARAMETER_NAME) name: String
        ): Single<List<CampaignMember>>

        @DELETE(PATH_MEMBER)
        fun removeMember(
            @Query(PARAMETER_CAMPAIGN_ID) campaignId: Long,
            @Query(PARAMETER_USER_ID_TO_REMOVE) userToRemoveId: Long
        ): Single<List<CampaignMember>>

    }

    interface InviteCampaignApi {

        @POST(PATH_MEMBER_INVITE_ACCEPT)
        fun acceptInvite(
            @Query(PARAMETER_CAMPAIGN_ID) campaignId: Long,
            @Query(PARAMETER_NAME) name: String
        ): Maybe<Campaign>
    }

    interface MessageApi {
        @POST(PATH_MAIL)
        @Headers("Content-Type: text/plain")
        fun sendMessage(
            @Query("receiverId") receiverId: Long,
            @Body message: RequestBody
        ): Maybe<Message>

        @DELETE(PATH_MAIL)
        fun deleteMessage(
            @Query("mailId") emailId: Int
        ): Maybe<Message>

        @GET(PATH_MAIL)
        fun getMessages(): Maybe<Message>
    }
}