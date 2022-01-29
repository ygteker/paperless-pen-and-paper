package lmu.msp.frontend.ui.campaign

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import lmu.msp.frontend.R
import lmu.msp.frontend.api.PenAndPaperApiInterface
import lmu.msp.frontend.api.model.CampaignMember
import lmu.msp.frontend.api.model.ChatType
import lmu.msp.frontend.api.model.GeneralChatMessage
import lmu.msp.frontend.api.model.User
import lmu.msp.frontend.databinding.FragmentChatBinding
import lmu.msp.frontend.helpers.ChatMessagesAdapter
import lmu.msp.frontend.helpers.retrofit.RetrofitProvider
import lmu.msp.frontend.models.websocket.ChatMessage
import lmu.msp.frontend.models.websocket.GroupMessage
import lmu.msp.frontend.viewmodels.WebSocketDataViewModel


class ChatFragment : Fragment(R.layout.fragment_chat) {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private lateinit var chatView: RecyclerView
    private lateinit var viewManager: LinearLayoutManager
    private lateinit var viewModel: WebSocketDataViewModel
    private lateinit var chatMessagesAdapter: ChatMessagesAdapter
    private lateinit var userApi: PenAndPaperApiInterface.UserApi
    private lateinit var memberApi: PenAndPaperApiInterface.CampaignMemberApi
    private lateinit var loggedInUser: User
    private lateinit var members: List<CampaignMember>
    private lateinit var campaignApi: PenAndPaperApiInterface.CampaignApi


    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(requireActivity()).get(WebSocketDataViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        chatView = binding.chatView
        viewManager = LinearLayoutManager(requireActivity())
        viewManager.stackFromEnd = true
        chatView.layoutManager = viewManager
        chatMessagesAdapter = ChatMessagesAdapter(MutableLiveData(), requireActivity())
        chatView.adapter = chatMessagesAdapter
        userApi = RetrofitProvider(requireContext()).getUserApi()
        memberApi = RetrofitProvider(requireContext()).getCampaignMemberApi()
        userApi.getUser()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                Log.e(ContentValues.TAG, "error ${it.message}")
            }
            .doOnSuccess {
                loggedInUser = it
            }
            .subscribe()

        memberApi.getMembers(16)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                Log.e(ContentValues.TAG, "error ${it.message}")
            }
            .doOnSuccess {
                members = it
            }
            .subscribe()

        binding.sendButton.setOnClickListener {
            sendMessage(binding.chatBox.text.toString())
        }
        observeData()
        return binding.root
    }

    private fun observeData() {
        viewModel.getGroupMessages().observe(requireActivity(), Observer{
            val newMessages: MutableList<GeneralChatMessage> = mutableListOf()
            for (newMessage in it) {
                newMessages.add(GeneralChatMessage(newMessage.senderId.toString(), newMessage.message, ChatType.GROUP))
            }
        })

        viewModel.getChatMessages().observe(requireActivity(), {
            val newMessages: MutableList<GeneralChatMessage> = mutableListOf()
            for (newMessage in it) {
                newMessages.add(GeneralChatMessage(newMessage.senderId.toString(), newMessage.message!!, ChatType.PERSONAL))
            }
        })
    }

    private fun sendMessage(message: String) {
        val  regex = "^@\\w+".toRegex()
        if (regex.containsMatchIn(message)) {
            val charName = message.split(" ")[0].removePrefix("@")
            Log.i("CharName", charName)

        } else {
           viewModel.sendGroupMessage(GroupMessage(loggedInUser.id, message))
        }

    }

}