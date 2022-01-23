package lmu.msp.frontend.helpers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import lmu.msp.frontend.R
import lmu.msp.frontend.models.websocket.ChatMessage

class ChatMessagesAdapter (private val messages: MutableList<ChatMessage>):
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
        val message: ChatMessage = messages.get(position)
        val sender = holder.sender
        val charBubble = holder.chatBubble
        sender.text  = message.senderId.toString()
        charBubble.text = message.message
    }

    override fun getItemCount(): Int {
        return messages.size
    }
}