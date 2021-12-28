package lmu.msp.frontend.ui.campaign

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.FragmentManager
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
        binding.floatingActionButton.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment, QRFragment()).addToBackStack(null)
                commit()
            }
        }



        /*
        val listview = findViewById<ListView>(R.id.tools_list);
        val arrayList = ArrayList<String>()
        arrayList.add("1")
        arrayList.add("2")
        var arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList)
        listview.adapter = arrayAdapter
        */


        // calling the action bar

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Campaign 1"

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                val currentFragment = supportFragmentManager.findFragmentByTag("tools")
                if(currentFragment != null && currentFragment.isVisible){
                    finish()
                } else {
                supportFragmentManager.popBackStack()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

}