package lmu.msp.frontend.ui.profile

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import lmu.msp.frontend.R
import lmu.msp.frontend.databinding.FragmentMessageBinding
import lmu.msp.frontend.databinding.FragmentProfileBinding
import lmu.msp.frontend.helpers.TokenManager
import lmu.msp.frontend.models.MessageModel
import lmu.msp.frontend.viewmodels.MessagesViewModel
import kotlin.math.log

class MessageFragment: Fragment() {

    private var _binding: FragmentMessageBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MessagesViewModel
    private lateinit var tokenManager: TokenManager

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(requireActivity()).get(MessagesViewModel::class.java)
        tokenManager = activity?.applicationContext?.let { TokenManager(it) }!!
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMessageBinding.inflate(inflater, container, false)

        val pos: Int? = arguments?.getInt("pos")
        val messageModel = arguments?.getParcelable<MessageModel>("messageModel")
        binding.messageText.text = pos.toString() + "\n" + messageModel?.content + "Message id: " + messageModel?.id

        binding.deleteButton.setOnClickListener {

            viewModel.deleteMessage("Bearer " + tokenManager.load(), messageModel!!)

            requireActivity().supportFragmentManager.popBackStack()
        }

        return binding.root
    }
}