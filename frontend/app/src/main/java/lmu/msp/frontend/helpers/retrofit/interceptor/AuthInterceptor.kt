package lmu.msp.frontend.helpers.retrofit.interceptor

import android.util.Log
import lmu.msp.frontend.helpers.TokenManager
import okhttp3.Interceptor
import okhttp3.Response

/**
 * intercepts retrofit http call
 * add a bearer token to every call (we need authentication in every call)
 *
 * @property tokenManager
 */
class AuthInterceptor(private val tokenManager: TokenManager) : Interceptor {

    companion object{
        private const val TAG = "AuthInterceptor"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        if (tokenManager.hasToken()) {
            builder.addHeader("Authorization", "Bearer ${tokenManager.load()}")
        } else {
            Log.d(TAG, "there is no token present. Authorization-Header will not be added")
        }

        return chain.proceed(builder.build())
    }
}