package lmu.msp.frontend.ui.campaign

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import lmu.msp.frontend.R

class CampaignActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_campaign)



        val toolsFragment = ToolsFragment()
        val chatFragment = ChatFragment()

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment, toolsFragment)
            commit()
        }


        // calling the action bar

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Campaign 1"

    }
    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onContextItemSelected(item)
    }
}