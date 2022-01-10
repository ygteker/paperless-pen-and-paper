package lmu.msp.frontend.ui.campaign

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import lmu.msp.frontend.R


class CampaignFragment : Fragment() {

    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArrayList: ArrayList<campaigns>
    private lateinit var titleStrings: Array<String>
    private lateinit var campaignIds: Array<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        titleStrings = arrayOf(
            "Curse of Strahd",
            "A wild sheep chase",
            "Mines of Phandelver"
        )
        campaignIds = arrayOf(
            "000001",
            "000002",
            "000003"
        )
        val view = inflater.inflate(R.layout.fragment_campaign, container, false)

        newRecyclerView = view.findViewById(R.id.recyclerView)
        val horizontalLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        newRecyclerView.layoutManager = horizontalLayoutManager
        newRecyclerView.setHasFixedSize(true)

        val helper = LinearSnapHelper()
        helper.attachToRecyclerView(newRecyclerView)

        newArrayList = arrayListOf<campaigns>()
        fillCampaigns()

        return view
    }

    private fun fillCampaigns() {

        for(i in titleStrings.indices){
            val data = campaigns(titleStrings[i], campaignIds[i])
            newArrayList.add(data)
        }
        newRecyclerView.adapter = CampaignAdapter(newArrayList)
    }
}