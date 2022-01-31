package lmu.msp.frontend.diceRolling

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import lmu.msp.frontend.R

/**
 * @author Valentin Scheibe
 */
class DiceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dice)

        if (savedInstanceState == null) { // initial transaction should be wrapped like this
            supportFragmentManager.beginTransaction()
                .replace(R.id.root_container, DiceFragmentAnimated())
                .commitAllowingStateLoss()
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Roll Dice"
    }
}