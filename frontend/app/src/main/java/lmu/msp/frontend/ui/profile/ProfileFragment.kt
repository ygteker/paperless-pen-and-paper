package lmu.msp.frontend.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import lmu.msp.frontend.HomeActivity
import lmu.msp.frontend.R
import lmu.msp.frontend.databinding.FragmentProfileBinding
import lmu.msp.frontend.helpers.auth0.CustomAdapter
import lmu.msp.frontend.viewmodels.HomeActivityViewModel

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.logoutButton.setOnClickListener {
            (activity as HomeActivity?)!!.logout()
        }

        val recyclerView = binding.list
        val adapter = CustomAdapter(viewModel.getProfileListItems())
        recyclerView.adapter = adapter
        val profileArray = resources.getStringArray(R.array.profile_list_items)


        return binding.root
    }
}