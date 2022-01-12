package lmu.msp.frontend

import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import lmu.msp.frontend.api.PenAndPaperApiInterface
import lmu.msp.frontend.api.model.User
import lmu.msp.frontend.databinding.ActivityHomeBinding
import lmu.msp.frontend.helpers.TokenManager
import lmu.msp.frontend.helpers.auth0.PAuthenticator
import lmu.msp.frontend.helpers.retrofit.RetrofitProvider
import lmu.msp.frontend.viewmodels.UserViewModel


class HomeActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "HomeActivity"
    }


    private lateinit var userApi: PenAndPaperApiInterface.UserApi


    private lateinit var binding: ActivityHomeBinding

    private lateinit var auth: PAuthenticator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = PAuthenticator(this, TokenManager(this))

        userApi = RetrofitProvider(applicationContext).getUserApi()

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_home)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_campaign, R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        loadUserData()
    }

    private fun loadUserData() {
        binding.pbHomeActivity.visibility = VISIBLE
        binding.navView.visibility = GONE
        binding.container.visibility = GONE

        userApi.getUser()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                Log.e(TAG, "error ${it.message}")
                //TODO ERROR HANDLING
            }
            .doOnSuccess {
                updateNavController(it)
                loadingFinished()
            }
            .subscribe()
    }

    private fun updateNavController(user: User) {
        val viewModel: UserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        viewModel.setUser(user)
    }

    private fun loadingFinished() {
        binding.pbHomeActivity.visibility = GONE
        binding.navView.visibility = VISIBLE
        binding.container.visibility = VISIBLE

    }

    fun logout() {
        auth.logout()
    }
}