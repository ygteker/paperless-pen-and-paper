package lmu.msp.frontend.ui.campaign

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import lmu.msp.frontend.R
import lmu.msp.frontend.api.PenAndPaperApiInterface
import lmu.msp.frontend.api.model.Campaign
import lmu.msp.frontend.api.model.CampaignMember
import lmu.msp.frontend.helpers.TokenManager
import lmu.msp.frontend.helpers.auth0.PAuthenticator
import lmu.msp.frontend.helpers.retrofit.RetrofitProvider
import lmu.msp.frontend.viewmodels.CampaignActivityViewModel

class UserFragment : Fragment() {

    private lateinit var userListText: TextView

    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArrayList: ArrayList<campaignUsers>
    private lateinit var userAdapter: UserAdapter

    private var campaignId = 0L

    private lateinit var campaignApi: PenAndPaperApiInterface.CampaignApi
    private lateinit var campaignMemberApi: PenAndPaperApiInterface.CampaignMemberApi
    private lateinit var auth: PAuthenticator

    val sharedViewModel: CampaignActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_list, container, false)

        userListText = view.findViewById(R.id.UserListText)

        auth = PAuthenticator(view.context, TokenManager(view.context))
        campaignApi = RetrofitProvider(view.context).getCampaignApi()
        campaignMemberApi = RetrofitProvider(view.context).getCampaignMemberApi()

        newRecyclerView = view.findViewById(R.id.userListRecyclerView)
        val layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        newRecyclerView.layoutManager = layoutManager
        newArrayList = arrayListOf<campaignUsers>()
        userAdapter = UserAdapter(newArrayList)
        newRecyclerView.adapter = userAdapter

        campaignId = sharedViewModel.campaignId.value!!

        fetchMembers()

        return view
    }

    private fun fetchMembers() {
        userListText.text = campaignId.toString()

        var campaignFromApi: Campaign
        var campaignMemberFromApi: List<CampaignMember>

     /*   campaignApi.getCampaign(campaignId)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                Log.e(ContentValues.TAG, "error ${it.message}")
                //TODO ERROR HANDLING
            }
            .doOnSuccess {
               campaignFromApi = it
                Log.d(TAG, "campaign API-call: " + campaignFromApi.toString())
            }
            .subscribe()
      */

        campaignMemberApi.getMembers(campaignId)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                Log.e(ContentValues.TAG, "error ${it.message}")
                //TODO ERROR HANDLING
            }
            .doOnSuccess {

                campaignMemberFromApi = it
                Log.d(TAG, "CampaignMember API-call: " + campaignMemberFromApi.toString())

            }
            .subscribe()


    }

}