package csapat.app;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.Calendar;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import csapat.app.auth.LoginActivity;
import csapat.app.teamstructure.model.AppUser;
import csapat.app.teamstructure.model.Patrol;

import static android.app.Notification.EXTRA_NOTIFICATION_ID;

public class NavMainActivity extends BaseCompat {

    public NotificationManagerCompat notificationManagerCompat;
    public static final String CHANNEL_ID = "notificationCH";
    private final static String default_notification_channel_id = "default" ;
    public final static String ACTION_YES = "yes";
    public static final String GUEST_LOGIN = "none";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        notificationManagerCompat = NotificationManagerCompat.from(this);
        createNotificationChannel();
        

        //OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(NotifyWorker.class).build();

        //WorkManager.getInstance(NavMainActivity.this).enqueue(oneTimeWorkRequest);



        if (SaveSharedPreference.getAppUser(NavMainActivity.this).getUsername().length() == 0) {
            Intent loginAct = new Intent(NavMainActivity.this, LoginActivity.class);
            startActivity(loginAct);
        } else {

            appUser = SaveSharedPreference.getAppUser(NavMainActivity.this);
            setContentView(R.layout.activity_nav_main);




            BottomNavigationView navView = findViewById(R.id.nav_view);
            try {

                if (appUser.getRank() > 1) {
                    navView.getMenu().clear();
                    navView.inflateMenu(R.menu.bottom_nav_menu_for_leader);

                    DocumentReference documentReference = db.collection("patrols").document(appUser.getPatrol());

                    documentReference
                            .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Patrol patrol;
                            patrol = documentSnapshot.toObject(Patrol.class);
                            scheduleNotification(patrol);

                        }
                    });
                }
            } catch (Exception e) {

            }

            try {
                if (appUser.getUsername().equals("802guest")) {
                    navView.getMenu().clear();
                    navView.inflateMenu(R.menu.bottom_nav_menu_for_guest);

                }
            } catch (Exception e) {

            }


            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
        /*AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_teamstructure, R.id.navigation_events, R.id.navigation_news, R.id.navigation_edit_profile)
                .build();*/
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
            //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
            NavigationUI.setupWithNavController(navView, navController);
        }


    }

    private void scheduleNotification(Patrol patrol) {

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());

        Log.d("Cal date", String.valueOf(cal.get(Calendar.DAY_OF_WEEK)));




        assert patrol != null;

        int dayToAskForMeeting = patrol.getMeetingDay();

        if(dayToAskForMeeting == 7) {
            dayToAskForMeeting = 1;
            cal.set(Calendar.DAY_OF_WEEK, dayToAskForMeeting);
        } else {
            cal.set(Calendar.DAY_OF_WEEK, dayToAskForMeeting+1);
        }

        cal.set(Calendar.HOUR_OF_DAY, patrol.getMeetingHour());
        cal.set(Calendar.MINUTE, patrol.getMeetingMinute());

        Log.d("Cal date", String.valueOf(cal.get(Calendar.YEAR)));
        Log.d("Cal date", String.valueOf(cal.get(Calendar.DAY_OF_WEEK)));
        Log.d("Cal date", String.valueOf(cal.get(Calendar.HOUR_OF_DAY)));
        Log.d("Cal date", String.valueOf(cal.get(Calendar.MINUTE)));


        Intent intent = new Intent(this, GoingToPatrolMeetingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent yesResponseIntent = new Intent(this, MyBroadcastReceiver.class);
        yesResponseIntent.setAction(ACTION_YES);
        yesResponseIntent.putExtra("notID", 9);
        PendingIntent yesPendingIntent = PendingIntent.getBroadcast(this,9,yesResponseIntent, PendingIntent.FLAG_CANCEL_CURRENT);



        NotificationCompat.Builder builder = new NotificationCompat.Builder( this, default_notification_channel_id ) ;
        builder.setContentTitle( "Scheduled Notification" ) ;
        builder.setContentText("boi") ;
        builder.setSmallIcon(R.drawable. ic_launcher_foreground ) ;
        builder.setAutoCancel( true ) ;
        builder.setContentIntent(pendIntent);
        builder.setChannelId(CHANNEL_ID) ;
        builder.addAction(R.drawable.leadericon, getString(R.string.app_name), yesPendingIntent);

        Notification notification = builder.build();

        Intent notificationIntent = new Intent(this, MyNotificationPublisher.class);
        notificationIntent.putExtra(MyNotificationPublisher. NOTIFICATION_ID , 1 ) ;
        notificationIntent.putExtra(MyNotificationPublisher. NOTIFICATION , notification) ;
        PendingIntent pendingIntent = PendingIntent. getBroadcast ( this, 1 , notificationIntent , PendingIntent. FLAG_UPDATE_CURRENT );
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context. ALARM_SERVICE ) ;
        assert alarmManager != null;
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP , cal.getTimeInMillis() , AlarmManager.INTERVAL_DAY, pendingIntent);

    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(NavMainActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                    }

                }).create().show();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = NavMainActivity.this.getSystemService(NotificationManager.class);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }
    }


}
