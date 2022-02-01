package lmu.msp.frontend.helpers.websockets

import android.content.Context
import lmu.msp.frontend.Constants.Companion.WS_BASE_PATH
import lmu.msp.frontend.helpers.TokenManager
import lmu.msp.frontend.helpers.retrofit.interceptor.AuthInterceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okhttp3.logging.HttpLoggingInterceptor

/**
 * provides a websocket
 *
 * @param context needed to get the jwt token (for authentication)
 */
class WebSocketProvider(context: Context) {

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptorAuth(context))
        .addInterceptor(interceptorLogging())
        .cache(null)
        .build()

    private fun interceptorLogging() =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)

    private fun interceptorAuth(context: Context) = AuthInterceptor(TokenManager(context))

    fun start(campaignId: Long, webSocketListener: WebSocketListener): WebSocket {

        val request = Request.Builder().url(WS_BASE_PATH + campaignId).build()

        return okHttpClient.newWebSocket(request, webSocketListener)
    }

}