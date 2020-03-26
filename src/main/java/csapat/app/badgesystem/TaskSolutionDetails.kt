package csapat.app.badgesystem

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.marginEnd
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.BaseTransientBottomBar.BaseCallback
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FieldValue
import com.google.firebase.storage.FirebaseStorage
import csapat.app.BaseCompat
import csapat.app.R
import csapat.app.teamstructure.model.AppUser
import kotlinx.android.synthetic.main.activity_send_task.*
import kotlinx.android.synthetic.main.activity_task_solution_details.*

class TaskSolutionDetails : BaseCompat() {

    private var taskSubmitterUser: AppUser? = null
    private lateinit var taskSolutionID: String
    private lateinit var taskSolution: TaskSolution

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_solution_details)
        showProgressDialog()

        taskSolution = intent.getSerializableExtra("TaskSolution") as TaskSolution
        taskSolutionID = intent.getStringExtra("TaskSolutionID") as String

        if (intent.getIntExtra("taskDetailMode", 0) == 1) {
            acceptTask.visibility = View.GONE
            refuseTask.visibility = View.GONE
        }

        refuseResponseTV.text = taskSolution.refuseResponse
        var solvedBadgeLevel: Long? = null

        db.collection("badges").document(taskSolution.badgeID.toString())
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    solvedBadgeLevel = documentSnapshot.getLong("level")
                    handleBadgeLevel(solvedBadgeLevel)
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
        } else {
            hideProgressDialog()
        }

        refuseTask.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val editText = EditText(this@TaskSolutionDetails)
            builder.setView(editText)
            builder.setTitle("Elutasítás")
            builder.setMessage("Miért nem fogadod el a megoldást?")

            builder.setPositiveButton("Elutasítás") { dialog, which ->
                db.collection("taskSolutions").document(taskSolutionID)
                        .update("refuseResponse", "Elutasítva: ${editText.text}")

                val snackbar = Snackbar.make(findViewById(R.id.taskSolutionDetailsRootLayout), "Megoldás elutasítva", Snackbar.LENGTH_SHORT)

                snackbar.addCallback(object : Snackbar.Callback() {
                    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                        super.onDismissed(transientBottomBar, event)
                        finish()
                    }
                })
                snackbar.show()
            }


            val dialog: AlertDialog = builder.create()
            dialog.show()
        }


    }

    private fun handleBadgeLevel(solvedBadgeLevel: Long?) {

        db.collection("users").document(taskSolution.taskSubmitterUserID)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    taskSubmitterUser = documentSnapshot.toObject(AppUser::class.java)
                }

        acceptTask.setOnClickListener {


            when (solvedBadgeLevel) {
                1L -> {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Elfogadás")
                    builder.setMessage("Biztosan elfogadod a beadott megoldást?")
                    builder.setPositiveButton("Igen") { dialog, which ->

                        val solvedBadges = taskSubmitterUser?.achievedBadges
                        solvedBadges?.add(taskSolution.badgeID)

                        val ref = db.collection("users").document(taskSolution.taskSubmitterUserID)

                        var incr = 0L
                        db.collection("badges").document(taskSolution.badgeID.toString())
                                .get()
                                .addOnSuccessListener { documentSnapshot ->
                                    incr = documentSnapshot.get("points") as Long
                                    ref.update("achievedBadges", solvedBadges)
                                    ref.update("score", FieldValue.increment(incr))
                                }

                        db.collection("taskSolutions").document(taskSolutionID)
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

                    builder.setNegativeButton("Mégsem") { dialog, which ->
                        dialog.dismiss()
                    }

                    val dialog: AlertDialog = builder.create()
                    dialog.show()
                }
                2L -> {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Elfogadás")
                    builder.setMessage("Biztosan elfogadod a beadott megoldást?")
                    builder.setPositiveButton("Igen") { dialog, which ->

                        var solvedBadges: MutableList<Int>

                        db.collection("patrols").document(taskSolution.taskSubmitterUserID)
                                .get()
                                .addOnSuccessListener { documentSnapshot ->
                                    solvedBadges = documentSnapshot.get("achievedBadges") as MutableList<Int>
                                    solvedBadges.add(taskSolution.badgeID)


                                    val ref = db.collection("patrols").document(taskSolution.taskSubmitterUserID)

                                    var incr = 0L
                                    db.collection("badges").document(taskSolution.badgeID.toString())
                                            .get()
                                            .addOnSuccessListener { documentSnapshot ->
                                                incr = documentSnapshot.get("points") as Long
                                                ref.update("achievedBadges", solvedBadges)
                                                ref.update("score", FieldValue.increment(incr))
                                            }
                                }

                        db.collection("taskSolutions").document(taskSolutionID)
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

                    builder.setNegativeButton("Mégsem") { dialog, which ->
                        dialog.dismiss()
                    }

                    val dialog: AlertDialog = builder.create()
                    dialog.show()
                }
            }




        }

    }
}
