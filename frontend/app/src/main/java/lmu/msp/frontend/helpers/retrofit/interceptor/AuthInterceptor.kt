package lmu.msp.frontend.helpers.retrofit.interceptor

import lmu.msp.frontend.helpers.TokenManager
import okhttp3.Interceptor
import okhttp3.Response

/**
 * intercepts retrofit http call and tries to add a bearer token
 *
 * @property tokenManager
 */
class AuthInterceptor(private val tokenManager: TokenManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()

        tokenManager.load()?.let {
            builder.addHeader("Authorization", "Bearer $it")
        }

        return chain.proceed(builder.build())
    }
}