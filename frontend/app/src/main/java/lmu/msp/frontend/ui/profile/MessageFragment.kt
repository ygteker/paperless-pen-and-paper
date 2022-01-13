package lmu.msp.frontend.ui.profile

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import lmu.msp.frontend.databinding.FragmentMessageBinding
import lmu.msp.frontend.databinding.FragmentProfileBinding
import kotlin.math.log

class MessageFragment: Fragment() {

    private var _binding: FragmentMessageBinding? = null
    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.i("MESSAGE_FRAGMENT", "ASDSADADASDASD")

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i("MESSAGE_FRAGMENT", "ASDSADADASDASD")

        _binding = FragmentMessageBinding.inflate(inflater, container, false)
        Log.i("MESSAGE_FRAGMENT", "ASDSADADASDASD")

        val pos: Int? = arguments?.getInt("pos")
        binding.messageText.text = pos.toString() + "TestingTestingTestingTestingTestingTesting"
        Log.i("MESSAGE_FRAGMENT", "ASDSADADASDASD")

        return binding.root
    }
}