package csapat.app.badgesystem

import android.annotation.SuppressLint
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
                    }
                    initrecyclerview()
                }
    }

    private fun initrecyclerview() {
        taskRecyclerView.layoutManager = LinearLayoutManager(this)
        var adapter : TaskItemAdapter = TaskItemAdapter(taskList, this)
        adapter.itemClickListener = this
        taskRecyclerView.adapter = adapter
    }

    @SuppressLint("ShowToast")
    override fun onItemClick(taskSolution: TaskSolution) {
        Toast.makeText(applicationContext, "teszt", Toast.LENGTH_LONG).show()
    }
}
