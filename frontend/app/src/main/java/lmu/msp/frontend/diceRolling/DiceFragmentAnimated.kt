package lmu.msp.frontend.diceRolling

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import lmu.msp.frontend.R
import kotlin.random.Random

/**
 * In this Fragment all dice rolling logic is implemented for animated dice rolling
 * @author Valentin Scheibe
 */
class DiceFragmentAnimated : Fragment() {

    private lateinit var addButtonD4: Button
    private lateinit var addButtonD6: Button
    private lateinit var addButtonD8: Button
    private lateinit var addButtonD10: Button
    private lateinit var addButtonD12: Button
    private lateinit var addButtonD20: Button
    private lateinit var rollAllDice: Button
    private lateinit var removeAllDice: Button

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
        removeAllDice.setOnClickListener { removeAllDiceFunction() }
    }

    /**
     * Clears all dice from the recyclerView, and the corresponding fields
     */
    private fun removeAllDiceFunction() {
        diceArrayList.clear()
        resultsArray.clear()
        resultTextView.text = "Result: "
        diceRollingAdapter.notifyDataSetChanged()
    }

    /**
     * plays an animation on all view items in the recycler view
     */
    private fun animateDice() {
        val shake = AnimationUtils.loadAnimation(context, R.anim.shake3)
        var i = 0
        while (i <= diceArrayList.size) {
            rollDiceRecyclerView.findViewHolderForAdapterPosition(i)?.itemView?.startAnimation(shake)
            i++
        }
    }

    /**
     * Handles the all dice rolling functions such as animation, random number generation and image switching
     */
    private fun rollAllDiceFunction() {
        if (diceArrayList.size != 0) {
            resultsArray.clear()
            animateDice()
            var i = 0
            while (i < diceArrayList.size) {
                when (diceArrayList[i].sides) {
                    4 -> {
                        val random = Random.nextInt(4) + 1
                        resultsArray.add(random)
                        changePicture(4, random, i)
                    }
                    6 -> {
                        val random = Random.nextInt(6) + 1
                        resultsArray.add(random)
                        changePicture(6, random, i)
                    }
                    8 -> {
                        val random = Random.nextInt(8) + 1
                        resultsArray.add(random)
                        changePicture(8, random, i)
                    }
                    10 -> {
                        val random = Random.nextInt(10) + 1
                        resultsArray.add(random)
                        changePicture(10, random, i)
                    }
                    12 -> {
                        val random = Random.nextInt(12) + 1
                        resultsArray.add(random)
                        changePicture(12, random, i)
                    }
                    20 -> {
                        val random = Random.nextInt(20) + 1
                        resultsArray.add(random)
                        changePicture(20, random, i)
                    }
                }
                i++
            }
            displayResult()
        } else {
            Toast.makeText(context, "Please add dice before rolling first!", Toast.LENGTH_SHORT)
                .show()
        }
    }

    /**
     * Changes the image asset of an item in the recyclerView according to the random number rolled and the amount of sides the item has
     * @param sides amount of sides the displayed item(a dice) has
     * @param random the random number that determines the new side of the dice to be displayed
     * @param index the index of the item that is getting changed in the recycler view
     */
    private fun changePicture(sides: Int, random: Int, index: Int) {
        when (sides) {
            4 -> {
                when (random) {
                    1 -> diceArrayList[index].imageId = R.drawable.d4_1
                    2 -> diceArrayList[index].imageId = R.drawable.d4_2
                    3 -> diceArrayList[index].imageId = R.drawable.d4_3
                    4 -> diceArrayList[index].imageId = R.drawable.d4_4
                }
            }
            6 -> {
                when (random) {
                    1 -> diceArrayList[index].imageId = R.drawable.d6_1
                    2 -> diceArrayList[index].imageId = R.drawable.d6_2
                    3 -> diceArrayList[index].imageId = R.drawable.d6_3
                    4 -> diceArrayList[index].imageId = R.drawable.d6_4
                    5 -> diceArrayList[index].imageId = R.drawable.d6_5
                    6 -> diceArrayList[index].imageId = R.drawable.d6_6
                }
            }
            8 -> {
                when (random) {
                    1 -> diceArrayList[index].imageId = R.drawable.d8_1
                    2 -> diceArrayList[index].imageId = R.drawable.d8_2
                    3 -> diceArrayList[index].imageId = R.drawable.d8_3
                    4 -> diceArrayList[index].imageId = R.drawable.d8_4
                    5 -> diceArrayList[index].imageId = R.drawable.d8_5
                    6 -> diceArrayList[index].imageId = R.drawable.d8_6
                    7 -> diceArrayList[index].imageId = R.drawable.d8_7
                    8 -> diceArrayList[index].imageId = R.drawable.d8_8
                }
            }
            10 -> {
                when (random) {
                    1 -> diceArrayList[index].imageId = R.drawable.d10_1
                    2 -> diceArrayList[index].imageId = R.drawable.d10_2
                    3 -> diceArrayList[index].imageId = R.drawable.d10_3
                    4 -> diceArrayList[index].imageId = R.drawable.d10_4
                    5 -> diceArrayList[index].imageId = R.drawable.d10_5
                    6 -> diceArrayList[index].imageId = R.drawable.d10_6
                    7 -> diceArrayList[index].imageId = R.drawable.d10_7
                    8 -> diceArrayList[index].imageId = R.drawable.d10_8
                    9 -> diceArrayList[index].imageId = R.drawable.d10_9
                    10 -> diceArrayList[index].imageId = R.drawable.d10_10
                }
            }
            12 -> {
                when (random) {
                    1 -> diceArrayList[index].imageId = R.drawable.d12_1
                    2 -> diceArrayList[index].imageId = R.drawable.d12_2
                    3 -> diceArrayList[index].imageId = R.drawable.d12_3
                    4 -> diceArrayList[index].imageId = R.drawable.d12_4
                    5 -> diceArrayList[index].imageId = R.drawable.d12_5
                    6 -> diceArrayList[index].imageId = R.drawable.d12_6
                    7 -> diceArrayList[index].imageId = R.drawable.d12_7
                    8 -> diceArrayList[index].imageId = R.drawable.d12_8
                    9 -> diceArrayList[index].imageId = R.drawable.d12_9
                    10 -> diceArrayList[index].imageId = R.drawable.d12_10
                    11 -> diceArrayList[index].imageId = R.drawable.d12_11
                    12 -> diceArrayList[index].imageId = R.drawable.d12_12
                }
            }
            20 -> {
                when (random) {
                    1 -> diceArrayList[index].imageId = R.drawable.d20_1
                    2 -> diceArrayList[index].imageId = R.drawable.d20_2
                    3 -> diceArrayList[index].imageId = R.drawable.d20_3
                    4 -> diceArrayList[index].imageId = R.drawable.d20_4
                    5 -> diceArrayList[index].imageId = R.drawable.d20_5
                    6 -> diceArrayList[index].imageId = R.drawable.d20_6
                    7 -> diceArrayList[index].imageId = R.drawable.d20_7
                    8 -> diceArrayList[index].imageId = R.drawable.d20_8
                    9 -> diceArrayList[index].imageId = R.drawable.d20_9
                    10 -> diceArrayList[index].imageId = R.drawable.d20_10
                    11 -> diceArrayList[index].imageId = R.drawable.d20_11
                    12 -> diceArrayList[index].imageId = R.drawable.d20_12
                    13 -> diceArrayList[index].imageId = R.drawable.d20_13
                    14 -> diceArrayList[index].imageId = R.drawable.d20_14
                    15 -> diceArrayList[index].imageId = R.drawable.d20_15
                    16 -> diceArrayList[index].imageId = R.drawable.d20_16
                    17 -> diceArrayList[index].imageId = R.drawable.d20_17
                    18 -> diceArrayList[index].imageId = R.drawable.d20_18
                    19 -> diceArrayList[index].imageId = R.drawable.d20_19
                    20 -> diceArrayList[index].imageId = R.drawable.d20_20
                }
            }

        }
        diceRollingAdapter.notifyItemChanged(index)
    }

    /**
     * displays the result of a roll in the resultTextView
     */
    private fun displayResult() {
        val output: String = resultsArray.joinToString(separator = ", ")
        var added = 0
        val size = resultsArray.size
        var i = 0
        while (i < size) {
            added += resultsArray[i]
            i++
        }
        val output2 = "Result: $output, Added: $added"
        resultTextView.text = output2
    }

    /**
     * adds an item(dice) to the recyclerView that contains dice.
     * @param sides indicates how many sides a dice should have that gets added to the view to be able to be rolled
     */
    @SuppressLint("NotifyDataSetChanged")
    private fun addDice(sides: Int) {
        if (diceArrayList.size >= 40) {
            Toast.makeText(context, "Cannot add any more dice", Toast.LENGTH_SHORT).show()
        } else {
            val data: diceData
            when (sides) {
                4 -> {
                    data = diceData(sides, R.drawable.d4_all)
                    diceArrayList.add(data)
                }
                6 -> {
                    data = diceData(sides, R.drawable.d6_1)
                    diceArrayList.add(data)
                }
                8 -> {
                    data = diceData(sides, R.drawable.d8_all)
                    diceArrayList.add(data)

                }
                10 -> {
                    data = diceData(sides, R.drawable.d10_all)
                    diceArrayList.add(data)
                }
                12 -> {
                    data = diceData(sides, R.drawable.d12_all)
                    diceArrayList.add(data)
                }
                20 -> {
                    data = diceData(sides, R.drawable.d20_all)
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
        removeAllDice = view.findViewById(R.id.removeAllDiceButton)

        resultTextView = view.findViewById(R.id.resultTextView)

        rollDiceRecyclerView = view.findViewById(R.id.rollDiceRecyclerView)
        diceArrayList = arrayListOf<diceData>()
        resultsArray = arrayListOf<Int>()
        diceRollingAdapter = DiceRollingAdapter(diceArrayList)
        rollDiceRecyclerView.adapter = diceRollingAdapter
    }

}