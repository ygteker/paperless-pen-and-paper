package lmu.msp.frontend.helpers.auth0

import lmu.msp.frontend.models.Message
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.*

interface ApiInterface {

    @GET("/api/v1/user/mail/")
    fun getMessages(@Header("Authorization") authorization: String): Call<List<Message>>

    @DELETE("/api/v1/user/mail/")
    fun deleteMessage(@Header("Authorization") authorization: String, @Query("mailId") emailId: Int): Call<String>

    companion object{
        var BASE_URL = "https://msp-ws2122-5.mobile.ifi.lmu.de"

        fun create(): ApiInterface {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(ApiInterface::class.java)
        }

    }
}