package csapat.app.badgesystem

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.BaseTransientBottomBar.BaseCallback
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.storage.FirebaseStorage
import csapat.app.BaseCompat
import csapat.app.R
import csapat.app.teamstructure.model.AppUser
import kotlinx.android.synthetic.main.activity_send_task.*
import kotlinx.android.synthetic.main.activity_task_solution_details.*

class TaskSolutionDetails : BaseCompat() {

    private var taskSubmitterUser: AppUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_solution_details)
        showProgressDialog()

        val taskSolution = intent.getSerializableExtra("TaskSolution") as? TaskSolution


        if (taskSolution != null) {

            db.collection("users").document(taskSolution.taskSubmitterUserID)
                    .get()
                    .addOnSuccessListener { documentSnapshot ->
                        taskSubmitterUser = documentSnapshot.toObject(AppUser::class.java)
                    }

            taskSolutionDetailTV.text = taskSolution.solutionDescription

            if (taskSolution.imagePath != "noimage") {
                val imageView = findViewById<ImageView>(R.id.taskSolutionDetailImage)


                storageReference.child(taskSolution.imagePath).downloadUrl.addOnSuccessListener { uri ->
                    Glide.with(this)
                            .load(uri)
                            .into(imageView)
                    hideProgressDialog()
                }

                acceptTask.setOnClickListener {
                    val solvedBadges = taskSubmitterUser?.achievedBadges
                    solvedBadges?.add(taskSolution.badgeID)

                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Megoldás elfogadása")
                    builder.setMessage("Biztosan elfogadod a beadott megoldást?")

                    builder.setPositiveButton("Igen") { dialog, which ->

                        db.collection("users").document(taskSolution.taskSubmitterUserID)
                                .update("achievedBadges", solvedBadges)


                        db.collection("taskSolutions").document(intent.getStringExtra("TaskSolutionID") as String)
                                .delete()
                        storageReference.child(taskSolution.imagePath).delete()

                        val snackbar = Snackbar.make(findViewById(R.id.taskSolutionDetailsRootLayout), "Megoldás elfogadva", Snackbar.LENGTH_SHORT)

                        snackbar.addCallback(object : Snackbar.Callback() {
                            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                                super.onDismissed(transientBottomBar, event)
                                finish()
                            }
                        })
                        snackbar.show()


                    }

                    val dialog : AlertDialog = builder.create()
                    dialog.show()


                }

            }
        } else {
            taskSolutionDetailTV.text = "tasksolution null"
            hideProgressDialog()
        }


    }
}
