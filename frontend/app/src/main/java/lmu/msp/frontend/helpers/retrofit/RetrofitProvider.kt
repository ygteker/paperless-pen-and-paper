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
class RetrofitProvider {

    private lateinit var userApi: PenAndPaperApiInterface.UserApi

    /**
     * if not init yet generates ne userApi and returns it
     *
     * @param context
     * @return
     */
    fun getUserApi(context: Context): PenAndPaperApiInterface.UserApi {
        if (!::userApi.isInitialized) {
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(interceptorAuth(context))
                .addInterceptor(interceptorLogging())
                .build()

            userApi = createRetrofitWithRxJava(okHttpClient)
                .create(PenAndPaperApiInterface.UserApi::class.java)
        }
        return userApi
    }

    private fun interceptorLogging() =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)

    private fun interceptorAuth(context: Context) = AuthInterceptor(TokenManager(context))

    private fun createRetrofitWithRxJava(okHttpClient: OkHttpClient) = Retrofit.Builder()
        .baseUrl(Constants.API_BASE_PATH_DEMO)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()
}