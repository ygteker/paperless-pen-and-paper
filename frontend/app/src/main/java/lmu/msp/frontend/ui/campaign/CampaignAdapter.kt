package lmu.msp.frontend.ui.campaign

import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import lmu.msp.frontend.R


class CampaignAdapter(private val campaignList: ArrayList<campaigns>) : RecyclerView.Adapter<CampaignAdapter.CampaignViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CampaignViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.campaign_view, parent, false)
        return CampaignViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CampaignViewHolder, position: Int) {
        val currentItem = campaignList[position]
        holder.titleString.text = currentItem.titleString
        holder.campaignId.text = currentItem.campaignId
    }

    override fun getItemCount(): Int {
      return campaignList.size
    }

    class CampaignViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val titleString : TextView = itemView.findViewById(R.id.title_campaign)
        val campaignId : TextView = itemView.findViewById(R.id.id_campaign)

    }
}