package com.jkv.in_appcalling.ui.screens.incomingcall

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import com.jkv.in_appcalling.ui.screens.ongoingcall.OngoingCallActivity
import com.jkv.in_appcalling.R
import com.jkv.in_appcalling.ui.model.CallerInfo
import com.jkv.in_appcalling.ui.notification.CallNotificationManager
import com.jkv.in_appcalling.ui.notification.EXTRA_CALLER_NAME
import com.jkv.in_appcalling.ui.theme.InAppCallingTheme

class IncomingCallActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val callerName = intent.getStringExtra(EXTRA_CALLER_NAME) ?: "Unknown Caller"
        val callerNumber = intent.getStringExtra("EXTRA_CALLER_NUMBER") ?: "N/A"
        val avatarResId =
            intent.getIntExtra("EXTRA_CALLER_AVATAR_RES_ID", R.drawable.ic_start_your_app_logo)

        Log.d(
            "IncomingCallActivity",
            "Launched for $callerName, Action: ${intent.getStringExtra("ACTION_TYPE")}"
        )

        val callerInfo = CallerInfo(
            name = callerName,
            number = "",
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

                        val ongoingCallIntent = OngoingCallActivity.newIntent(
                            context = this@IncomingCallActivity,
                            callerName = callerInfo.name,
                            callerNumber = callerInfo.number,
                            callerAvatarResId = callerInfo.avatarResId
                        )
                        startActivity(ongoingCallIntent)

                        finish()
                    },
                    onDecline = {
                        Log.d("IncomingCallActivity", "Call Declined in UI for: ${callerInfo.name}")
                        finish()
                    }
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("IncomingCallActivity", "onDestroy called.")
    }
}