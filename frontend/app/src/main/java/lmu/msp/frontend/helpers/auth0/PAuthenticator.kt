package lmu.msp.frontend.helpers.auth0

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.request.DefaultClient
import com.auth0.android.result.Credentials
import lmu.msp.frontend.HomeActivity
import lmu.msp.frontend.LoginActivity
import lmu.msp.frontend.R

class PAuthenticator(private val context: Context) {

    private val account: Auth0 by lazy {
        // -- REPLACE this credentials with your own Auth0 app credentials!
        val account = Auth0(
            context.getString(R.string.com_auth0_client_id),
            context.getString(R.string.com_auth0_domain)
        )
        // Only enable network traffic logging on production environments!
        account.networkingClient = DefaultClient(enableLogging = true)
        account
    }

    private val apiClient: AuthenticationAPIClient by lazy {
        AuthenticationAPIClient(account)
    }

    fun login() {
        WebAuthProvider.login(account)
            .withScheme(context.getString(R.string.com_auth0_scheme))
            .withAudience(context.getString(R.string.com_auth0_audience))
            .start(context, object : Callback<Credentials, AuthenticationException> {
                override fun onSuccess(result: Credentials) {
                    val intent = Intent(context, HomeActivity::class.java)
                    intent.putExtra("access_token", result.accessToken)
                    context.startActivity(intent)
                }

                override fun onFailure(error: AuthenticationException) {
                    Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show()
                }
            })
    }

    fun logout() {
        WebAuthProvider.logout(account)
            .withScheme(context.getString(R.string.com_auth0_scheme))
            .start(context, object : Callback<Void?, AuthenticationException> {
                override fun onSuccess(result: Void?) {
                    val intent = Intent(context, LoginActivity::class.java)
                    context.startActivity(intent)
                }

                override fun onFailure(error: AuthenticationException) {
                    Toast.makeText(context, error.message.toString(), Toast.LENGTH_LONG).show()
                }

            })
    }

}