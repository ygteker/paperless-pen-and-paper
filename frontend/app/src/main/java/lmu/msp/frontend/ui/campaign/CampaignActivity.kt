package lmu.msp.frontend.ui.campaign

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import lmu.msp.frontend.HomeActivity
import lmu.msp.frontend.R
import lmu.msp.frontend.api.PenAndPaperApiInterface
import lmu.msp.frontend.api.model.CampaignMember
import lmu.msp.frontend.api.model.User
import lmu.msp.frontend.databinding.ActivityCampaignBinding
import lmu.msp.frontend.helpers.TokenManager
import lmu.msp.frontend.helpers.auth0.PAuthenticator
import lmu.msp.frontend.helpers.retrofit.RetrofitProvider
import lmu.msp.frontend.viewmodels.WebSocketDataViewModel
import okhttp3.internal.wait

class CampaignActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCampaignBinding

    private lateinit var userApi: PenAndPaperApiInterface.UserApi
    private lateinit var campaignMemberApi: PenAndPaperApiInterface.CampaignMemberApi
    private lateinit var auth: PAuthenticator

    private lateinit var user: User
    private lateinit var campaignMemberFromApi: List<CampaignMember>
    private var campaignId: Long = 0

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
        fetchMembers()


        val intent = intent
        campaignId = intent.getSerializableExtra("campaignId").toString().toLong()
        val titleString = intent.getSerializableExtra("titleString").toString()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = titleString

        val viewModel = ViewModelProvider(this).get(WebSocketDataViewModel::class.java)
        viewModel.startWebSocket(campaignId)
    }

    private fun fetchMembers() {
        campaignMemberApi.getMembers(campaignId)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                campaignMemberFromApi = it
            }
            .subscribe(
                //TODO ERROR HANDLING
            )
    }

    private fun fetchUser() {
        userApi.getUser()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                user = it
            }
            .subscribe(
                //TODO ERROR HANDLING
            )
    }

    fun getCampaignId(): Long {
        return campaignId
    }

    fun getUser(): User{
        return user
    }

    fun getCampaignMemberList(): List<CampaignMember>{
        return campaignMemberFromApi
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {

                val currentFragment = supportFragmentManager.findFragmentByTag("tools")

                val bundle = Bundle()
                bundle.putLong("campaignId",campaignId)
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

}

