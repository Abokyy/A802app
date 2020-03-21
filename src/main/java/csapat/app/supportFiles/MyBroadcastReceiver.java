package csapat.app.supportFiles;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import csapat.app.BaseCompat;
import csapat.app.NavMainActivity;

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

        private Task(PendingResult pendingResult, Intent intent, Context context) {
            this.pendingResult = pendingResult;
            this.intent = intent;
            this.context = context;
        }

        @Override
        protected String doInBackground(String... strings) {



            String action = intent.getAction();

            int not_id = intent.getIntExtra("notID", 0);

            if(action.equals(NavMainActivity.ACTION_YES)) {
                Log.d("Action yes", "Pressed");
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                assert notificationManager != null;
                notificationManager.cancel(1);

                FirebaseFirestore db = BaseCompat.db;

                DocumentReference documentReference= db.collection("patrols").document(BaseCompat.appUser.getPatrol());

                documentReference.update("nextMeetingAttendance", FieldValue.arrayUnion(BaseCompat.appUser.getFullName()));

                DocumentReference userReference = db.collection("users").document(BaseCompat.appUser.getUserID());

                userReference.update("answeredNextMeetingRequest", true);

            }


            return "done";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // Must call finish() so the BroadcastReceiver can be recycled.
            Toast.makeText(context,"Igen válasz elküldve.", Toast.LENGTH_LONG).show();
            BaseCompat.appUser.setAnsweredToNextMeeting(true);
            SaveSharedPreference.setPrefUserAnsweredToNextMeeting(context, BaseCompat.appUser, true);

            Log.d("Task: ", "Executed " + s);
            pendingResult.finish();
        }
    }


}