package lmu.msp.frontend.ui.campaign

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import lmu.msp.frontend.diceRolling.DiceFragmentAnimated
import lmu.msp.frontend.R
import lmu.msp.frontend.databinding.FragmentToolsBinding


class ToolsFragment : Fragment() {
    private var _binding: FragmentToolsBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentToolsBinding.inflate(inflater, container, false)
        val view = binding.root
        val toolsList = binding.toolsList
        val floatingActionButton = binding.inviteFriendFAB
        val arrayList = ArrayList<String>()
        arrayList.add("Map")
        arrayList.add("Roll Dice")
        arrayList.add("Chat")
        arrayList.add("Roll animated Dice")
        val arrayAdapter = ArrayAdapter(view.context, android.R.layout.simple_list_item_1, arrayList)
        toolsList.adapter = arrayAdapter
        toolsList.setOnItemClickListener { _, view, position, id ->
            val fragmentManager = parentFragmentManager.beginTransaction()
            when (position){
                0 -> fragmentManager.replace(R.id.fragment, MapFragment()).addToBackStack(null)
                1 -> fragmentManager.replace(R.id.fragment, DiceFragment()).addToBackStack(null)
                2 -> fragmentManager.replace(R.id.fragment, ChatFragment()).addToBackStack(null)
                3 -> fragmentManager.replace(R.id.fragment, DiceFragmentAnimated()).addToBackStack(null)
            }
            fragmentManager.commit()
        }
        floatingActionButton.setOnClickListener {
            val fragmentManager = parentFragmentManager.beginTransaction()
            fragmentManager.replace(R.id.fragment, QRFragment()).addToBackStack(null)
            fragmentManager.commit()
            }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}