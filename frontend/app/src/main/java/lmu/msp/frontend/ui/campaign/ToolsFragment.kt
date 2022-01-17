package lmu.msp.frontend.ui.campaign

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import lmu.msp.frontend.R
import lmu.msp.frontend.databinding.FragmentToolsBinding
import lmu.msp.frontend.diceRolling.DiceFragmentAnimated
import lmu.msp.frontend.helpers.websockets.IRxWebSocket
import lmu.msp.frontend.helpers.websockets.WebSocketProvider
import lmu.msp.frontend.models.websocket.BasicMessage
import lmu.msp.frontend.models.websocket.ChatMessage
import lmu.msp.frontend.models.websocket.MessageType


class ToolsFragment : Fragment() {

    companion object {
        private const val TAG = "ToolsFragment"
    }

    private var _binding: FragmentToolsBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentToolsBinding.inflate(inflater, container, false)
        val view = binding.root
        val toolsList = binding.toolsList
        val floatingActionButton = binding.inviteFriendFAB
        val arrayList = ArrayList<String>()
        arrayList.add("Map")
        arrayList.add("Roll Dice")
        arrayList.add("Chat")
        arrayList.add("Roll animated Dice")
        val arrayAdapter =
            ArrayAdapter(view.context, android.R.layout.simple_list_item_1, arrayList)
        toolsList.adapter = arrayAdapter
        toolsList.setOnItemClickListener { _, view, position, id ->
            val fragmentManager = parentFragmentManager.beginTransaction()
            when (position) {
                0 -> fragmentManager.replace(R.id.fragment, MapFragment()).addToBackStack(null)
                1 -> fragmentManager.replace(R.id.fragment, DiceFragment()).addToBackStack(null)
                2 -> fragmentManager.replace(R.id.fragment, ChatFragment()).addToBackStack(null)
                3 -> fragmentManager.replace(R.id.fragment, DiceFragmentAnimated())
                    .addToBackStack(null)
            }
            fragmentManager.commit()
        }
        val rxWebSocket = WebSocketProvider(requireContext()).start(1)
        demoWebSocketConnection(rxWebSocket)
        floatingActionButton.setOnClickListener {
            rxWebSocket.send(
                Gson().toJson(
                    BasicMessage(
                        MessageType.CHAT_MESSAGE, listOf(
                            ChatMessage("msg", 1, 1)
                        ), null
                    )

                )
            )
            val fragmentManager = parentFragmentManager.beginTransaction()
            fragmentManager.replace(R.id.fragment, QRFragment()).addToBackStack(null)
            fragmentManager.commit()
        }
        return view
    }

    private fun demoWebSocketConnection(rxWebSocket: IRxWebSocket) {

        rxWebSocket.getObserver()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                Log.e(TAG, "error ${it.message}")
                //TODO ERROR HANDLING
            }
            .doOnNext {
                val basicMessage = Gson().fromJson(it, BasicMessage::class.java)
                when (basicMessage.messageType) {
                    MessageType.DRAW_PATH -> Log.i(TAG, "points")
                    MessageType.CONNECT -> Log.i(TAG, "connected")
                    MessageType.DISCONNECT -> TODO()
                    MessageType.CHAT_MESSAGE -> Log.i(TAG, "chatmsg")
                    MessageType.DRAW_IMAGE -> TODO()
                    MessageType.DRAW_RESET -> TODO()
                }

                Log.i(TAG, basicMessage.messageType.name)
            }.subscribe()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}