package lmu.msp.frontend

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.provider.AuthProvider
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import com.auth0.android.result.UserProfile
import lmu.msp.frontend.databinding.ActivityLoginBinding
import java.util.*


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var account: Auth0

    private lateinit var auth: AuthenticationAPIClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        account = Auth0(
            getString(R.string.com_auth0_client_id),
            getString(R.string.com_auth0_domain)
        )

        auth = AuthenticationAPIClient(account)

        val loginButton = binding.loginButton

        loginButton.setOnClickListener {
            loginWithBrowser()
        }
    }

    private fun loginWithBrowser() {
        WebAuthProvider.login(account)
            .withScheme("android")
            .withScope("openid profile email")
            .start(this, object: Callback<Credentials, AuthenticationException> {
                override fun onFailure(error: AuthenticationException) {
                    error.printStackTrace()
                }

                override fun onSuccess(result: Credentials) {
                    val accessToken = result.accessToken
                    intent = Intent(this@LoginActivity, HomeActivity::class.java)
                    intent.putExtra("access_token", accessToken)
                    startActivity(intent)
                }
            })
    }
}