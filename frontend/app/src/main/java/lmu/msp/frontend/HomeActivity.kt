package lmu.msp.frontend

import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import lmu.msp.frontend.api.PenAndPaperApiInterface
import lmu.msp.frontend.api.model.User
import lmu.msp.frontend.databinding.ActivityHomeBinding
import lmu.msp.frontend.helpers.TokenManager
import lmu.msp.frontend.helpers.auth0.PAuthenticator
import lmu.msp.frontend.helpers.retrofit.RetrofitProvider
import lmu.msp.frontend.viewmodels.UserViewModel

/**
 * This is the home activity it handles its bottom navigation bar and fetches the logged in user
 * @author Valentin Scheibe
 */
class HomeActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "HomeActivity"
    }


    private lateinit var userApi: PenAndPaperApiInterface.UserApi
    private lateinit var binding: ActivityHomeBinding
    private lateinit var auth: PAuthenticator
    private val disposables = CompositeDisposable()


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

    /**
     * This loads the user data from the backend and calls updateNavController on success
     * @author LLewellyn Hochhauser & Valentin Scheibe
     */
    private fun loadUserData() {
        binding.pbHomeActivity.visibility = VISIBLE
        binding.navView.visibility = GONE
        binding.container.visibility = GONE

        disposables.add(userApi.getUser()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                updateNavController(it)
                loadingFinished()
            }
            .subscribe { user, error ->
                if (error == null) {

                } else {
                    val httpException: HttpException = error as HttpException
                    when (httpException.code()) {
                        404 -> Toast.makeText(this, "404 Not Found", Toast.LENGTH_SHORT).show()
                        401 -> Toast.makeText(this, "401 Unauthorized", Toast.LENGTH_SHORT).show()
                    }
                }
            })
    }

    /**
     * Saves the fetched user object into a viewModel
     * @param user the user object that gets saved to the viewModel
     */
    private fun updateNavController(user: User) {
        val viewModel: UserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        viewModel.setUser(user)
    }

    private fun loadingFinished() {
        binding.pbHomeActivity.visibility = GONE
        binding.navView.visibility = VISIBLE
        binding.container.visibility = VISIBLE
    }

    /**
     * Logs out the user
     */
    fun logout() {
        auth.logout()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

}