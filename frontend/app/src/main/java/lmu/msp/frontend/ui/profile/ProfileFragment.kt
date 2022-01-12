package lmu.msp.frontend.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import lmu.msp.frontend.HomeActivity
import lmu.msp.frontend.viewmodels.UserViewModel
import lmu.msp.frontend.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    companion object {
        private const val TAG = "ProfileFragment"
    }

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.logoutButton.setOnClickListener {
            (activity as HomeActivity?)!!.logout()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel: UserViewModel by activityViewModels()
        viewModel.getUser().observe(viewLifecycleOwner, { Log.i(TAG, "new user ${it.id}") })
    }

}