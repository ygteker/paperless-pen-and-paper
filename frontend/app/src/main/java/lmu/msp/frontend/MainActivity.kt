package lmu.msp.frontend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent




class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        homeActivityButtonOnClick();
    }
    private fun homeActivityButtonOnClick(){
        val homeActivityButton =  findViewById<Button>(R.id.homeActivity);
        homeActivityButton.setOnClickListener(){
            val myIntent = Intent(this, HomeActivity::class.java)
            startActivity(myIntent)
        }
    }
}