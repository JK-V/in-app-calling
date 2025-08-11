package com.jkv.in_appcalling

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresPermission

// Dummy BroadcastReceiver to handle notification actions
class CallNotificationActionReceiver : BroadcastReceiver() {
    @RequiresPermission("android.permission.BROADCAST_CLOSE_SYSTEM_DIALOGS")
    override fun onReceive(context: Context, intent: Intent) {
        if (context == null || intent == null) return

        val callerName = intent.getStringExtra(EXTRA_CALLER_NAME) ?: "Unknown Caller"
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        when (intent.action) {
            // ACTION_ANSWER_CALL is now handled by starting an Activity.
            // If you still need some background processing for answer via BroadcastReceiver
            // (e.g., before launching UI), you could keep it, but it's less common.

            ACTION_DECLINE_CALL -> {
                Log.d("CallNotification", "Declined call from $callerName (via BroadcastReceiver)")
                Toast.makeText(context, "Call from $callerName declined", Toast.LENGTH_SHORT).show()
                // TODO: Handle actual call decline logic (e.g., inform server, telephony stack)
                notificationManager.cancel(INCOMING_CALL_NOTIFICATION_ID) // Dismiss notification
            }
        }
        // Optionally close the notification shade if it's still open
        // val closeIntent = Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
        // context.sendBroadcast(closeIntent)
    }
}