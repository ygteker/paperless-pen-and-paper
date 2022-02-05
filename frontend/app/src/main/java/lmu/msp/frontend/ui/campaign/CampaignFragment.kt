package lmu.msp.frontend.ui.campaign

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import lmu.msp.frontend.HomeActivity
import lmu.msp.frontend.R
import lmu.msp.frontend.api.PenAndPaperApiInterface
import lmu.msp.frontend.api.model.User
import lmu.msp.frontend.helpers.TokenManager
import lmu.msp.frontend.helpers.auth0.PAuthenticator
import lmu.msp.frontend.helpers.retrofit.RetrofitProvider
import lmu.msp.frontend.viewmodels.UserViewModel

/**
 * Handles the recycler view that displays campaigns the active user has joined
 * @author Valentin Scheibe
 */
class CampaignFragment : Fragment() {

    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArrayList: ArrayList<campaigns>
    private lateinit var titleStrings: ArrayList<String>
    private lateinit var campaignIds: ArrayList<String>

    private lateinit var campaignAdapter: CampaignAdapter

    private lateinit var userApi: PenAndPaperApiInterface.UserApi
    private lateinit var auth: PAuthenticator
    private val disposables = CompositeDisposable()


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

    /**
     * Fetches all campaigns the active user is part of from the backend
     */
    private fun fetchCampaigns() {
        disposables.add(userApi.getUser()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                sharedViewModel.setUser(it)
                fillCampaignArray()
            }
            .subscribe { campaignMembers, error ->
                if (error != null) {
                    val httpException: HttpException = error as HttpException
                    when (httpException.code()) {
                        404 -> Toast.makeText(context, "404 Not Found", Toast.LENGTH_SHORT).show()
                        401 -> Toast.makeText(context, "401 Unauthorized", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                }
            })
    }

    /**
     * Fills the campaignIds and titleStrings lists view with the campaigns the active user is part of
     * for each campaign that the user owns and is a member of
     */
    private fun fillCampaignArray() {
        var userFromViewModel: User
        sharedViewModel.userData.observe(
            viewLifecycleOwner,
            { userData ->
                userFromViewModel = userData

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

    /**
     * fills the recycler view with the information from the titleStrings and campaignIds lists
     * and updates the recyclerView
     */
    private fun fillCampaigns() {
        newArrayList.clear()
        for (i in titleStrings.indices) {
            val data = campaigns(titleStrings[i], campaignIds[i])
            newArrayList.add(data)
        }
        campaignAdapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

}