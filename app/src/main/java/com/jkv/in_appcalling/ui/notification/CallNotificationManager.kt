package com.jkv.in_appcalling.ui.notification

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
import com.jkv.in_appcalling.ui.screens.incomingcall.IncomingCallActivity
import com.jkv.in_appcalling.R
import com.jkv.in_appcalling.ui.screens.auth.FingerprintAuthForCallActivity

const val INCOMING_CALL_CHANNEL_ID = "incoming_call_channel"
const val INCOMING_CALL_NOTIFICATION_ID = 101

const val ACTION_DECLINE_CALL = "com.jkv.newswaale.ACTION_DECLINE_CALL"
const val EXTRA_CALLER_NAME = "extra_caller_name"

object CallNotificationManager {

    private val VIBRATION_PATTERN = longArrayOf(0, 400, 200, 400, 200, 400)

    fun createNotificationChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val name = "Incoming Calls"
            val descriptionText = "Channel for incoming call notifications with ringtone and vibration."
            val importance = NotificationManager.IMPORTANCE_HIGH

            val ringtoneUri: Uri? = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
                ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

            val channel = NotificationChannel(INCOMING_CALL_CHANNEL_ID, name, importance).apply {
                description = descriptionText
                enableLights(true)

                if (ringtoneUri != null) {
                    val audioAttributes = AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                        .build()
                    setSound(ringtoneUri, audioAttributes)
                } else {
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

        val answerIntent = Intent(context, FingerprintAuthForCallActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(EXTRA_CALLER_NAME, callerName)
            putExtra("EXTRA_CALLER_NUMBER", callerNumber)
            putExtra("ACTION_TYPE", "ANSWER_CALL")
        }
        val answerPendingIntent = PendingIntent.getActivity(
            context,
            1,
            answerIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val declineIntent = Intent(context, CallNotificationActionReceiver::class.java).apply {
            action = ACTION_DECLINE_CALL
            putExtra(EXTRA_CALLER_NAME, callerName)
        }
        val declinePendingIntent = PendingIntent.getBroadcast(
            context,
            2,
            declineIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val contentIntentActivity = Intent(context, IncomingCallActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(EXTRA_CALLER_NAME, callerName)
            putExtra("EXTRA_CALLER_NUMBER", callerNumber)
            putExtra("ACTION_TYPE", "SHOW_INCOMING_CALL")
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
            .setContentIntent(contentPendingIntent)
            .addAction(R.drawable.call_accept_icon, "Answer", answerPendingIntent)
            .addAction(R.drawable.call_decline_icon, "Decline", declinePendingIntent)
            .setAutoCancel(false)
            .setOngoing(true)
            .setFullScreenIntent(contentPendingIntent, true)

        notificationManager.notify(INCOMING_CALL_NOTIFICATION_ID, builder.build())
        Log.i("NotificationManager", "Incoming call notification shown for $callerName. Answer action will start Activity.")
    }

    fun cancelIncomingCallNotification(context: Context) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(INCOMING_CALL_NOTIFICATION_ID)
        Log.i("NotificationManager", "Incoming call notification cancelled. ID: $INCOMING_CALL_NOTIFICATION_ID")
    }
}