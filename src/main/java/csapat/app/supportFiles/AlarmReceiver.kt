package csapat.app.supportFiles

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class AlarmReceiver : BroadcastReceiver() {

    private lateinit var context: Context
    private lateinit var intent: Intent

    override fun onReceive(context: Context?, intent: Intent?) {

        val pendingResult: PendingResult = goAsync()
        val task: Task = Task(pendingResult, intent, context)
        task.execute()

    }

    private class Task(pendingResult: PendingResult, private val intent: Intent?, private val context: Context?) : AsyncTask<String, Int, String>() {

        private val appUser = SaveSharedPreference.getAppUser(context)

        override fun doInBackground(vararg params: String?): String {

            if (intent?.action.equals("clearAnswer")) {

                val db = FirebaseFirestore.getInstance()

                db.collection("patrols")
                        .document(appUser.patrol)
                        .update("nextMeetingAttendance", FieldValue.arrayRemove(appUser.fullName))
                        .addOnSuccessListener {
                            db.collection("users")
                                    .document(appUser.userID)
                                    .update("answeredNextMeetingRequest", false)
                            SaveSharedPreference.setPrefUserAnsweredToNextMeeting(context, appUser, false)
                        }
            }

            return "done"

        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

        }

    }

}