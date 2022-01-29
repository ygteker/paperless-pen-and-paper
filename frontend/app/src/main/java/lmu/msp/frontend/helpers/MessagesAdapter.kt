package lmu.msp.frontend.helpers

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import lmu.msp.frontend.R
import lmu.msp.frontend.api.model.Message

class MessagesAdapter(private val onItemClicked: (position: Int) -> Unit, private var messages: MutableList<Message>) :
    RecyclerView.Adapter<MessagesAdapter.ViewHolder>() {


    inner class ViewHolder(
        itemView: View,
        private val onItemClicked: (position: Int) -> Unit
    ) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val messageSender = itemView.findViewById<TextView>(R.id.from)
        val messageSummary = itemView.findViewById<TextView>(R.id.summary)
        val avatar = itemView.findViewById<ImageView>(R.id.senderAvatar)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val position = adapterPosition
            onItemClicked(position)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val messagesView = inflater.inflate(R.layout.inbox_list, parent, false)

        return ViewHolder(messagesView, onItemClicked)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message: Message = messages.get(position)
        val sender = holder.messageSender
        sender.text = message.sender.toString()

        val summary = holder.messageSummary
        //TODO display only 2 rows of the message in the summary
        summary.text = message.message

        val avatar = holder.avatar
        //TODO fetch avatar from server and add to imageview
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    fun submitList(newMessages: List<Message>) {
        messages.clear()
        messages.addAll(newMessages)
        notifyDataSetChanged()
    }

}