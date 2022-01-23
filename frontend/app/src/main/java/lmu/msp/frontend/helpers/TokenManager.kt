package lmu.msp.frontend.helpers

import android.content.Context
import android.util.Log
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.storage.CredentialsManagerException
import com.auth0.android.authentication.storage.SecureCredentialsManager
import com.auth0.android.authentication.storage.SharedPreferencesStorage
import com.auth0.android.callback.Callback
import com.auth0.android.result.Credentials


class TokenManager(context: Context) {

    companion object {
        private const val TAG = "TokenManager"
    }

    private var token: String = ""

    private val credentialsManager: SecureCredentialsManager

    init {
        val auth0 = Auth0(context)
        val apiClient = AuthenticationAPIClient(auth0)
        credentialsManager =
            SecureCredentialsManager(context, apiClient, SharedPreferencesStorage(context))

        credentialsManager.getCredentials(object :
            Callback<Credentials, CredentialsManagerException> {
            override fun onSuccess(result: Credentials) {
                Log.d(TAG, "loaded token from credentials manager ${result.accessToken}")
                token = result.accessToken
            }

            override fun onFailure(error: CredentialsManagerException) {
                Log.d(TAG, "wasn't able to load token from credentials manager")
                token = ""
            }
        })
    }

    fun save(credentials: Credentials) {
        Log.d(TAG, "save new credentials")
        credentialsManager.saveCredentials(credentials)
        token = credentials.accessToken
    }

    fun hasToken(): Boolean {
        Log.d(TAG, "check if auth token is valid")
        return token.isNotEmpty() && credentialsManager.hasValidCredentials()
    }

    fun load(): String {
        Log.d(TAG, "load auth token")
        return if (hasToken()) {
            token
        } else {
            ""
        }
    }

    fun deleteToken() {
        Log.d(TAG, "delete auth token")
        credentialsManager.clearCredentials()
    }
}