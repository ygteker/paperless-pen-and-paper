package lmu.msp.frontend.ui.profile

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import lmu.msp.frontend.R
import lmu.msp.frontend.api.PenAndPaperApiInterface
import lmu.msp.frontend.api.model.Message
import lmu.msp.frontend.databinding.FragmentInboxBinding
import lmu.msp.frontend.helpers.TokenManager
import lmu.msp.frontend.helpers.MessagesAdapter
import lmu.msp.frontend.helpers.retrofit.RetrofitProvider
import lmu.msp.frontend.viewmodels.MessagesViewModel
import okhttp3.internal.notify

class InboxFragment: Fragment() {
    private lateinit var viewManager: LinearLayoutManager
    private var _binding: FragmentInboxBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: MessagesViewModel
    private lateinit var tokenManager: TokenManager
    private lateinit var messageApi: PenAndPaperApiInterface.MessageApi
    private lateinit var messagesAdapter: MessagesAdapter

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_with_add_button, menu)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        tokenManager = activity?.applicationContext?.let { TokenManager(it) }!!
        viewModel = ViewModelProvider(requireActivity()).get(MessagesViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInboxBinding.inflate(inflater, container, false)
        viewManager = LinearLayoutManager(requireActivity())
        recyclerView = binding.inboxList
        messageApi = RetrofitProvider(requireContext()).getMessageApi()
        messageApi.getMessages()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                Log.e("error", "error ${it.message}")
            }
            .doOnSuccess {
                messagesAdapter = MessagesAdapter({position -> onListItemClick(position, it[position]) }, it.toMutableList())
                recyclerView.adapter = messagesAdapter
                Log.i("list",   "size: ${it.size} ${it.toString()}")
                viewModel.clearList()
                viewModel.add(it)
            }
            .subscribe()


        initialiseAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.title_messages)
    }

    private fun onListItemClick(position: Int, message: Message) {

        val messageFragment: Fragment = MessageFragment()
        val bundle = Bundle()

        bundle.putInt("pos", position)
        bundle.putLong("id", message.id!!)
        bundle.putLong("sender", message.sender!!)
        bundle.putString("message", message.message)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.addButton -> {
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentPlaceholder, ComposeFragment(), "COMPOSE")
                    .addToBackStack("COMPOSE")
                    .commit()

                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}