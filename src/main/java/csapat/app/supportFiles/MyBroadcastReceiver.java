package csapat.app.supportFiles;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import csapat.app.BaseCompat;
import csapat.app.NavMainActivity;
import csapat.app.teamstructure.model.AppUser;

public class MyBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "MyBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        final PendingResult pendingResult = goAsync();
        Task asyncTask = new Task(pendingResult, intent, context);
        asyncTask.execute();
    }

    private static class Task extends AsyncTask<String, Integer, String> {

        private final PendingResult pendingResult;
        private final Intent intent;
        @SuppressLint("StaticFieldLeak")
        private final Context context;
        private AppUser appUser;
        private boolean yesAnswer = false;

        private Task(PendingResult pendingResult, Intent intent, Context context) {
            this.pendingResult = pendingResult;
            this.intent = intent;
            this.context = context;
            appUser = SaveSharedPreference.getAppUser(context);
        }

        @Override
        protected String doInBackground(String... strings) {


            String action = intent.getAction();

            int not_id = intent.getIntExtra("notID", 0);
            FirebaseFirestore db = FirebaseFirestore.getInstance();


            if (action.equals(NavMainActivity.ACTION_YES)) {
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                assert notificationManager != null;
                notificationManager.cancel(1);


                DocumentReference documentReference = db.collection("patrols").document(appUser.getPatrol());

                documentReference.update("nextMeetingAttendance", FieldValue.arrayUnion(appUser.getFullName()));

                DocumentReference userReference = db.collection("users").document(appUser.getUserID());

                userReference.update("answeredNextMeetingRequest", true);
                yesAnswer = true;

            } else if (action.equals(NavMainActivity.ACTION_NO)) {

                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                assert notificationManager != null;
                notificationManager.cancel(1);

                DocumentReference userReference = db.collection("users").document(appUser.getUserID());

                userReference.update("answeredNextMeetingRequest", true);
            }


            return "done";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // Must call finish() so the BroadcastReceiver can be recycled.
            if (yesAnswer) {
                Toast.makeText(context, "Igen válasz elküldve.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "Nem válasz elküldve.", Toast.LENGTH_LONG).show();
            }
            appUser.setAnsweredToNextMeeting(true);
            SaveSharedPreference.setPrefUserAnsweredToNextMeeting(context, appUser, true);
            pendingResult.finish();
        }
    }


}