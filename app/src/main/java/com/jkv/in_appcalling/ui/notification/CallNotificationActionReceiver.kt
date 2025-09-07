package com.jkv.in_appcalling.ui.notification

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresPermission

class CallNotificationActionReceiver : BroadcastReceiver() {
    @RequiresPermission("android.permission.BROADCAST_CLOSE_SYSTEM_DIALOGS")
    override fun onReceive(context: Context, intent: Intent) {
        if (context == null || intent == null) return

        val callerName = intent.getStringExtra(EXTRA_CALLER_NAME) ?: "Unknown Caller"
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        when (intent.action) {
            ACTION_DECLINE_CALL -> {
                Log.d("CallNotification", "Declined call from $callerName (via BroadcastReceiver)")
                Toast.makeText(context, "Call from $callerName declined", Toast.LENGTH_SHORT).show()
                notificationManager.cancel(INCOMING_CALL_NOTIFICATION_ID)
            }
        }
    }
}