package com.jkv.in_appcalling.ui.screens.ongoingcall

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.jkv.in_appcalling.R
import com.jkv.in_appcalling.ui.model.CallerInfo
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
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

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
            intent.getIntExtra(EXTRA_CALLER_AVATAR_RES_ID_PARAM, R.drawable.ic_start_your_app_logo)

        val callerInfo = CallerInfo(callerName, callerNumber, avatarResId)

        Log.d("OngoingCallActivity", "onCreate: Displaying ongoing call for ${callerInfo.name}")

        setContent {
            InAppCallingTheme {
                OngoingCallScreen(
                    callerInfo = callerInfo,
                    onHangUp = {
                        Log.d("OngoingCallActivity", "Hang Up clicked for: ${callerInfo.name}")
                        finish()
                    }
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("OngoingCallActivity", "onDestroy called.")
    }
}
