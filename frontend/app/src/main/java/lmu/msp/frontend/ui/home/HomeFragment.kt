package lmu.msp.frontend.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import lmu.msp.frontend.viewmodels.UserViewModel
import lmu.msp.frontend.R

class HomeFragment : Fragment() {
    companion object{
        private const val TAG = "HomeFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel: UserViewModel by activityViewModels()
        viewModel.getUser().observe(viewLifecycleOwner, { Log.i(TAG, "new user ${it.id}") })
    }
}