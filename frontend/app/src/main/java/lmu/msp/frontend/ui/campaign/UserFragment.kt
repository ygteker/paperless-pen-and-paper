package lmu.msp.frontend.ui.campaign

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import lmu.msp.frontend.R
import lmu.msp.frontend.api.PenAndPaperApiInterface
import lmu.msp.frontend.api.model.Campaign
import lmu.msp.frontend.api.model.CampaignMember
import lmu.msp.frontend.helpers.TokenManager
import lmu.msp.frontend.helpers.auth0.PAuthenticator
import lmu.msp.frontend.helpers.retrofit.RetrofitProvider


/**
 * @author Valentin Scheibe
 */
class UserFragment : Fragment() {

    private lateinit var editText_CharacterName: EditText
    private lateinit var editText_DeleteUser: EditText
    private lateinit var changeCharacterNameButton: Button
    private lateinit var deleterUserButton: Button

    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArrayList: ArrayList<campaignUsers>
    private lateinit var userAdapter: UserAdapter

    private var campaignId = 0L

    private lateinit var campaignApi: PenAndPaperApiInterface.CampaignApi
    private lateinit var campaignMemberApi: PenAndPaperApiInterface.CampaignMemberApi
    private lateinit var auth: PAuthenticator
    private val disposables = CompositeDisposable()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_list, container, false)

        editText_CharacterName = view.findViewById(R.id.editText_CharacterName)
        editText_DeleteUser = view.findViewById(R.id.editText_DeleteUser)
        changeCharacterNameButton = view.findViewById(R.id.changeCharacterNameButton)
        deleterUserButton = view.findViewById(R.id.deleterUserButton)
        changeCharacterNameButton.setOnClickListener { changeCharacterName() }
        deleterUserButton.setOnClickListener { deleteUserFromCampaign() }

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

        campaignId = (activity as CampaignActivity).getCampaignId()

        fetchMembers()

        return view
    }

    private fun deleteUserFromCampaign() {
        if (editText_DeleteUser.text.isNullOrBlank()) {
            Toast.makeText(
                context,
                "Please specify the user you want to remove from the campaign!",
                Toast.LENGTH_LONG
            ).show()
            editText_DeleteUser.setError("Must not be empty!")
        } else {
            disposables.add(campaignMemberApi.removeMember(
                campaignId,
                editText_DeleteUser.text.toString().toLong()
            )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess {
                    Toast.makeText(
                        context,
                        "Removed user from the campaign!",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    userAdapter.notifyDataSetChanged()
                    editText_DeleteUser.text.clear()
                }
                .subscribe { reply, error ->
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

    private fun changeCharacterName() {
        if (editText_CharacterName.text.isNullOrBlank()) {
            Toast.makeText(
                context,
                "Please specify what you want to change your name to!",
                Toast.LENGTH_LONG
            ).show()
            editText_CharacterName.setError("Must not be empty!")
        } else {
            disposables.add(campaignMemberApi.updateMember(
                campaignId,
                editText_CharacterName.text.toString()
            )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess {
                    Toast.makeText(context, "Changed Character Name ", Toast.LENGTH_SHORT).show()
                    editText_CharacterName.text.clear()
                    userAdapter.notifyDataSetChanged()
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

    private fun fetchMembers() {
        var campaignMemberFromApi: List<CampaignMember>
        var campaignFromApi: Campaign

        disposables.add(campaignApi.getCampaign(campaignId)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                campaignFromApi = it
                fillDungeonMaster(campaignFromApi)
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

        disposables.add(campaignMemberApi.getMembers(campaignId)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                campaignMemberFromApi = it
                if (campaignMemberFromApi.isNotEmpty()) {
                    fillUsers(campaignMemberFromApi)
                }
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

    private fun fillDungeonMaster(campaignFromApi: Campaign?) {
        val dmData =
            campaignUsers(campaignFromApi?.owner.toString(), "Game Master")
        newArrayList.add(dmData)
        userAdapter.notifyDataSetChanged()
    }

    private fun fillUsers(campaignMemberFromApi: List<CampaignMember>) {
        var characterNames = arrayListOf<String>()
        var userIds = arrayListOf<String>()
        campaignMemberFromApi.forEach {
            characterNames.add(it.characterName)
            userIds.add(it.user.toString())
        }

        for (i in userIds.indices) {
            val data = campaignUsers(userIds[i], characterNames[i])
            newArrayList.add(data)
        }
        userAdapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

}