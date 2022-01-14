package lmu.msp.frontend.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import lmu.msp.frontend.viewmodels.UserViewModel
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import lmu.msp.frontend.HomeActivity
import lmu.msp.frontend.R
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

                activity?.let{
                    val intent = Intent (it, MessagesActivity::class.java)
                    intent.putExtra("access_token", it.intent.extras?.get("access_token").toString())
                    it.startActivity(intent)
                }

            } else if (position == 1) {
                //TODO start
            } else {

            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel: UserViewModel by activityViewModels()
        viewModel.getUser().observe(viewLifecycleOwner, { Log.i(TAG, "new user ${it.id}") })
    }

}