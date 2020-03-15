package csapat.app.badgesystem

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.tasks.Task
import csapat.app.BaseCompat
import csapat.app.R
import kotlinx.android.synthetic.main.activity_task_list.*

class TaskListActivity : BaseCompat(), TaskItemAdapter.taskItemClickListener {

    private val taskList = mutableListOf<TaskSolution>()
    private val taskIDList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)

        readData()

    }

    private fun readData() {

        db.collection("taskSolutions")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val taskSolution = document.toObject(TaskSolution::class.java)
                        taskList.add(taskSolution)
                        taskIDList.add(document.id)
                    }
                    initrecyclerview()
                }
    }

    private fun initrecyclerview() {
        taskRecyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = TaskItemAdapter(taskList, this)
        adapter.itemClickListener = this
        taskRecyclerView.adapter = adapter
    }

    override fun onItemClick(taskSolution: TaskSolution) {
        val intent = Intent(this, TaskSolutionDetails::class.java).apply {
            putExtra("TaskSolution", taskSolution)
            putExtra("TaskSolutionID", taskIDList[taskList.indexOf(taskSolution)])
        }
        startActivity(intent)
    }
}
