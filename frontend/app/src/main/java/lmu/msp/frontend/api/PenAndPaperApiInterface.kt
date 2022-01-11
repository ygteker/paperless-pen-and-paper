package lmu.msp.frontend.api

import io.reactivex.Single
import lmu.msp.frontend.Constants.Companion.PATH_HELLO_WORLD_AUTH
import lmu.msp.frontend.Constants.Companion.PATH_USER
import retrofit2.http.GET

interface PenAndPaperApiInterface {
    interface UserApi {
        @GET(PATH_USER)
        fun getUser(): Single<String>

        @GET(PATH_HELLO_WORLD_AUTH)
        fun getAuthenticatedHelloWorld(): Single<String>
    }
}