package lmu.msp.frontend.ui.campaign

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import lmu.msp.frontend.R


class QRFragment : Fragment() {

    private lateinit var TextViewQRCampaignId: TextView
    private var campaignId = 0L


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_q_r, container, false)

        TextViewQRCampaignId = view.findViewById(R.id.TextViewQRCampaignId)
        campaignId = (activity as CampaignActivity).getCampaignId()

        TextViewQRCampaignId.text = campaignId.toString()

        return view
    }
}