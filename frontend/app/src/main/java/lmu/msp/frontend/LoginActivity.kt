package lmu.msp.frontend

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.provider.AuthProvider
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.request.DefaultClient
import com.auth0.android.result.Credentials
import com.auth0.android.result.UserProfile
import com.google.android.material.snackbar.Snackbar
import lmu.msp.frontend.databinding.ActivityLoginBinding
import lmu.msp.frontend.helpers.auth0.PAuthenticator
import java.util.*


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: PAuthenticator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = PAuthenticator(this)

        val loginButton = binding.loginButton

        loginButton.setOnClickListener {
            auth.login()
        }
    }


}