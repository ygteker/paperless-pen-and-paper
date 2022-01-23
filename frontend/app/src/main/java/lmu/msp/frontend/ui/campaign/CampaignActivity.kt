package lmu.msp.frontend.ui.campaign

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import lmu.msp.frontend.R
import lmu.msp.frontend.databinding.ActivityCampaignBinding
import lmu.msp.frontend.viewmodels.WebSocketDataViewModel

class CampaignActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCampaignBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCampaignBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment, ToolsFragment(), "tools")
            commit()
        }

        val intent = intent
        var campaignIdFromIntent = intent.getSerializableExtra("campaignId")
        var campaignId = campaignIdFromIntent.toString().toLong()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Campaign 1"


        val viewModel = ViewModelProvider(this).get(WebSocketDataViewModel::class.java)


        viewModel.startWebSocket(campaignId)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                val currentFragment = supportFragmentManager.findFragmentByTag("tools")
                if (currentFragment != null && currentFragment.isVisible) {
                    finish()
                } else {
                    supportFragmentManager.popBackStack()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

}