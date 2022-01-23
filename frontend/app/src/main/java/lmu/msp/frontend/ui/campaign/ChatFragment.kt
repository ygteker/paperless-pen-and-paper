package lmu.msp.frontend.ui.campaign

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import lmu.msp.frontend.R
import lmu.msp.frontend.databinding.FragmentChatBinding
import lmu.msp.frontend.helpers.ChatMessagesAdapter
import lmu.msp.frontend.models.websocket.ChatMessage
import lmu.msp.frontend.viewmodels.CampaignViewModel
import lmu.msp.frontend.viewmodels.WebSocketDataViewModel


class ChatFragment : Fragment(R.layout.fragment_chat) {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private lateinit var chatView: RecyclerView
    private lateinit var viewManager: LinearLayoutManager
    private lateinit var viewModel: WebSocketDataViewModel

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
        chatView.layoutManager = viewManager

        binding.sendButton.setOnClickListener {
            val textToSend = binding.chatBox.text.toString()
            //TODO handle ws call
        }
        observeData()
//        createDummyData()
        return binding.root
    }

    private fun observeData() {
        viewModel.getChatMessages().observe(requireActivity(), Observer{
            chatView.adapter = ChatMessagesAdapter(it)
        })
    }

    private fun sendMessage(message: ChatMessage, receiverId: Int) {
        viewModel.sendChatMessage(ChatMessage(message="test", receiverId, 1))
    }


    private fun createDummyData() {
        viewModel.sendChatMessage(ChatMessage("Dummy message", 1, 1))
    }

}