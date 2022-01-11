package lmu.msp.frontend.diceRolling

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import lmu.msp.frontend.R
import kotlin.random.Random

class DiceFragmentAnimated : Fragment() {

    private lateinit var addButtonD4: Button
    private lateinit var addButtonD6: Button
    private lateinit var addButtonD8: Button
    private lateinit var addButtonD10: Button
    private lateinit var addButtonD12: Button
    private lateinit var addButtonD20: Button
    private lateinit var rollAllDice: Button
    private lateinit var testButton: Button

    private lateinit var resultTextView: TextView

    private lateinit var rollDiceRecyclerView: RecyclerView
    private lateinit var diceRollingAdapter: DiceRollingAdapter
    private lateinit var diceArrayList: ArrayList<diceData>
    private lateinit var resultsArray: ArrayList<Int>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_dice_animated, container, false)
        findViews(view)

        val gridLayoutManager = GridLayoutManager(context, 4)
        rollDiceRecyclerView.layoutManager = gridLayoutManager

        setOnclickListeners()

        return view
    }

    private fun setOnclickListeners() {
        addButtonD4.setOnClickListener { addDice(4) }
        addButtonD6.setOnClickListener { addDice(6) }
        addButtonD8.setOnClickListener { addDice(8) }
        addButtonD10.setOnClickListener { addDice(10) }
        addButtonD12.setOnClickListener { addDice(12) }
        addButtonD20.setOnClickListener { addDice(20) }
        rollAllDice.setOnClickListener { rollAllDiceFunction() }
        testButton.setOnClickListener { animateDice() }

    }

    private fun animateDice() {

        var shake = AnimationUtils.loadAnimation(context, R.anim.shake3)
        var i = 0
        while (i <= diceArrayList.size) {
            rollDiceRecyclerView.findViewHolderForAdapterPosition(i)?.itemView?.startAnimation(shake)
            i++
        }
    }

    private fun rollAllDiceFunction() {
        resultsArray = arrayListOf<Int>()
        animateDice()
        var i = 0
        while (i < diceArrayList.size) {
            when (diceArrayList.get(i).sides) {
                4 -> {
                    resultsArray.add(Random.nextInt(4) + 1)
                }
                6 -> {
                    val random = Random.nextInt(6) + 1
                    resultsArray.add(random)

                    changePicture(6, random, i)
                }
                8 -> {
                    resultsArray.add(Random.nextInt(8) + 1)
                }
                10 -> {
                    resultsArray.add(Random.nextInt(10) + 1)
                }
                12 -> {
                    resultsArray.add(Random.nextInt(12) + 1)
                }
                20 -> {
                    resultsArray.add(Random.nextInt(20) + 1)
                }
            }
            i++
        }

        displayResult()
    }

    private fun changePicture(sides: Int, random: Int, index: Int) {

        when (sides) {
            6 -> {
                when (random) {
                    1 -> diceArrayList.get(index).imageId = R.drawable.d6_one
                    2 -> diceArrayList.get(index).imageId = R.drawable.d6_two
                    3 -> diceArrayList.get(index).imageId = R.drawable.d6_three
                    4 -> diceArrayList.get(index).imageId = R.drawable.d6_four
                    5 -> diceArrayList.get(index).imageId = R.drawable.d6_five
                    6 -> diceArrayList.get(index).imageId = R.drawable.d6_six
                }

            }

        }
          diceRollingAdapter.notifyItemChanged(index)
      //  diceRollingAdapter.notifyDataSetChanged()

    }

    private fun displayResult() {
        val output: String = resultsArray.joinToString(separator = ", ")
        var added = 0
        val size = resultsArray.size
        var i = 0
        while (i < size) {
            added += resultsArray[i]
            i++
        }
        val output2 = "Result: " + output + ", Added: " + added.toString()
        resultTextView.setText(output2)
    }

    private fun addDice(sides: Int) {
        if (diceArrayList.size >= 28) {
            Toast.makeText(context, "Cannot add any more dice", Toast.LENGTH_SHORT).show()
        } else {
            val data: diceData
            when (sides) {
                4 -> {
                    data = diceData(sides, R.drawable.d6_two)
                    diceArrayList.add(data)
                }
                6 -> {
                    data = diceData(sides, R.drawable.d6_one)
                    diceArrayList.add(data)
                }
                8 -> {
                    data = diceData(sides, R.drawable.d6_three)
                    diceArrayList.add(data)

                }
                10 -> {
                    data = diceData(sides, R.drawable.d6_four)
                    diceArrayList.add(data)
                }
                12 -> {
                    data = diceData(sides, R.drawable.d6_five)
                    diceArrayList.add(data)
                }
                20 -> {
                    data = diceData(sides, R.drawable.d6_six)
                    diceArrayList.add(data)
                }
            }
            diceRollingAdapter.notifyDataSetChanged()
        }
    }

    private fun findViews(view: View) {
        addButtonD4 = view.findViewById(R.id.addButtonD4)
        addButtonD6 = view.findViewById(R.id.addButtonD6)
        addButtonD8 = view.findViewById(R.id.addButtonD8)
        addButtonD10 = view.findViewById(R.id.addButtonD10)
        addButtonD12 = view.findViewById(R.id.addButtonD12)
        addButtonD20 = view.findViewById(R.id.addButtonD20)
        rollAllDice = view.findViewById(R.id.rollDiceButtonAll)
        testButton = view.findViewById(R.id.testButton)

        resultTextView = view.findViewById(R.id.resultTextView)

        rollDiceRecyclerView = view.findViewById(R.id.rollDiceRecyclerView)
        diceArrayList = arrayListOf<diceData>()
        diceRollingAdapter = DiceRollingAdapter(diceArrayList)
        rollDiceRecyclerView.adapter = diceRollingAdapter
    }


/*
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

 */

}