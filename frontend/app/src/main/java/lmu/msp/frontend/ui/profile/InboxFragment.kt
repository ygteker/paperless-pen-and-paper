package lmu.msp.frontend.ui.profile

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import lmu.msp.frontend.R
import lmu.msp.frontend.databinding.FragmentInboxBinding
import lmu.msp.frontend.helpers.auth0.MessagesAdapter
import lmu.msp.frontend.models.MessageModel

class InboxFragment: Fragment() {
    private var _binding: FragmentInboxBinding? = null
    private val binding get() = _binding!!
    private lateinit var listrc: RecyclerView
    private lateinit var messages: ArrayList<MessageModel>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInboxBinding.inflate(inflater, container, false)
        listrc = binding.inboxList

        messages = MessageModel.createContactsList(50)
        val adapter = MessagesAdapter({position -> onListItemClick(position) }, messages)
        listrc.adapter = adapter

        listrc.layoutManager = LinearLayoutManager(requireContext())

        Toast.makeText(requireContext(), "Testing toast", Toast.LENGTH_LONG).show()
        return binding.root
    }

    private fun onListItemClick(position: Int) {
        Log.i("POS", "Position: $position")
        Toast.makeText(requireActivity(), messages[position].content, Toast.LENGTH_LONG).show()

        val messageFragment: Fragment = MessageFragment()
        val bundle = Bundle()

        bundle.putInt("pos", position)
        messageFragment.arguments = bundle

        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentPlaceholder, messageFragment, "MESSAGE")
            .addToBackStack("MESSAGE")
            .commit()

    }



    // Needed to make changes to the list
    // adapter.notifyItemInserted(0)
    // adapter.notifyItemRangeInserted(curSize, newItems.size)
}