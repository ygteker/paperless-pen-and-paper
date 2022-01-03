package lmu.msp.frontend.ui.campaign

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import lmu.msp.frontend.R
import lmu.msp.frontend.databinding.FragmentCampaignBinding

class CampaignFragment : Fragment() {

    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArrayList: ArrayList<campaigns>
    private lateinit var titleStrings : Array<String>
    private lateinit var campaignIds : Array<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_campaign, container, false)
    }
}