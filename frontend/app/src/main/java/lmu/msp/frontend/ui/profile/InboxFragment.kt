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
import lmu.msp.frontend.helpers.TokenManager
import lmu.msp.frontend.helpers.auth0.MessagesAdapter
import lmu.msp.frontend.models.MessageModel
import lmu.msp.frontend.viewmodels.MessagesViewModel

class InboxFragment: Fragment() {
    private lateinit var viewManager: LinearLayoutManager
    private var _binding: FragmentInboxBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: MessagesViewModel
    private lateinit var tokenManager: TokenManager

    override fun onAttach(context: Context) {
        super.onAttach(context)
        tokenManager = activity?.applicationContext?.let { TokenManager(it) }!!
        viewModel = ViewModelProvider(requireActivity()).get(MessagesViewModel::class.java)
        viewModel.getMessages("Bearer " + tokenManager.load())

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInboxBinding.inflate(inflater, container, false)
        viewManager = LinearLayoutManager(requireActivity())
        recyclerView = binding.inboxList

        initialiseAdapter()
        observeData()
        return binding.root
    }

    private fun onListItemClick(position: Int, messageModel: MessageModel) {

        val messageFragment: Fragment = MessageFragment()
        val bundle = Bundle()

        bundle.putInt("pos", position)
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

}