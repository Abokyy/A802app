package csapat.app.badgesystem

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import csapat.app.R
import csapat.app.teamstructure.model.AppUser
import kotlinx.android.synthetic.main.item_task_card.view.*

class TaskItemAdapter (val taskList : MutableList<TaskSolution>, val taskIDList : MutableList<String>, val context : Context) :
        RecyclerView.Adapter<TaskItemAdapter.TaskViewHolder>() {

    var itemClickListener : taskItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(LayoutInflater.from(context).inflate(R.layout.item_task_card, parent, false))
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    fun addItem(taskSolution: TaskSolution, taskSolutionID: String) {
        val size = taskList.size
        taskIDList.add(taskSolutionID)
        taskList.add(taskSolution)
        notifyItemInserted(size)
    }


    fun deleteItem(taskSolutionID: String) {
        val index = taskIDList.indexOf(taskSolutionID)
        taskList.removeAt(index)
        taskIDList.removeAt(index)
        notifyItemRemoved(index)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]
        holder.taskSolution = task
        val submitterUserID = task.taskSubmitterUserID
        FirebaseFirestore.getInstance().collection("users").document(submitterUserID)
                .get()
                .addOnSuccessListener { document ->
                    holder.submitterUser = document.toObject(AppUser::class.java)
                    holder.taskText.text = "${holder.submitterUser?.fullName} megoldása a ${task.badgeID} kitűzőre"
                }

    }

    interface taskItemClickListener {
        fun onItemClick(taskSolution: TaskSolution)
    }

    inner class TaskViewHolder (view : View) : RecyclerView.ViewHolder(view) {

        var taskSolution: TaskSolution? = null
        val taskText = view.task_card_text
        var submitterUser : AppUser? = null

        init {
            view.setOnClickListener {
                taskSolution?.let { taskSolution -> itemClickListener?.onItemClick(taskSolution) }
            }
        }
    }



}