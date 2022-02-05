package lmu.msp.frontend.diceRolling

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import lmu.msp.frontend.R
import lmu.msp.frontend.ui.campaign.campaigns
/**
 * This class contains the adapter for the recycler view used in the diceRollingFragmentAnimated
 * @author Valentin Scheibe
 */
class DiceRollingAdapter(private val diceList: ArrayList<diceData>) :
    RecyclerView.Adapter<DiceRollingAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DiceRollingAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_dice, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: DiceRollingAdapter.ViewHolder, position: Int) {
        val currentItem = diceList[position]
        holder.diceImageView.setImageResource(currentItem.imageId)
        holder.diceImageView.setOnClickListener { removeAt(position) }

    }

    /**
     * when called removes an item from the diceList
     * enables removal of an item in the recycler view when touched
     * @param position indicates position in the diceList that should get removed
     */
    private fun removeAt(position: Int) {
        diceList.removeAt(position)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return diceList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val diceImageView: ImageView = itemView.findViewById(R.id.diceImageView)

        init {
        }
    }
}