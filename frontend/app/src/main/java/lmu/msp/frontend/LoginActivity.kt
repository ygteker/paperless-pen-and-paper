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
    private lateinit var accessToken: String

    private lateinit var auth: AuthenticationAPIClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        account = Auth0(
            "fO2ATpY1mfFrutuO9HLpiiF80qQRSJiH",
            "dev-paperlesspenandpaper.eu.auth0.com"
        )

        auth = AuthenticationAPIClient(account)

        val loginButton = binding.loginButton
        val email = binding.email.text.toString()
        val password = binding.password.text.toString()
        val databaseConnection: String = ""

        loginButton.setOnClickListener {
            // Toast.makeText(this@LoginActivity, "Login", Toast.LENGTH_SHORT).show()

            // Login method
        //    login(email, password, databaseConnection)
            intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)

            // loginWithBrowser()

        }

        val registerButton = binding.registerButton

        registerButton.setOnClickListener {
            // Toast.makeText(this@LoginActivity, "Register", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun login(email: String, password: String, databaseConnection: String) {
        auth
            .login(email, password)
            .setConnection(databaseConnection)
            .start(object: Callback<Credentials, AuthenticationException> {
                override fun onFailure(exception: AuthenticationException) {
                    //TODO(Handle failure)
                }

                override fun onSuccess(credentials: Credentials) {
                    //TODO(redirect to HomeActivity or ProfileActivity)
                }
            })
    }

    private fun loginWithBrowser() {
        WebAuthProvider.login(account)
            .withScheme("android")
            .start(this, object: Callback<Credentials, AuthenticationException> {
                override fun onFailure(exception: AuthenticationException) {
                    //TODO(handle exception)
                }

                override fun onSuccess(credentials: Credentials) {
                    accessToken = credentials.accessToken
                    print(accessToken)
                }
            })
    }

    private fun showUserInformation(accessToken: String) {
        var client = AuthenticationAPIClient(account)

        // With the access token, call `userInfo` and get the profile from Auth0.
        client.userInfo(accessToken)
            .start(object : Callback<UserProfile, AuthenticationException> {
                override fun onFailure(exception: AuthenticationException) {
                    // Something went wrong!
                }

                override fun onSuccess(profile: UserProfile) {
                    // We have the user's profile!
                    val email = profile.email
                    val name = profile.name

                    print(email + name)
                }
            })
    }
}