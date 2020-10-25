package csapat.app

import android.os.Bundle
import android.util.Log
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

        val userRef = db.collection("users").document(responderUser.userID)
        userRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        if (document.getBoolean("answeredNextMeetingRequest")!!) {
                            responseTV.text = "Már válaszoltál a héten, ha szeretnéd megváltoztatni akkor kattints a megfelelő gombra"
                        } else {
                            responseTV.text = "Még nem válaszoltál"
                        }
                    } else {
                        Log.d("Going to patrol meeting", "No such document")
                    }
                }



        yesResponseBtn.setOnClickListener {
            val docRef: DocumentReference = db.collection("patrols").document(responderUser.patrol)
            docRef.update("nextMeetingAttendance", FieldValue.arrayUnion(responderUser.fullName))
                    .addOnSuccessListener {
                        userRef
                                .update("answeredNextMeetingRequest", true)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Igen válasz elküldve.", Toast.LENGTH_LONG).show()
                                    SaveSharedPreference.setPrefUserAnsweredToNextMeeting(this, responderUser, true)
                                    finish()
                                }
                    }
        }

        noResponseBtn.setOnClickListener {
            userRef
                    .update("answeredNextMeetingRequest", true)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Nem válasz elküldve.", Toast.LENGTH_LONG).show()
                        SaveSharedPreference.setPrefUserAnsweredToNextMeeting(this, responderUser, true)
                        finish()
                    }
        }

    }
}