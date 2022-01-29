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
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException

class HomeFragment : Fragment() {
    companion object {
        private const val TAG = "HomeFragment"
    }

    val sharedViewModel: UserViewModel by activityViewModels()
    private lateinit var joinCampaignButton: Button
    private lateinit var createCampaignButton: Button
    private lateinit var deleteCampaignButton: Button
    private lateinit var joinCampaignEditText: EditText
    private lateinit var joinCampaignCharacterText: EditText
    private lateinit var createCampaignEditText: EditText
    private lateinit var deleteCampaignEditText: EditText

    private lateinit var campaignApi: PenAndPaperApiInterface.CampaignApi
    private lateinit var inviteCampaignApi: PenAndPaperApiInterface.InviteCampaignApi
    private lateinit var auth: PAuthenticator


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        findViews(view)

        joinCampaignButton.setOnClickListener { joinCampaign() }
        createCampaignButton.setOnClickListener { createCampaign() }
        deleteCampaignButton.setOnClickListener { deleteCampaign() }

        auth = PAuthenticator(view.context, TokenManager(view.context))
        campaignApi = RetrofitProvider(view.context).getCampaignApi()
        inviteCampaignApi = RetrofitProvider(view.context).getInviteCampaignApi()

        //viewModel.getUser().observe(viewLifecycleOwner, { Log.i(TAG, "new user ${it.id}") })

        return view

    }

    private fun findViews(view: View) {
        joinCampaignButton = view.findViewById(R.id.joinCampaignButton)
        createCampaignButton = view.findViewById(R.id.createCampaignButton)
        deleteCampaignButton = view.findViewById(R.id.deleteCampaignButton)
        joinCampaignEditText = view.findViewById(R.id.joinCampaignEditText)
        joinCampaignCharacterText = view.findViewById(R.id.joinCampaignCharacterText)
        createCampaignEditText = view.findViewById(R.id.createCampaignEditText)
        deleteCampaignEditText = view.findViewById(R.id.deleteCampaignEditText)
    }

    private fun deleteCampaign() {
        if (deleteCampaignEditText.text.isNullOrBlank()) {
            Toast.makeText(
                context,
                "Please specify which campaign you would like to delete!",
                Toast.LENGTH_LONG
            ).show()
            deleteCampaignEditText.setError("Must not be empty!")
        } else {
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
    }

    private fun createCampaign() {
        if (createCampaignEditText.text.isNullOrBlank()) {
            Toast.makeText(
                context,
                "Please specify the name of the campaign you would like to create!",
                Toast.LENGTH_LONG
            ).show()
            createCampaignEditText.setError("Must not be empty!")
        } else {
            campaignApi.createCampaign(createCampaignEditText.text.toString().toRequestBody())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError {
                    Log.e(TAG, "error ${it.message}")
                    //TODO ERROR HANDLING
                }
                .doOnSuccess {
                    Toast.makeText(context, "Created Campaign", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, createCampaignEditText.text.toString())
                }
                .subscribe()
        }
    }

    private fun joinCampaign() {
        if (joinCampaignEditText.text.isNullOrBlank() || joinCampaignCharacterText.text.isNullOrBlank()) {

            if (joinCampaignEditText.text.isNullOrBlank()) {
                joinCampaignEditText.setError("Must not be empty!")
                Toast.makeText(
                    context,
                    "Please specify which campaign you would like to join!",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                joinCampaignCharacterText.setError("Must not be empty!")
                Toast.makeText(
                    context,
                    "Please specify the characters name you would like to join the campaign with!",
                    Toast.LENGTH_LONG
                ).show()
            }
        } else {
            inviteCampaignApi.acceptInvite(
                joinCampaignEditText.text.toString().toLong(),
                joinCampaignCharacterText.text.toString()
            )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError {
                    try {

                    } catch (it: HttpException) {
                        Log.e(TAG, "error ${it.message}")
                    }

                }
                .doOnSuccess {
                    Toast.makeText(context, "Join Campaign Success", Toast.LENGTH_SHORT).show()
                }
                .subscribe()
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


}
