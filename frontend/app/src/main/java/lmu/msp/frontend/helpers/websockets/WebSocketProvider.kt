package lmu.msp.frontend.helpers.websockets

import android.content.Context
import lmu.msp.frontend.Constants.Companion.WS_BASE_PATH
import lmu.msp.frontend.helpers.TokenManager
import lmu.msp.frontend.helpers.retrofit.interceptor.AuthInterceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor


class WebSocketProvider(context: Context) {

    private var rxWebSocket: IRxWebSocket? = null

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptorAuth(context))
        .addInterceptor(interceptorLogging())
        .build()

    private fun interceptorLogging() =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)

    private fun interceptorAuth(context: Context) = AuthInterceptor(TokenManager(context))

    fun start(campaignId: Long): IRxWebSocket {
        val request = Request.Builder().url(WS_BASE_PATH + campaignId).build()

        rxWebSocket?.close()
        rxWebSocket = RxWebSocket(okHttpClient, request)

        return rxWebSocket!!
    }

}