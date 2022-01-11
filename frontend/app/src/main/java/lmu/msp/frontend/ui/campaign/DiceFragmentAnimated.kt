package lmu.msp.frontend.ui.campaign

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import lmu.msp.frontend.R
import org.w3c.dom.Text
import kotlin.random.Random

class DiceFragmentAnimated : Fragment() {

    private lateinit var d6View: ImageView
    private lateinit var resultView: TextView



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_dice_animated, container, false)

        d6View = view.findViewById(R.id.d6View)
        resultView = view.findViewById(R.id.resultView)

        d6View.setOnClickListener { rotateDice() }

        return view
    }

    private fun rotateDice() {
        var i = Random.nextInt(6) +1
        var shake = AnimationUtils.loadAnimation(context, R.anim.shake)
        d6View.startAnimation(shake)
     /*
        d6View.animate().apply {
            duration = 500
           // rotationYBy(720f)
            rotationBy(360f)
        }
      */
        when (i) {
            1 -> {
                d6View.setImageResource(R.drawable.d6_one)
                resultView.setText("Result: $i")
            }
            2 -> {
                d6View.setImageResource(R.drawable.d6_two)
                resultView.setText("Result: $i")
            }
            3 -> {
                d6View.setImageResource(R.drawable.d6_three)
                resultView.setText("Result: $i")
            }
            4 -> {
                d6View.setImageResource(R.drawable.d6_four)
                resultView.setText("Result: $i")
            }
            5 -> {
                d6View.setImageResource(R.drawable.d6_five)
                resultView.setText("Result: $i")
            }
            6 -> {
                d6View.setImageResource(R.drawable.d6_six)
                resultView.setText("Result: $i")
            }
        }
    }
}