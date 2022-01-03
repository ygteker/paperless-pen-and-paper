package lmu.msp.frontend.ui.campaign

import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
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
        val cardButton : Button = itemView.findViewById(R.id.cardButton)
        val imageView : ShapeableImageView = itemView.findViewById(R.id.imageViewCampaign)



        init {
            cardButton.setOnClickListener{v: View -> campaignButtonClick(titleString.text)}
        }

        private fun campaignButtonClick( titleString: CharSequence) {
            Toast.makeText(itemView.context, "You clicked on item $titleString", Toast.LENGTH_SHORT).show()
        }

    }



}