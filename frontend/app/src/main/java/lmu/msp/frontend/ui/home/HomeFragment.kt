package lmu.msp.frontend.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import lmu.msp.frontend.viewmodels.UserViewModel
import lmu.msp.frontend.R
import lmu.msp.frontend.api.model.User

class HomeFragment : Fragment() {
    companion object {
        private const val TAG = "HomeFragment"
    }

    val sharedViewModel: UserViewModel by activityViewModels()
    private lateinit var user_text: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        user_text = view.findViewById(R.id.user_id_text)

        sharedViewModel.userData.observe(
            viewLifecycleOwner,
            { userData -> user_text.setText(userData.toString()) })


        var user = sharedViewModel.testString
        Log.i(TAG, user)
        //viewModel.getUser().observe(viewLifecycleOwner, { Log.i(TAG, "new user ${it.id}") })

        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


}
