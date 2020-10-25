package csapat.app;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationManagerCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import csapat.app.auth.LoginActivity;
import csapat.app.supportFiles.SaveSharedPreference;

public class NavMainActivity extends BaseCompat {

    public NotificationManagerCompat notificationManagerCompat;
    public static final String CHANNEL_ID = "notificationCH";
    private final static String TAG = "NAVMAINACTIVITY";
    public final static String ACTION_YES = "yes";
    public final static String ACTION_NO = "no";
    //public static final String GUEST_LOGIN = "none";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MobileAds.initialize(this, getString(R.string.ADMOB_APP_ID));

        firebaseMessaging.setAutoInitEnabled(true);

        notificationManagerCompat = NotificationManagerCompat.from(this);
        createNotificationChannel();

        if (SaveSharedPreference.getAppUser(NavMainActivity.this).getUsername().length() == 0) {
            Intent loginAct = new Intent(NavMainActivity.this, LoginActivity.class);
            startActivity(loginAct);
        } else {

            appUser = SaveSharedPreference.getAppUser(NavMainActivity.this);
            setContentView(R.layout.activity_nav_main);

            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "getInstanceId failed", task.getException());
                                return;
                            }

                            // Get new Instance ID token
                            String token = task.getResult().getToken();

                            DocumentReference documentReference = db.collection("users").document(appUser.getUserID());

                            documentReference.update("token", token);

                            // Log and toast
                            String msg = getString(R.string.msg_token_fmt, token);
                            Log.d(TAG, msg);
                        }
                    });


            NavController navController;
            BottomNavigationView navView = findViewById(R.id.nav_view);
            try {

                if (appUser.getRank() > 1) {
                    findViewById(R.id.nav_host_fragment_for_guests).setVisibility(View.GONE);
                    navController = Navigation.findNavController(this, R.id.nav_host_fragment);
                    navView.getMenu().clear();
                    navView.inflateMenu(R.menu.bottom_nav_menu_for_leader);

                } else {
                    findViewById(R.id.nav_host_fragment_for_guests).setVisibility(View.GONE);
                    navController = Navigation.findNavController(this, R.id.nav_host_fragment);
                }

                NavigationUI.setupWithNavController(navView, navController);

            } catch (Exception e) {

            }

            try {
                if (appUser.getUsername().equals("802guest")) {
                    findViewById(R.id.nav_host_fragment).setVisibility(View.GONE);
                    findViewById(R.id.nav_host_fragment_for_guests).setVisibility(View.VISIBLE);
                    navView.getMenu().clear();
                    navController = Navigation.findNavController(this, R.id.nav_host_fragment_for_guests);
                    navView.inflateMenu(R.menu.bottom_nav_menu_for_guest);
                    //assert navController != null;
                    NavigationUI.setupWithNavController(navView, navController);
                }


            } catch (Exception e) {

            }


            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
        /*AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_teamstructure, R.id.navigation_events, R.id.navigation_news, R.id.navigation_edit_profile)
                .build();*/
            //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        }


    }

    @Override
    public void onBackPressed() {

        /*int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.really_logout)
                    .setMessage(R.string.are_you_sure_to_logout)
                    .setNegativeButton(R.string.cancel, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(NavMainActivity.this, LoginActivity.class);
                            SaveSharedPreference.logOutUser(NavMainActivity.this);
                            FirebaseAuth.getInstance().signOut();
                            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                        }

                    }).create().show();
        } else {
            getSupportFragmentManager().popBackStack();
        }*/

        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);

    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
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
