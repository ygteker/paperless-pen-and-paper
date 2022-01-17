package lmu.msp.frontend.ui.campaign

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import lmu.msp.frontend.R
import lmu.msp.frontend.databinding.ActivityCampaignBinding

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

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Campaign 1"

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