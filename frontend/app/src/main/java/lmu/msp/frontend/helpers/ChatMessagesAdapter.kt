package lmu.msp.frontend.helpers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import lmu.msp.frontend.R
import lmu.msp.frontend.models.websocket.ChatMessage
import lmu.msp.frontend.ui.campaign.CampaignActivity

class ChatMessagesAdapter (private val messages: LiveData<MutableList<ChatMessage>>, private val activity: FragmentActivity):
    RecyclerView.Adapter<ChatMessagesAdapter.ViewHolder>(){
    inner class ViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView){
        val sender = itemView.findViewById<TextView>(R.id.sender)
        val chatBubble = itemView.findViewById<TextView>(R.id.message)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val chatView = inflater.inflate(R.layout.view_chat_message, parent, false)

        return ViewHolder(chatView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var message: ChatMessage
        val sender = holder.sender
        val chatBubble = holder.chatBubble

        messages.observe(activity, Observer {
            message = it.get(position)
            sender.text = message.senderId.toString()
            chatBubble.text = message.message
//            notifyItemChanged(position)
        })
    }

    override fun getItemCount(): Int {
        var size: Int = 0
        messages.observe(activity, Observer{
            size = it.size
        })
        return size
    }
}