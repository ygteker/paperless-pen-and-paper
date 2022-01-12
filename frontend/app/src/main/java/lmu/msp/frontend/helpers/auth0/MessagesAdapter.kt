package lmu.msp.frontend.helpers.auth0

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import lmu.msp.frontend.R
import lmu.msp.frontend.models.MessageModel

class MessagesAdapter(private val messages: ArrayList<MessageModel>):
    RecyclerView.Adapter<MessagesAdapter.ViewHolder>() {

    inner class ViewHolder(
        itemView: View,
        private val onItemClicked: (position: Int) -> Unit
    ): RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var onItemClick: ((MessageModel) -> Unit)? = null
        val messageTitle = itemView.findViewById<TextView>(R.id.from)
        val messageSummary = itemView.findViewById<TextView>(R.id.summary)
        val avatar = itemView.findViewById<ImageView>(R.id.senderAvatar)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            onItemClicked(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessagesAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val messagesView = inflater.inflate(R.layout.inbox_list, parent, false)

        return ViewHolder(messagesView, 0)
    }

    override fun onBindViewHolder(holder: MessagesAdapter.ViewHolder, position: Int) {
        val message: MessageModel = messages.get(position)
        val title = holder.messageTitle
        title.text = message.title

        val summary = holder.messageSummary
        //TODO display only 2 rows of the message in the summary
        summary.text = message.content

        val avatar = holder.avatar
        avatar.setImageResource(R.drawable.logo)
        //TODO fetch avatar from server and add to imageview
    }

    override fun getItemCount(): Int {
        return messages.size
    }

}