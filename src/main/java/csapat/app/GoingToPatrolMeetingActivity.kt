package csapat.app

import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import csapat.app.supportFiles.SaveSharedPreference
import csapat.app.teamstructure.model.AppUser
import kotlinx.android.synthetic.main.activity_going_to_patrol_meeting.*

class GoingToPatrolMeetingActivity : BaseCompat() {

    private lateinit var responderUser: AppUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_going_to_patrol_meeting)

        responderUser = SaveSharedPreference.getAppUser(applicationContext)


        yesResponseBtn.setOnClickListener {
            val docRef: DocumentReference = db.collection("patrols").document(responderUser.patrol)
            docRef.update("nextMeetingAttendance", FieldValue.arrayUnion(responderUser.fullName))
                    .addOnSuccessListener {
                        db.collection("users")
                                .document(responderUser.userID)
                                .update("answeredNextMeetingRequest", true)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Igen válasz elküldve.", Toast.LENGTH_LONG).show()
                                    SaveSharedPreference.setPrefUserAnsweredToNextMeeting(this, responderUser, true)
                                    finish()
                                }
                    }
        }

        noResponseBtn.setOnClickListener {
            db.collection("users")
                    .document(responderUser.userID)
                    .update("answeredNextMeetingRequest", true)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Nem válasz elküldve.", Toast.LENGTH_LONG).show()
                        SaveSharedPreference.setPrefUserAnsweredToNextMeeting(this, responderUser, true)
                        finish()
                    }
        }

    }
}