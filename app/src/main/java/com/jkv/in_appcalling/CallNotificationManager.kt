package com.jkv.in_appcalling

// It's good practice to check for VIBRATOR_SERVICE permission if targeting higher APIs,
// though for basic vibration patterns with notifications, it's often implied by POST_NOTIFICATIONS.
// For complex custom vibrations outside notifications, explicit permission might be needed.
// import android.os.VibrationEffect // For newer vibration APIs
// import android.os.Vibrator
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat

const val INCOMING_CALL_CHANNEL_ID = "incoming_call_channel"
const val INCOMING_CALL_NOTIFICATION_ID = 101 // Unique ID for this specific notification

const val ACTION_ANSWER_CALL = "com.jkv.newswaale.ACTION_ANSWER_CALL"
const val ACTION_DECLINE_CALL = "com.jkv.newswaale.ACTION_DECLINE_CALL"
const val EXTRA_CALLER_NAME = "extra_caller_name"

object CallNotificationManager {

    // Define a vibration pattern: [delay, vibrate_for, delay, vibrate_for, ...] (in milliseconds)
    // Example: Wait 0ms, vibrate 400ms, wait 200ms, vibrate 400ms, wait 200ms, vibrate 400ms
    private val VIBRATION_PATTERN = longArrayOf(0, 400, 200, 400, 200, 400)
    // A simpler pattern for testing: longArrayOf(0, 500, 500, 500)

    fun createNotificationChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val name = "Incoming Calls" // User-visible name in settings
            val descriptionText = "Channel for incoming call notifications with ringtone and vibration."
            val importance = NotificationManager.IMPORTANCE_HIGH // Crucial for heads-up & sound/vibration

            val ringtoneUri: Uri? = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
                ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION) // Fallback

            val channel = NotificationChannel(INCOMING_CALL_CHANNEL_ID, name, importance).apply {
                description = descriptionText
                enableLights(true)
                // Ensure you have a color defined as R.color.your_notification_light_color
                // lightColor = ContextCompat.getColor(context, R.color.your_notification_light_color)


                if (ringtoneUri != null) {
                    val audioAttributes = AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                        .build()
                    setSound(ringtoneUri, audioAttributes)
                } else {
                    // Handle case where no default ringtone is found, maybe log it
                    Log.w("NotificationSetup", "No default ringtone found for incoming call channel.")
                }

                enableVibration(true)
                vibrationPattern = VIBRATION_PATTERN
            }

            Log.d("NotificationSetup", "Creating notification channel: $INCOMING_CALL_CHANNEL_ID with sound and vibration")
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
            Log.d("NotificationSetup", "Channel $INCOMING_CALL_CHANNEL_ID created/updated.")
        }
    }

    fun showIncomingCallNotification(
        context: Context,
        callerName: String,
        callerNumber: String
    ) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager.getNotificationChannel(INCOMING_CALL_CHANNEL_ID) == null) {
                Log.w("NotificationManager", "Channel $INCOMING_CALL_CHANNEL_ID not found. Recreating.")
                createNotificationChannels(context.applicationContext)
            }
        }

        // --- INTENT FOR "ANSWER" ACTION ---
        // This intent will now start your IncomingCallActivity (or MainActivity with specific extras)
        val answerIntent = Intent(context, IncomingCallActivity::class.java).apply {
            // Standard flags for starting a new activity from a notification
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            // Pass necessary data to the activity
            putExtra(EXTRA_CALLER_NAME, callerName)
            putExtra("EXTRA_CALLER_NUMBER", callerNumber) // Add if needed by your activity
            putExtra("ACTION_TYPE", "ANSWER_CALL") // To inform the activity it's an answer action
            // Add any other call-specific identifiers if you have them
        }
        val answerPendingIntent = PendingIntent.getActivity(
            context,
            1, // Unique request code for this PendingIntent
            answerIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // --- INTENT FOR "DECLINE" ACTION (can still be a BroadcastReceiver or launch an activity that quickly finishes) ---
        val declineIntent = Intent(context, CallNotificationActionReceiver::class.java).apply {
            action = ACTION_DECLINE_CALL
            putExtra(EXTRA_CALLER_NAME, callerName)
        }
        val declinePendingIntent = PendingIntent.getBroadcast(
            context,
            2, // Unique request code
            declineIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // --- CONTENT INTENT (when notification body is tapped - can also open call screen) ---
        val contentIntentActivity = Intent(context, IncomingCallActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(EXTRA_CALLER_NAME, callerName)
            putExtra("EXTRA_CALLER_NUMBER", callerNumber)
            putExtra("ACTION_TYPE", "SHOW_INCOMING_CALL") // Different action type if needed
        }
        val contentPendingIntent = PendingIntent.getActivity(
            context,
            0,
            contentIntentActivity,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )


        val builder = NotificationCompat.Builder(context, INCOMING_CALL_CHANNEL_ID)
            .setSmallIcon(R.drawable.call_icon)
            .setContentTitle("Incoming Call from")
            .setContentText("$callerName ($callerNumber)")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_CALL)
            .setContentIntent(contentPendingIntent) // Tapping notification body
            .addAction(R.drawable.call_accept_icon, "Answer", answerPendingIntent) // "Answer" button
            .addAction(R.drawable.call_decline_icon, "Decline", declinePendingIntent) // "Decline" button
            .setAutoCancel(false) // Important: Notification should persist until call is handled
            .setOngoing(true)
            // CRITICAL FOR CALLS: This makes the Activity launch immediately in full screen
            .setFullScreenIntent(contentPendingIntent, true) // Use contentPendingIntent or a dedicated one for full screen

        // ... (sound/vibration for pre-Oreo as before) ...

        notificationManager.notify(INCOMING_CALL_NOTIFICATION_ID, builder.build())
        Log.i("NotificationManager", "Incoming call notification shown for $callerName. Answer action will start Activity.")
    }

    // ... (cancelIncomingCallNotification remains the same)
    fun cancelIncomingCallNotification(context: Context) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(INCOMING_CALL_NOTIFICATION_ID)
        Log.i("NotificationManager", "Incoming call notification cancelled. ID: $INCOMING_CALL_NOTIFICATION_ID")
    }
}