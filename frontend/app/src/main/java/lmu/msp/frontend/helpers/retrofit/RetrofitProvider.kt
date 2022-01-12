package lmu.msp.frontend.helpers.retrofit

import android.content.Context
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import lmu.msp.frontend.Constants
import lmu.msp.frontend.api.PenAndPaperApiInterface
import lmu.msp.frontend.helpers.TokenManager
import lmu.msp.frontend.helpers.retrofit.interceptor.AuthInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * provides retrofit apis
 *
 */
class RetrofitProvider(context: Context) {

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptorAuth(context))
        .addInterceptor(interceptorLogging())
        .build()

    private lateinit var userApi: PenAndPaperApiInterface.UserApi
    private lateinit var campaignApi: PenAndPaperApiInterface.CampaignApi
    private lateinit var campaignMemberApi: PenAndPaperApiInterface.CampaignMemberApi
    private lateinit var inviteCampaignApi: PenAndPaperApiInterface.InviteCampaignApi

    private fun interceptorLogging() =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)

    private fun interceptorAuth(context: Context) = AuthInterceptor(TokenManager(context))

    private fun createRetrofitWithRxJava(okHttpClient: OkHttpClient) = Retrofit.Builder()
        .baseUrl(Constants.API_BASE_PATH)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    /**
     * with the "!::userApi.isInitialized" implementation
     * an api object only hast to be created if the class really needs it
     *
     */


    fun getUserApi(): PenAndPaperApiInterface.UserApi {
        if (!::userApi.isInitialized) {
            userApi = createRetrofitWithRxJava(okHttpClient)
                .create(PenAndPaperApiInterface.UserApi::class.java)
        }
        return userApi
    }
    fun getCampaignApi(): PenAndPaperApiInterface.CampaignApi {
        if (!::campaignApi.isInitialized) {
            campaignApi = createRetrofitWithRxJava(okHttpClient)
                .create(PenAndPaperApiInterface.CampaignApi::class.java)
        }
        return campaignApi
    }
    fun getCampaignMemberApi(): PenAndPaperApiInterface.CampaignMemberApi {
        if (!::campaignMemberApi.isInitialized) {
            campaignMemberApi = createRetrofitWithRxJava(okHttpClient)
                .create(PenAndPaperApiInterface.CampaignMemberApi::class.java)
        }
        return campaignMemberApi
    }
    fun getInviteCampaignApi(): PenAndPaperApiInterface.InviteCampaignApi {
        if (!::inviteCampaignApi.isInitialized) {
            inviteCampaignApi = createRetrofitWithRxJava(okHttpClient)
                .create(PenAndPaperApiInterface.InviteCampaignApi::class.java)
        }
        return inviteCampaignApi
    }
}