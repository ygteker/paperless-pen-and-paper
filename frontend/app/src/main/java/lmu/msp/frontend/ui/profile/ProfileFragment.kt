package lmu.msp.frontend.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import lmu.msp.frontend.HomeActivity
import lmu.msp.frontend.R
import lmu.msp.frontend.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.logoutButton.setOnClickListener {
            (activity as HomeActivity?)!!.logout()
        }

        val listView = binding.list
        val profileArray = resources.getStringArray(R.array.profile_list_items)
        val adapter = ArrayAdapter<String>(requireActivity(), android.R.layout.simple_list_item_1, profileArray)
        listView.adapter = adapter

        listView.setOnItemClickListener{ parent, view, position, id ->
            //TODO(implement list item clicks)

            if (position == 0) {
                //TODO start MessagesActivity
            } else if (position == 1) {
                //TODO start
            } else {

            }
        }

        return binding.root
    }
}