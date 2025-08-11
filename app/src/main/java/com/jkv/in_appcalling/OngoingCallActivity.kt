package com.jkv.in_appcalling

// Import your notification manager if you plan to show an "Ongoing Call" notification
// import com.jkv.newswaale.notifications.CallNotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.jkv.in_appcalling.ui.theme.InAppCallingTheme

class OngoingCallActivity : ComponentActivity() {

    companion object {
        private const val EXTRA_CALLER_NAME_PARAM = "caller_name"
        private const val EXTRA_CALLER_NUMBER_PARAM = "caller_number"
        private const val EXTRA_CALLER_AVATAR_RES_ID_PARAM = "caller_avatar_res_id"

        fun newIntent(
            context: Context,
            callerName: String,
            callerNumber: String,
            callerAvatarResId: Int
        ): Intent {
            return Intent(context, OngoingCallActivity::class.java).apply {
                putExtra(EXTRA_CALLER_NAME_PARAM, callerName)
                putExtra(EXTRA_CALLER_NUMBER_PARAM, callerNumber)
                putExtra(EXTRA_CALLER_AVATAR_RES_ID_PARAM, callerAvatarResId)
                // If IncomingCallActivity should be removed from the back stack:
                // flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                // Or if you want a specific animation:
                // flags = Intent.FLAG_ACTIVITY_NO_ANIMATION // Example
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Keep screen on during the call
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        // Make the activity show over the lock screen and turn screen on,
        // if this activity could somehow be launched while locked (less common for ongoing call screen).
        // If it's always launched from an active IncomingCallActivity, these might not be strictly necessary here.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        } else {
            @Suppress("DEPRECATION")
            window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
            )
        }


        val callerName = intent.getStringExtra(EXTRA_CALLER_NAME_PARAM) ?: "Unknown"
        val callerNumber = intent.getStringExtra(EXTRA_CALLER_NUMBER_PARAM) ?: "N/A"
        val avatarResId =
            intent.getIntExtra(EXTRA_CALLER_AVATAR_RES_ID_PARAM, R.drawable.default_avatar)

        val callerInfo = CallerInfo(callerName, callerNumber, avatarResId)

        Log.d("OngoingCallActivity", "onCreate: Displaying ongoing call for ${callerInfo.name}")

        setContent {
            InAppCallingTheme {
                // Example: If you want to show an "Ongoing Call" notification
                // that allows returning to this screen.
                // LaunchedEffect(Unit) {
                //    CallNotificationManager.showOngoingCallNotification(this, callerInfo)
                // }

                OngoingCallScreen(
                    callerInfo = callerInfo,
                    onHangUp = {
                        Log.d("OngoingCallActivity", "Hang Up clicked for: ${callerInfo.name}")
                        // TODO:
                        // 1. Handle actual telephony logic to terminate the call
                        // 2. Cancel any ongoing call notification
                        // CallNotificationManager.cancelOngoingCallNotification(this)
                        // 3. Finish this activity
                        finish()
                        // Optionally, navigate somewhere else like the call log or main app screen
                    }
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("OngoingCallActivity", "onDestroy called.")
        // Ensure any call resources are released, and ongoing call notifications are cleared
        // if the activity is destroyed unexpectedly.
        // CallNotificationManager.cancelOngoingCallNotification(this)
        // TODO: Add any other necessary cleanup for your telephony logic.
    }

    // Optional: Override onBackPressed to control behavior, e.g., minimize call instead of finishing.
    // override fun onBackPressed() {
    //    Log.d("OngoingCallActivity", "Back pressed during ongoing call.")
    //    // Move to background or show a dialog "End call?"
    //    // For now, default behavior (finish) is fine.
    //    super.onBackPressed()
    // }
}
