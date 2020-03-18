package csapat.app.badgesystem

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import csapat.app.R
import kotlinx.android.synthetic.main.item_scoreboard.view.*

class ScoreboardItemAdapter (val userList : MutableList<Triple<String, String, Long>>, val context: Context) :
        RecyclerView.Adapter<ScoreboardItemAdapter.ScoreboardItemViewHolder>() {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreboardItemViewHolder {
        return ScoreboardItemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_scoreboard, parent, false))
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ScoreboardItemViewHolder, position: Int) {
        val userToShow = userList[position]
        holder.userToShow = userToShow
        holder.userFullName.text = "${position + 1}. ${userToShow.first}"
        holder.userPatrol.text = userToShow.second
        holder.userScore.text = userToShow.third.toString()
    }

    inner class ScoreboardItemViewHolder (view : View) : RecyclerView.ViewHolder(view) {
        var userToShow : Triple<String, String, Long>? = null
        val userFullName : TextView = view.scoreBoardItemName
        val userPatrol : TextView = view.scoreBoardItemPatrol
        val userScore : TextView = view.scoreBoardItemScore

    }
}