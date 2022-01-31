package lmu.msp.frontend.ui.campaign


import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import lmu.msp.frontend.R
import lmu.msp.frontend.api.PenAndPaperApiInterface
import lmu.msp.frontend.api.model.CampaignMember
import lmu.msp.frontend.api.model.User
import lmu.msp.frontend.databinding.ActivityCampaignBinding
import lmu.msp.frontend.helpers.TokenManager
import lmu.msp.frontend.helpers.auth0.PAuthenticator
import lmu.msp.frontend.helpers.retrofit.RetrofitProvider
import lmu.msp.frontend.ui.home.HomeFragment
import lmu.msp.frontend.viewmodels.WebSocketDataViewModel

/**
 * @author Valentin Scheibe & Kenny-Minh Nguyen
 */

class CampaignActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCampaignBinding

    private lateinit var userApi: PenAndPaperApiInterface.UserApi
    private lateinit var campaignMemberApi: PenAndPaperApiInterface.CampaignMemberApi
    private lateinit var auth: PAuthenticator

    private lateinit var user: User
    private var campaignMembersFromApi: List<CampaignMember> = emptyList()
    private var campaignId: Long = 0
    private val disposables = CompositeDisposable()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCampaignBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment, ToolsFragment(), "tools")
            commit()
        }

        auth = PAuthenticator(this, TokenManager(this))
        userApi = RetrofitProvider(applicationContext).getUserApi()
        campaignMemberApi = RetrofitProvider(applicationContext).getCampaignMemberApi()
        fetchUser()

        val intent = intent
        campaignId = intent.getSerializableExtra("campaignId").toString().toLong()
        val titleString = intent.getSerializableExtra("titleString").toString()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = titleString

        val viewModel = ViewModelProvider(this).get(WebSocketDataViewModel::class.java)
        viewModel.startWebSocket(campaignId)
    }

    private fun fetchMembers() {
        disposables.add(campaignMemberApi.getMembers(campaignId)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                campaignMembersFromApi = it
            }
            .subscribe { campaignMembers, error ->
                if (error != null) {
                    val httpException: HttpException = error as HttpException
                    when (httpException.code()) {
                        404 -> Toast.makeText(this, "404 Not Found", Toast.LENGTH_SHORT).show()

                        401 -> Toast.makeText(this, "401 Unauthorized", Toast.LENGTH_SHORT).show()
                    }
                } else {
                }
            })
    }

    private fun fetchUser() {
        disposables.add(userApi.getUser()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                user = it
                fetchMembers()
            }
            .subscribe { user, error ->
                if (error != null) {
                    val httpException: HttpException = error as HttpException
                    when (httpException.code()) {
                        404 -> Toast.makeText(this, "404 Not Found", Toast.LENGTH_SHORT).show()
                        401 -> Toast.makeText(this, "401 Unauthorized", Toast.LENGTH_SHORT).show()
                    }
                } else {
                }
            })
    }

    fun getCampaignId(): Long {
        return campaignId
    }

    fun getUser(): User {
        return user
    }

    fun getCampaignMemberList(): List<CampaignMember> {
        return campaignMembersFromApi
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {

                val currentFragment = supportFragmentManager.findFragmentByTag("tools")

                val bundle = Bundle()
                bundle.putLong("campaignId", campaignId)
                currentFragment!!.arguments = bundle

                if (currentFragment != null && currentFragment.isVisible) {
                    finish()
                } else {
                    supportFragmentManager.popBackStack()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }
}

