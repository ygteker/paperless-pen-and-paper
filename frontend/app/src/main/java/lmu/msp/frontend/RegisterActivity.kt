package lmu.msp.frontend

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.result.Credentials
import lmu.msp.frontend.databinding.ActivityLoginBinding
import lmu.msp.frontend.databinding.ActivityRegisterBinding

class RegisterActivity: AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var account: Auth0
    private lateinit var accessToken: String

    private lateinit var auth: AuthenticationAPIClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        account = Auth0(
            "fO2ATpY1mfFrutuO9HLpiiF80qQRSJiH",
            "dev-paperlesspenandpaper.eu.auth0.com"
        )

        auth = AuthenticationAPIClient(account)

        val registerButton = binding.registerButton
        val email = binding.email.text.toString()
        val password = binding.password.toString()
        val databaseConnection: String = ""
        val connection: String = ""

        registerButton.setOnClickListener {
            register(email, password, databaseConnection, connection)
        }


    }

    private fun register(email: String, password: String, databaseConnection: String, connection: String) {
        auth
            .signUp(email, password, databaseConnection, connection)
            .start(object: Callback<Credentials, AuthenticationException> {
                override fun onFailure(exception: AuthenticationException) {
                    //TODO(Handle failure)
                }

                override fun onSuccess(credentials: Credentials) {
                //TODO(change target activity to HomeActivity)
                    val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                    startActivity(intent)
                }
            })
    }
}