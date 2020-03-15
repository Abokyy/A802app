package csapat.app.badgesystem

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import csapat.app.R
import kotlinx.android.synthetic.main.item_task_card.view.*

class TaskItemAdapter (val taskList : MutableList<TaskSolution>, val context : Context) :
        RecyclerView.Adapter<TaskItemAdapter.TaskViewHolder>() {

    var itemClickListener : taskItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(LayoutInflater.from(context).inflate(R.layout.item_task_card, parent, false))
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]
        holder.taskSolution = task
        holder.taskText.text = task.taskSubmitterUserID
    }

    interface taskItemClickListener {
        fun onItemClick(taskSolution: TaskSolution)
    }

    inner class TaskViewHolder (view : View) : RecyclerView.ViewHolder(view) {

        var taskSolution: TaskSolution? = null
        val taskText = view.task_card_text

        init {
            view.setOnClickListener {
                taskSolution?.let { taskSolution -> itemClickListener?.onItemClick(taskSolution) }
            }
        }
    }



}