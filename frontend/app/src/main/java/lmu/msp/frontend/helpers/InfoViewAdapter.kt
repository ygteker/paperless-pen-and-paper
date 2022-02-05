package lmu.msp.frontend.helpers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import lmu.msp.frontend.R
import lmu.msp.frontend.api.model.GeneralChatMessage
import lmu.msp.frontend.ui.profile.InfoElement

class InfoViewAdapter(private val infos: List<InfoElement>): RecyclerView.Adapter<InfoViewAdapter.ViewHolder>(){
    inner class ViewHolder(
        itemView: View
    )  : RecyclerView.ViewHolder(itemView){
        val infoTitle = itemView.findViewById<TextView>(R.id.infoTitle)
        val infoContent = itemView.findViewById<TextView>(R.id.infoContent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val infoView = inflater.inflate(R.layout.view_info, parent, false)
        return ViewHolder(infoView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val info = infos[position]
        val title = holder.infoTitle
        val content = holder.infoContent
        title.text = info.title
        content.text = info.content
    }

    override fun getItemCount(): Int {
        return infos.size
    }

}