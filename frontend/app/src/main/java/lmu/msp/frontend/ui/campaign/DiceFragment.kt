package lmu.msp.frontend.ui.campaign

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import lmu.msp.frontend.R
import android.util.Log
import android.view.inputmethod.InputMethodManager
import kotlin.random.Random


class DiceFragment : Fragment() {
    private lateinit var rollButtonD20: Button
    private lateinit var resultTextD20: TextView
    private lateinit var amountEditTextD20: EditText
    private lateinit var rollButtonD12: Button
    private lateinit var resultTextD12: TextView
    private lateinit var amountEditTextD12: EditText
    private lateinit var rollButtonD10: Button
    private lateinit var resultTextD10: TextView
    private lateinit var amountEditTextD10: EditText
    private lateinit var rollButtonD8: Button
    private lateinit var resultTextD8: TextView
    private lateinit var amountEditTextD8: EditText
    private lateinit var rollButtonD6: Button
    private lateinit var resultTextD6: TextView
    private lateinit var amountEditTextD6: EditText
    private lateinit var rollButtonD4: Button
    private lateinit var resultTextD4: TextView
    private lateinit var amountEditTextD4: EditText
    private lateinit var rolls: ArrayList<Int>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_dice, container, false)

        findViews(view)

        rollButtonD20.setOnClickListener {
            rollDice(20)
        }
        rollButtonD12.setOnClickListener {
            rollDice(12)
        }
        rollButtonD10.setOnClickListener {
            rollDice(10)
        }
        rollButtonD8.setOnClickListener {
            rollDice(8)
        }
        rollButtonD6.setOnClickListener {
            rollDice(6)
        }
        rollButtonD4.setOnClickListener {
            rollDice(4)
        }

        return view
    }

    private fun findViews(view: View) {
        rollButtonD20 = view.findViewById(R.id.rollDiceButtonD20)
        amountEditTextD20 = view.findViewById(R.id.amountD20)
        resultTextD20 = view.findViewById(R.id.resultD20)
        rollButtonD12 = view.findViewById(R.id.rollDiceButtonD12)
        amountEditTextD12 = view.findViewById(R.id.amountD12)
        resultTextD12 = view.findViewById(R.id.resultD12)
        rollButtonD10 = view.findViewById(R.id.rollDiceButtonD10)
        amountEditTextD10 = view.findViewById(R.id.amountD10)
        resultTextD10 = view.findViewById(R.id.resultD10)
        rollButtonD8 = view.findViewById(R.id.rollDiceButtonD8)
        amountEditTextD8 = view.findViewById(R.id.amountD8)
        resultTextD8 = view.findViewById(R.id.resultD8)
        rollButtonD6 = view.findViewById(R.id.rollDiceButtonD6)
        amountEditTextD6 = view.findViewById(R.id.amountD6)
        resultTextD6 = view.findViewById(R.id.resultD6)
        rollButtonD4 = view.findViewById(R.id.rollDiceButtonD4)
        amountEditTextD4 = view.findViewById(R.id.amountD4)
        resultTextD4 = view.findViewById(R.id.resultD4)
    }

    private fun rollDice(sides: Int) {
        when (sides) {
            20 -> roll20SidedDice()
            12 -> roll12SidedDice()
            10 -> roll10SidedDice()
            8 -> roll8SidedDice()
            6 -> roll6SidedDice()
            4 -> roll4SidedDice()
        }
    }

    private fun roll20SidedDice() {
        Log.d("Tag", "Roll D20")
        rolls = arrayListOf<Int>()
        if (amountEditTextD20.text.toString().isNullOrBlank()) {
            Toast.makeText(activity, "Empty field not allowed!", Toast.LENGTH_SHORT).show()
            Log.d("Tag", "Empty field not allowed!")
        } else {
            val amountOfDice = amountEditTextD20.text.toString().toInt()
            fillRolls(amountOfDice, 20)
            setResultText(20)
        }
    }

    private fun roll12SidedDice() {
        Log.d("Tag", "Roll D12")
        rolls = arrayListOf<Int>()
        if (amountEditTextD12.text.toString().isNullOrBlank()) {
            Toast.makeText(activity, "Empty field not allowed!", Toast.LENGTH_SHORT).show()
            Log.d("Tag", "Empty field not allowed!")
        } else {
            val amountOfDice = amountEditTextD12.text.toString().toInt()
            fillRolls(amountOfDice, 12)
            setResultText(12)
        }
    }

    private fun roll10SidedDice() {
        Log.d("Tag", "Roll D10")
        rolls = arrayListOf<Int>()
        if (amountEditTextD10.text.toString().isNullOrBlank()) {
            Toast.makeText(activity, "Empty field not allowed!", Toast.LENGTH_SHORT).show()
            Log.d("Tag", "Empty field not allowed!")
        } else {
            val amountOfDice = amountEditTextD10.text.toString().toInt()
            fillRolls(amountOfDice, 10)
            setResultText(10)
        }
    }

    private fun roll8SidedDice() {
        Log.d("Tag", "Roll D8")
        rolls = arrayListOf<Int>()
        if (amountEditTextD8.text.toString().isNullOrBlank()) {
            Toast.makeText(activity, "Empty field not allowed!", Toast.LENGTH_SHORT).show()
            Log.d("Tag", "Empty field not allowed!")
        } else {
            val amountOfDice = amountEditTextD8.text.toString().toInt()
            fillRolls(amountOfDice, 8)
            setResultText(8)
        }
    }

    private fun roll6SidedDice() {
        Log.d("Tag", "Roll D6")
        rolls = arrayListOf<Int>()
        if (amountEditTextD6.text.toString().isNullOrBlank()) {
            Toast.makeText(activity, "Empty field not allowed!", Toast.LENGTH_SHORT).show()
            Log.d("Tag", "Empty field not allowed!")
        } else {
            val amountOfDice = amountEditTextD6.text.toString().toInt()
            fillRolls(amountOfDice, 6)
            setResultText(6)
        }
    }

    private fun roll4SidedDice() {
        Log.d("Tag", "Roll D4")
        rolls = arrayListOf<Int>()
        if (amountEditTextD4.text.toString().isNullOrBlank()) {
            Toast.makeText(activity, "Empty field not allowed!", Toast.LENGTH_SHORT).show()
            Log.d("Tag", "Empty field not allowed!")
        } else {
            val amountOfDice = amountEditTextD4.text.toString().toInt()
            fillRolls(amountOfDice, 4)
            setResultText(4)
        }
    }

    private fun fillRolls(amountOfDice: Int, sides: Int) {
        var i = 0
        while (i < amountOfDice) {
            var data = Random.nextInt(sides) + 1
            rolls.add(data)
            i++
        }
    }

    private fun setResultText(sides: Int) {
        val output: String = rolls.joinToString(separator = ", ")
        var added = 0
        val size = rolls.size
        var i = 0
        while (i < size) {
            added += rolls[i]
            i++
        }
        val output2 = output + ", Added: " + added.toString()
        when (sides) {
            20 -> resultTextD20.setText(output2)
            12 -> resultTextD12.setText(output2)
            10 -> resultTextD10.setText(output2)
            8 -> resultTextD8.setText(output2)
            6 -> resultTextD6.setText(output2)
            4 -> resultTextD4.setText(output2)
        }
    }
}