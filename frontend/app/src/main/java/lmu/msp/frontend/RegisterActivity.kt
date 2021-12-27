package lmu.msp.frontend

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

        registerButton.setOnClickListener {
            //TODO(handle register)
            register()
        }


    }

    private fun register() {
        auth
            .signUp("info@auth0.com", "a secret password", "Username-Password-Authentication")
            .start(object: Callback<Credentials, AuthenticationException> {
                override fun onFailure(exception: AuthenticationException) { }

                override fun onSuccess(credentials: Credentials) { }
            })
    }
}