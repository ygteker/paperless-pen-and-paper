package lmu.msp.frontend.ui.profile

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import lmu.msp.frontend.R
import lmu.msp.frontend.databinding.FragmentInboxBinding
import lmu.msp.frontend.helpers.auth0.MessagesAdapter
import lmu.msp.frontend.models.MessageModel
import lmu.msp.frontend.viewmodels.MessagesViewModel

class InboxFragment: Fragment() {
    private lateinit var viewManager: LinearLayoutManager
    private var _binding: FragmentInboxBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: MessagesViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(requireActivity()).get(MessagesViewModel::class.java)
        viewModel.getMessages("Bearer " + (activity?.intent?.extras?.get("access_token")))

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInboxBinding.inflate(inflater, container, false)
        viewManager = LinearLayoutManager(requireActivity())
        recyclerView = binding.inboxList

//        addDummyData()
        initialiseAdapter()
        observeData()
        return binding.root
    }

    private fun onListItemClick(position: Int, messageModel: MessageModel) {

        val messageFragment: Fragment = MessageFragment()
        val bundle = Bundle()

        bundle.putInt("pos", position)
//        bundle.putString("message", messageModel.content)
        bundle.putParcelable("messageModel", messageModel)
        messageFragment.arguments = bundle

        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentPlaceholder, messageFragment, "MESSAGE")
            .addToBackStack("MESSAGE")
            .commit()

    }

    private fun initialiseAdapter() {
        recyclerView.layoutManager = viewManager
    }

    private fun observeData() {
        viewModel.lst.observe(requireActivity(), Observer {
            recyclerView.adapter = MessagesAdapter({position -> onListItemClick(position, it[position]) }, it)
        })
    }

    private fun addDummyData() {
        for (i in 1..2) {
            val mes = MessageModel(1,  "Player B", "Sample message", "Sample Content", false)
            viewModel.add(mes)
        }
    }



    // Needed to make changes to the list
    // adapter.notifyItemInserted(0)
    // adapter.notifyItemRangeInserted(curSize, newItems.size)
}