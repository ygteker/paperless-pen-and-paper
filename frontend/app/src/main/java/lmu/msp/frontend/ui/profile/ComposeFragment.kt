package lmu.msp.frontend.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import lmu.msp.frontend.api.PenAndPaperApiInterface
import lmu.msp.frontend.databinding.FragmentComposeBinding
import lmu.msp.frontend.helpers.TokenManager
import lmu.msp.frontend.helpers.retrofit.RetrofitProvider
import lmu.msp.frontend.viewmodels.MessagesViewModel

class ComposeFragment: Fragment() {

    private var _binding: FragmentComposeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MessagesViewModel
    private lateinit var tokenManager: TokenManager
    private lateinit var messageApi: PenAndPaperApiInterface.MessageApi
    private lateinit var receiverEditText: EditText
    private lateinit var messageEditText: EditText

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
        _binding = FragmentComposeBinding.inflate(inflater, container, false)

        receiverEditText = binding.sendTo
        messageEditText = binding.message
        messageApi = RetrofitProvider(requireContext()).getMessageApi()

        binding.sendButton.setOnClickListener {
            messageApi.sendMessage(receiverEditText.text.toString().toLong(), messageEditText.text.toString())

        }



        return binding.root
    }
}