package csapat.app.badgesystem

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import csapat.app.BaseCompat
import csapat.app.R
import kotlinx.android.synthetic.main.activity_send_task.*

class SendTaskActivity : BaseCompat() {

    private var image : Uri? = null

    @SuppressLint("ShowToast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_task)

        choose_image_for_task_solution.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_DENIED){
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                    //show popup to request runtime permission
                    requestPermissions(permissions, PERMISSION_CODE);
                }
                else{
                    //permission already granted
                    pickImageFromGallery();
                }
            }
            else{
                //system OS is < Marshmallow
                pickImageFromGallery();
            }
        }

        submitTaskSolutionBtn.setOnClickListener {

            if (image != null) {
                val taskSolution = TaskSolution(taskSolutionET.text.toString(),
                        appUser.userID, intent.getIntExtra("badgeID", 0),
                        "task_solutions/${appUser.userID}_${appUser.patrol}_${intent.getIntExtra("badgeID", 0)}")

                db.collection("taskSolutions").document("${taskSolution.taskSubmitterUserID} For Badge ${taskSolution.badgeID}").set(taskSolution)
                val uploadTask = storageReference.child("task_solutions/${taskSolution.taskSubmitterUserID}_${appUser.patrol}_${taskSolution.badgeID}").putFile(image!!)
                uploadTask.addOnSuccessListener {
                    Toast.makeText(applicationContext, "Elküldve!", Toast.LENGTH_LONG).show()
                }
            }
            else {
                val taskSolution = TaskSolution(taskSolutionET.text.toString(),
                        appUser.userID, intent.getIntExtra("badgeID", 0))

                db.collection("taskSolutions").document("${taskSolution.taskSubmitterUserID} For Badge ${taskSolution.badgeID}").set(taskSolution)

            }
            finish()
            //image?.let { it1 -> storageReference.child("task_solutions/${appUser.fullName}_${appUser.patrol}").putFile(it1) }
        }


    }


    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1000;
        //Permission code
        private val PERMISSION_CODE = 1001;
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.size >0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED){
                    //permission from popup granted
                    pickImageFromGallery()
                }
                else{
                    //permission from popup denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            imageForTaskSolution.setImageURI(data?.data)
            image = data?.data
        }
    }



}