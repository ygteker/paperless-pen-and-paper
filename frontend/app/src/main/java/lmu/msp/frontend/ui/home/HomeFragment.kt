package lmu.msp.frontend.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import lmu.msp.frontend.viewmodels.UserViewModel
import lmu.msp.frontend.R
import lmu.msp.frontend.api.PenAndPaperApiInterface
import lmu.msp.frontend.diceRolling.DiceFragmentAnimated
import lmu.msp.frontend.helpers.TokenManager
import lmu.msp.frontend.helpers.auth0.PAuthenticator
import lmu.msp.frontend.helpers.retrofit.RetrofitProvider
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber
import android.app.Activity
import android.content.Intent
import androidx.fragment.app.FragmentManager
import lmu.msp.frontend.diceRolling.DiceActivity
import lmu.msp.frontend.ui.campaign.CampaignActivity


/**
 * @author Valentin Scheibe
 */
class HomeFragment : Fragment() {
    companion object {
        private const val TAG = "HomeFragment"
    }

    private lateinit var joinCampaignButton: Button
    private lateinit var createCampaignButton: Button
    private lateinit var deleteCampaignButton: Button
    private lateinit var navigateToRollDiceButton:Button
    private lateinit var joinCampaignEditText: EditText
    private lateinit var joinCampaignCharacterText: EditText
    private lateinit var createCampaignEditText: EditText
    private lateinit var deleteCampaignEditText: EditText
    private val disposables = CompositeDisposable()

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
        navigateToRollDiceButton.setOnClickListener { loadDiceFragment(view) }

        auth = PAuthenticator(view.context, TokenManager(view.context))
        campaignApi = RetrofitProvider(view.context).getCampaignApi()
        inviteCampaignApi = RetrofitProvider(view.context).getInviteCampaignApi()

        return view
    }

    private fun loadDiceFragment(view: View) {
        val intent = Intent(view.context, DiceActivity::class.java)
        view.context.startActivity(intent)
    }

    private fun findViews(view: View) {
        joinCampaignButton = view.findViewById(R.id.joinCampaignButton)
        createCampaignButton = view.findViewById(R.id.createCampaignButton)
        deleteCampaignButton = view.findViewById(R.id.deleteCampaignButton)
        navigateToRollDiceButton = view.findViewById(R.id.navigateToRollDiceButton)
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
            disposables.add(campaignApi.deleteCampaign(
                deleteCampaignEditText.text.toString().toLong()
            )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess {
                    Toast.makeText(context, "Deleted Campaign", Toast.LENGTH_SHORT).show()
                    deleteCampaignEditText.text.clear()
                }
                .subscribe { campaignMembers, error ->
                    if (error != null) {
                        val httpException: HttpException = error as HttpException
                        when (httpException.code()) {
                            404 -> Toast.makeText(context, "404 Not Found", Toast.LENGTH_SHORT)
                                .show()
                            401 -> Toast.makeText(
                                context,
                                "401 Unauthorized",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                    }
                })
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
            disposables.add(campaignApi.createCampaign(
                createCampaignEditText.text.toString().toRequestBody()
            )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess {
                    Toast.makeText(context, "Created Campaign", Toast.LENGTH_SHORT).show()
                    createCampaignEditText.text.clear()
                }

                .subscribe { campaignMembers, error ->
                    if (error != null) {
                        val httpException: HttpException = error as HttpException
                        when (httpException.code()) {
                            404 -> Toast.makeText(context, "404 Not Found", Toast.LENGTH_SHORT)
                                .show()
                            401 -> Toast.makeText(
                                context,
                                "401 Unauthorized",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                    }
                })
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
            disposables.add(inviteCampaignApi.acceptInvite(
                joinCampaignEditText.text.toString().toLong(),
                joinCampaignCharacterText.text.toString()
            )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess {
                    Toast.makeText(context, "Join Campaign Success", Toast.LENGTH_SHORT).show()
                    joinCampaignEditText.text.clear()
                    joinCampaignCharacterText.text.clear()
                }
                .subscribe { campaignMembers, error ->
                    if (error == null) {
                    } else {
                        val httpException: HttpException = error as HttpException
                        when (httpException.code()) {
                            404 -> Toast.makeText(context, "404 Not Found", Toast.LENGTH_SHORT)
                                .show()
                            401 -> Toast.makeText(
                                context,
                                "401 Unauthorized",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }
}
