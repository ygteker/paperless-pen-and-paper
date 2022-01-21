package lmu.msp.frontend.ui.campaign

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import lmu.msp.frontend.HomeActivity
import lmu.msp.frontend.R
import lmu.msp.frontend.api.PenAndPaperApiInterface
import lmu.msp.frontend.api.model.User
import lmu.msp.frontend.helpers.TokenManager
import lmu.msp.frontend.helpers.auth0.PAuthenticator
import lmu.msp.frontend.helpers.retrofit.RetrofitProvider
import lmu.msp.frontend.viewmodels.UserViewModel


class CampaignFragment : Fragment() {

    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArrayList: ArrayList<campaigns>
    private lateinit var titleStrings: ArrayList<String>
    private lateinit var campaignIds: ArrayList<String>

    private lateinit var campaignAdapter: CampaignAdapter

    private lateinit var userApi: PenAndPaperApiInterface.UserApi
    private lateinit var auth: PAuthenticator

    val sharedViewModel: UserViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_campaign, container, false)

        auth = PAuthenticator(view.context, TokenManager(view.context))
        userApi = RetrofitProvider(view.context).getUserApi()

        titleStrings = arrayListOf()
        campaignIds = arrayListOf()

        newRecyclerView = view.findViewById(R.id.recyclerView)

        val horizontalLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        newRecyclerView.layoutManager = horizontalLayoutManager

        newRecyclerView.setHasFixedSize(true)
        newArrayList = arrayListOf<campaigns>()

        campaignAdapter = CampaignAdapter(newArrayList)
        newRecyclerView.adapter = campaignAdapter

        val helper = LinearSnapHelper()
        helper.attachToRecyclerView(newRecyclerView)

        fetchCampaigns()

        return view
    }

    private fun fetchCampaigns() {
        userApi.getUser()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                Log.e(TAG, "error ${it.message}")
                //TODO ERROR HANDLING
            }
            .doOnSuccess {
                sharedViewModel.setUser(it)
                fillCampaignArray()
            }
            .subscribe()
    }

    private fun fillCampaignArray() {
        var userFromViewModel: User
        sharedViewModel.userData.observe(
            viewLifecycleOwner,
            { userData ->
                userFromViewModel = userData
                Log.d(
                    TAG,
                    "This is the campaignOwner: " + userFromViewModel.campaignOwner.toString()
                )
                Log.d(
                    TAG,
                    "This is the campaignMember: " + userFromViewModel.campaignMember.toString()
                )

                userFromViewModel.campaignOwner.forEach {
                    campaignIds.add(it.id.toString())
                    titleStrings.add(it.title)
                }
                userFromViewModel.campaignMember.forEach {
                    campaignIds.add(it.campaign.id.toString())
                    titleStrings.add(it.campaign.title)

                }
                fillCampaigns()
            })
    }

    private fun fillCampaigns() {
        for (i in titleStrings.indices) {
            val data = campaigns(titleStrings[i], campaignIds[i])
            newArrayList.add(data)
        }
        campaignAdapter.notifyDataSetChanged()
    }
}