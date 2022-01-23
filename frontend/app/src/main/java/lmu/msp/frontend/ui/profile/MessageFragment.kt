package lmu.msp.frontend.ui.profile

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import lmu.msp.frontend.R
import lmu.msp.frontend.databinding.FragmentMessageBinding
import lmu.msp.frontend.helpers.TokenManager
import lmu.msp.frontend.models.MessageModel
import lmu.msp.frontend.viewmodels.MessagesViewModel

class MessageFragment: Fragment() {

    private var _binding: FragmentMessageBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MessagesViewModel
    private lateinit var tokenManager: TokenManager
    private var messageModel: MessageModel? = null


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_with_delete_button, menu)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(requireActivity()).get(MessagesViewModel::class.java)
        tokenManager = activity?.applicationContext?.let { TokenManager(it) }!!
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

        _binding = FragmentMessageBinding.inflate(inflater, container, false)

        val pos: Int? = arguments?.getInt("pos")
        messageModel = arguments?.getParcelable<MessageModel>("messageModel")
        binding.messageText.text = pos.toString() + "\n" + messageModel?.content + "Message id: " + messageModel?.id

        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.deleteButton -> {
                Log.i("deleteButtonTrigger", "Delete button clicked!!!")
                viewModel.deleteMessage("Bearer " + tokenManager.load(), messageModel!!)
                requireActivity().supportFragmentManager.popBackStack()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}