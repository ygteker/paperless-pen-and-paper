package lmu.msp.frontend.ui.campaign

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import lmu.msp.frontend.R

/**
 * This class contains the adapter for the recycler view used in the campaignFragment
 * @author Valentin Scheibe
 */
class CampaignAdapter(private val campaignList: ArrayList<campaigns>) :
    RecyclerView.Adapter<CampaignAdapter.CampaignViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CampaignViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.campaign_view, parent, false)
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

    /**
     * This class contains the view holder for the adapter defined above
     */
    class CampaignViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleString: TextView = itemView.findViewById(R.id.title_campaign)
        val campaignId: TextView = itemView.findViewById(R.id.id_campaign)
        val cardButton: Button = itemView.findViewById(R.id.cardButton)
        val imageView: ShapeableImageView = itemView.findViewById(R.id.imageViewCampaign)


        init {
            cardButton.setOnClickListener { v: View ->
                campaignButtonClick(
                    v,
                    campaignId.text.toString(),
                    titleString.text.toString()
                )
            }
        }

        /**
         * This handles the "start" button click on a card in the campaignFragment recycler view.
         * opens the campaignActivity
         * @param campaignId campaign id of the campaign that gets started
         * @param titleString this is the title of the campaign that is started
         */
        private fun campaignButtonClick(v: View, campaignId: String, titleString: String) {
            val intent = Intent(v.context, CampaignActivity::class.java)
            intent.putExtra("campaignId", campaignId)
            intent.putExtra("titleString", titleString)
            v.context.startActivity(intent)
        }

    }


}