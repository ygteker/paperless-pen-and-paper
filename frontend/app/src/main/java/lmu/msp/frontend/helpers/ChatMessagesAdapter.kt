package lmu.msp.frontend.helpers

import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import lmu.msp.frontend.R
import lmu.msp.frontend.api.model.GeneralChatMessage
import lmu.msp.frontend.models.websocket.ChatMessage

class ChatMessagesAdapter (private val messages: MutableLiveData<MutableList<GeneralChatMessage>>, private val activity: FragmentActivity):
    RecyclerView.Adapter<ChatMessagesAdapter.ViewHolder>(){
    inner class ViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView){
        val sender = itemView.findViewById<TextView>(R.id.senderTextView)
        val chatBubble = itemView.findViewById<TextView>(R.id.message)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val chatView = inflater.inflate(R.layout.view_chat_message, parent, false)

        return ViewHolder(chatView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var message: GeneralChatMessage
        val chatBubble = holder.chatBubble
        val senderLabel = holder.sender

        messages.observe(activity, Observer {
            message = it.get(position)
            chatBubble.text = message.message
            senderLabel.text = message.from + ": "
            if (message.self) {
                chatBubble.foregroundGravity = Gravity.END
                senderLabel.foregroundGravity = Gravity.END
            }
        })
    }

    override fun getItemCount(): Int {
        var size: Int = 0
        messages.observe(activity, Observer{
            size = it.size
        })
        return size
    }

    fun submitMessageList(newMessages: List<GeneralChatMessage>) {
        Log.i("Submit", "Element submitted")
        liveDataListClear(messages)
        liveDataListAddElementList(newMessages, messages)
        notifyDataSetChanged()
    }

    fun submitMessage(newMessage: GeneralChatMessage) {
        liveDataListClear(messages)
        liveDataListAddElement(newMessage, messages)
        notifyDataSetChanged()
    }
}