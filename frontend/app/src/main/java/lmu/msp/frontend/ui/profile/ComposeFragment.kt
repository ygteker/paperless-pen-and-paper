package lmu.msp.frontend.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import lmu.msp.frontend.databinding.FragmentComposeBinding
import lmu.msp.frontend.helpers.TokenManager
import lmu.msp.frontend.viewmodels.MessagesViewModel

class ComposeFragment: Fragment() {

    private var _binding: FragmentComposeBinding? = null
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
        _binding = FragmentComposeBinding.inflate(inflater, container, false)

        binding.sendButton.setOnClickListener {

            // TODO confirm receiver
            // TODO call send message api
        }



        return binding.root
    }
}