package lmu.msp.frontend.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import lmu.msp.frontend.HomeActivity
import lmu.msp.frontend.viewmodels.UserViewModel
import lmu.msp.frontend.R
import lmu.msp.frontend.api.PenAndPaperApiInterface
import lmu.msp.frontend.api.model.User
import lmu.msp.frontend.helpers.TokenManager
import lmu.msp.frontend.helpers.auth0.PAuthenticator
import lmu.msp.frontend.helpers.retrofit.RetrofitProvider

class HomeFragment : Fragment() {
    companion object {
        private const val TAG = "HomeFragment"
    }

    val sharedViewModel: UserViewModel by activityViewModels()
    private lateinit var user_text: TextView
    private lateinit var joinCampaignButton: Button
    private lateinit var createCampaignButton: Button
    private lateinit var deleteCampaignButton: Button
    private lateinit var joinCampaignEditText: EditText
    private lateinit var createCampaignEditText: EditText
    private lateinit var deleteCampaignEditText: EditText

    private lateinit var campaignApi: PenAndPaperApiInterface.CampaignApi
    private lateinit var auth: PAuthenticator




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        user_text = view.findViewById(R.id.user_id_text)

        joinCampaignButton = view.findViewById(R.id.joinCampaignButton)
        createCampaignButton = view.findViewById(R.id.createCampaignButton)
        deleteCampaignButton = view.findViewById(R.id.deleteCampaignButton)
        joinCampaignEditText = view.findViewById(R.id.joinCampaignEditText)
        createCampaignEditText = view.findViewById(R.id.createCampaignEditText)
        deleteCampaignEditText = view.findViewById(R.id.deleteCampaignEditText)

        joinCampaignButton.setOnClickListener { joinCampaign() }
        createCampaignButton.setOnClickListener { createCampaign() }
        deleteCampaignButton.setOnClickListener { deleteCampaign() }

        auth = PAuthenticator(view.context, TokenManager(view.context))
        campaignApi = RetrofitProvider(view.context).getCampaignApi()


        sharedViewModel.userData.observe(
            viewLifecycleOwner,
            { userData -> user_text.setText(userData.toString()) })


        var user = sharedViewModel.testString
        Log.i(TAG, user)
        //viewModel.getUser().observe(viewLifecycleOwner, { Log.i(TAG, "new user ${it.id}") })

        return view

    }

    private fun deleteCampaign() {
        campaignApi.deleteCampaign(deleteCampaignEditText.text.toString().toLong())
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                Log.e(TAG, "error ${it.message}")
                //TODO ERROR HANDLING
            }
            .doOnSuccess {
                Toast.makeText(context, "Deleted Campaign", Toast.LENGTH_SHORT).show()
            }
            .subscribe()
    }

    private fun createCampaign() {
        campaignApi.createCampaign(createCampaignEditText.text.toString())
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                Log.e(TAG, "error ${it.message}")
                //TODO ERROR HANDLING
            }
            .doOnSuccess {
                Toast.makeText(context, "Created Campaign", Toast.LENGTH_SHORT).show()
            }
            .subscribe()
    }

    private fun joinCampaign() {


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


}
