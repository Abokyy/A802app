package csapat.app

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import csapat.app.NavMainActivity.CHANNEL_ID
import csapat.app.supportFiles.MyBroadcastReceiver
import csapat.app.supportFiles.MyNotificationPublisher

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val ACTION_YES = "yes"
    private val ACTION_NO = "no"
    private val defaultNotificationChannelId = "default"

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]


        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: ${remoteMessage.from}")

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")
            val dataType = remoteMessage.data[getString(R.string.data_type)]

            if (dataType.equals(getString(R.string.meeting_attendance_notification))) {
                Log.d(TAG, "Message data payload: ${remoteMessage.data}")
                sendNextMeetingAttendanceNotification()
            }

            /*if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use WorkManager.
                scheduleJob()
            } else {
                // Handle message within 10 seconds
                handleNow()
            }*/
        }

    }
    // [END receive_message]

    // [START on_new_token]
    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token)
    }
    // [END on_new_token]

    /**
     * Schedule async work using WorkManager.
     */
    private fun scheduleJob() {
        // [START dispatch_job]
        val work = OneTimeWorkRequest.Builder(MyWorker::class.java).build()
        WorkManager.getInstance().beginWith(work).enqueue()
        // [END dispatch_job]
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private fun handleNow() {
        Log.d(TAG, "Short lived task is done.")
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private fun sendRegistrationToServer(token: String?) {
        Log.d(TAG, "sendRegistrationTokenToServer($token)")
        val documentReference = BaseCompat.db.collection("users").document(BaseCompat.appUser.userID)
        documentReference.update("token", token)
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private fun sendNextMeetingAttendanceNotification() {

        val intent =  Intent(this, GoingToPatrolMeetingActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK and Intent.FLAG_ACTIVITY_CLEAR_TASK)
        val pendIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val yesResponseIntent = Intent(this, MyBroadcastReceiver::class.java)
        yesResponseIntent.action = ACTION_YES
        yesResponseIntent.putExtra("notID", 0)
        val yesPendingIntent = PendingIntent.getBroadcast(this, 0, yesResponseIntent, PendingIntent.FLAG_CANCEL_CURRENT)

        val noResponseIntent = Intent(this, MyBroadcastReceiver::class.java)
        noResponseIntent.action = ACTION_NO
        noResponseIntent.putExtra("notID", 0)
        val noPendingIntent = PendingIntent.getBroadcast(this, 0, noResponseIntent, PendingIntent.FLAG_CANCEL_CURRENT)

        val builder = NotificationCompat.Builder(this, defaultNotificationChannelId)
        builder.setContentTitle("Őrsi jelenlét")
        builder.setContentText("Mész a következő őrsire?")
        builder.setSmallIcon(R.drawable.check_attendance)
        builder.setAutoCancel(true)
        builder.setContentIntent(pendIntent)
        builder.setChannelId(CHANNEL_ID)
        builder.addAction(R.drawable.leadericon, getString(R.string.yes), yesPendingIntent)
        builder.addAction(R.drawable.leadericon, getString(R.string.no), noPendingIntent)

        val notification = builder.build()

        val notificationIntent = Intent(this, MyNotificationPublisher::class.java)
                notificationIntent . putExtra(MyNotificationPublisher.NOTIFICATION_ID, 1)
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION, notification)
        val pendingIntent = PendingIntent.getBroadcast(this, 1, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)


        //val intent = Intent(this, NavMainActivity::class.java)
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        /*val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT)*/

        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        /*val notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.scoreboard_icon)
                .setContentTitle(getString(R.string.choose_patrol))
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)*/

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 /* ID of notification */, notification)
    }

    companion object {

        private const val TAG = "MyFirebaseMsgService"
    }
}