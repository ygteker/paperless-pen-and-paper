package lmu.msp.frontend.ui.campaign

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import lmu.msp.frontend.R

/**
 * This class contains the adapter for the recycler view used in the userFragment
 * currently does not handle avatar implementation
 * @author Valentin Scheibe
 */
class UserAdapter(private val userList: ArrayList<campaignUsers>) :
    RecyclerView.Adapter<UserAdapter.ViewHolder>() {


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rec_user_id: TextView = itemView.findViewById(R.id.rec_user_id)
        val rec_character_name: TextView = itemView.findViewById(R.id.rec_character_name)
        val rec_avatar: ImageView = itemView.findViewById(R.id.rec_avatar)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_user_list, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserAdapter.ViewHolder, position: Int) {
        val currentItem = userList[position]
        holder.rec_user_id.text = currentItem.userId
        holder.rec_character_name.text = currentItem.characterName
        holder.rec_avatar.setImageResource(R.drawable.logo)
    }

    override fun getItemCount(): Int {
        return userList.size
    }


}