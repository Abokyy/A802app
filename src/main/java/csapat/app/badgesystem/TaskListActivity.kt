package csapat.app.badgesystem

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.DocumentChange
import csapat.app.BaseCompat
import csapat.app.R
import kotlinx.android.synthetic.main.activity_task_list.*
import kotlinx.android.synthetic.main.activity_task_solution_details.*

class TaskListActivity : BaseCompat(), TaskItemAdapter.taskItemClickListener {

    private val taskList = mutableListOf<TaskSolution>()
    private val taskIDList = mutableListOf<String>()
    private val TAG = "TaskListActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)

        if (appUser.rank < 2) {
            acceptTask.visibility = View.GONE
            refuseTask.visibility = View.GONE
            readDataForUser()
        } else {
            readDataForLeaders()
        }


    }

    private fun readDataForUser() {

        db.collection("taskSolutions")
                .whereEqualTo("taskSubmitterUserID", appUser.userID)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val taskSolution = document.toObject(TaskSolution::class.java)
                        taskList.add(taskSolution)
                        taskIDList.add(document.id)
                    }
                    initrecyclerview(1)
                }

    }

    private fun readDataForLeaders() {

        db.collection("taskSolutions")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val taskSolution = document.toObject(TaskSolution::class.java)
                        taskList.add(taskSolution)
                        taskIDList.add(document.id)
                    }
                    initrecyclerview(2)
                }

    }

    private fun initrecyclerview(mode: Int) {
        taskRecyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = TaskItemAdapter(taskList, taskIDList, this)
        adapter.itemClickListener = this
        taskRecyclerView.adapter = adapter

        when (mode) {
            1 -> {
                db.collection("taskSolutions")
                        .whereEqualTo("taskSubmitterUserID", appUser.userID)
                        .addSnapshotListener { snapshots, e ->
                            if (e != null) {
                                Log.w(TAG, "listen:error", e)
                                return@addSnapshotListener
                            }

                            for (dc in snapshots!!.documentChanges) {
                                val docID = dc.document.id
                                when (dc.type) {
                                    DocumentChange.Type.ADDED -> {
                                        if (!taskIDList.contains(docID)) {
                                            val taskSolution = dc.document.toObject(TaskSolution::class.java)
                                            adapter.addItem(taskSolution, docID)
                                        }
                                    }
                                    DocumentChange.Type.MODIFIED -> Log.d(TAG, "Modified taskSolution: ${dc.document.data}")
                                    DocumentChange.Type.REMOVED -> adapter.deleteItem(docID)
                                }
                            }
                        }

            }

            2 -> {
                db.collection("taskSolutions")
                        .addSnapshotListener { snapshots, e ->
                            if (e != null) {
                                Log.w(TAG, "listen:error", e)
                                return@addSnapshotListener
                            }

                            for (dc in snapshots!!.documentChanges) {
                                val docID = dc.document.id
                                when (dc.type) {
                                    DocumentChange.Type.ADDED -> {
                                        if (!taskIDList.contains(docID)) {
                                            val taskSolution = dc.document.toObject(TaskSolution::class.java)
                                            adapter.addItem(taskSolution, docID)
                                        }
                                    }
                                    DocumentChange.Type.MODIFIED -> Log.d(TAG, "Modified taskSolution: ${dc.document.data}")
                                    DocumentChange.Type.REMOVED -> adapter.deleteItem(docID)
                                }
                            }
                        }
            }
        }


    }

    override fun onItemClick(taskSolution: TaskSolution) {
        val intent = Intent(this, TaskSolutionDetails::class.java).apply {
            putExtra("TaskSolution", taskSolution)
            putExtra("TaskSolutionID", taskIDList[taskList.indexOf(taskSolution)])
        }
        startActivity(intent)
    }
}
