package lmu.msp.frontend.ui.profile

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import lmu.msp.frontend.R
import lmu.msp.frontend.databinding.ActivityMessagesBinding

class MessagesActivity: AppCompatActivity(){

    private lateinit var binding: ActivityMessagesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessagesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()

        ft.replace(R.id.fragmentPlaceholder, InboxFragment())
        ft.commit()

    }
}