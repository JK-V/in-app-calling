package com.jkv.in_appcalling

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import com.jkv.in_appcalling.ui.theme.InAppCallingTheme

// Data class for CallerInfo if not globally accessible or you want a local version
// data class CallerUIData(val name: String, val number: String, val avatarResId: Int)

class IncomingCallActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ... (window flags as before) ...

        val callerName = intent.getStringExtra(EXTRA_CALLER_NAME) ?: "Unknown Caller"
        val callerNumber = intent.getStringExtra("EXTRA_CALLER_NUMBER") ?: "N/A"
        // Assuming avatar is passed or defaulted, for now using default
        val avatarResId =
            intent.getIntExtra("EXTRA_CALLER_AVATAR_RES_ID", R.drawable.default_avatar)

        Log.d(
            "IncomingCallActivity",
            "Launched for $callerName, Action: ${intent.getStringExtra("ACTION_TYPE")}"
        )

        val callerInfo = CallerInfo(
            name = callerName,
            number = callerNumber,
            avatarResId = avatarResId
        )

        setContent {
            InAppCallingTheme  {
                LaunchedEffect(Unit) {
                    Log.d(
                        "IncomingCallActivity",
                        "Dismissing incoming call notification upon UI display."
                    )
                    CallNotificationManager.cancelIncomingCallNotification(applicationContext)
                }

                IncomingCallScreen(
                    callerInfo = callerInfo,
                    onAnswer = {
                        Log.d(
                            "IncomingCallActivity",
                            "Call Answered in UI for: ${callerInfo.name}. Launching OngoingCallActivity."
                        )
                        // TODO: Handle actual telephony logic to answer the call (e.g., connect to telecom stack)

                        // Start OngoingCallActivity
                        val ongoingCallIntent = OngoingCallActivity.newIntent(
                            context = this@IncomingCallActivity,
                            callerName = callerInfo.name,
                            callerNumber = callerInfo.number,
                            callerAvatarResId = callerInfo.avatarResId
                        )
                        startActivity(ongoingCallIntent)

                        // Finish IncomingCallActivity so it's not on the back stack
                        // during the ongoing call.
                        finish()
                    },
                    onDecline = {
                        Log.d("IncomingCallActivity", "Call Declined in UI for: ${callerInfo.name}")
                        // TODO: Handle actual telephony logic to decline/hang up the call
                        finish() // Finish this activity
                    }
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("IncomingCallActivity", "onDestroy called.")
        // If the activity is destroyed without the call being answered or declined
        // (e.g., user swipes it away from recents), you might want to ensure
        // the call is officially terminated or the notification is re-posted if the call is still active.
        // This depends heavily on your call state management.
        // For a simple dummy, this might not be necessary.
        // CallNotificationManager.cancelIncomingCallNotification(applicationContext) // Ensure cleanup
    }
}
