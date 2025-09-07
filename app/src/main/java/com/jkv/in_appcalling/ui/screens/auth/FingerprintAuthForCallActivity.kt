package com.jkv.in_appcalling.ui.screens.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jkv.in_appcalling.ui.notification.EXTRA_CALLER_NAME
import com.jkv.in_appcalling.ui.screens.incomingcall.IncomingCallActivity
import com.jkv.in_appcalling.ui.theme.InAppCallingTheme

private const val AUTH_SUCCESS_MESSAGE = "Authentication successful"
private const val AUTH_STATUS_INITIALIZING = "Initializing..."
private const val AUTH_STATUS_AUTHENTICATING = "Authenticating..."
private const val AUTH_STATUS_AUTHENTICATED_PROCEEDING = "Authenticated! Incoming call..."
private const val LOG_TAG_FINGERPRINT_AUTH = "FingerprintAuth"

object CallHandler {
    fun answerCall(context: Context, callId: String?) {
        Log.d(LOG_TAG_FINGERPRINT_AUTH, "Attempting to answer call (ID: $callId)")
        Toast.makeText(context, AUTH_SUCCESS_MESSAGE, Toast.LENGTH_LONG)
            .show()
    }

    fun canAnswerCall(callId: String?): Boolean {
        return callId != null
    }
}

class FingerprintAuthForCallActivity : ComponentActivity() {

    private var callIdToAnswer: String? = null
    private val authStatusState = mutableStateOf(FingerprintAuthStatus.IDLE)
    private val statusMessageState = mutableStateOf(AUTH_STATUS_INITIALIZING)
    private val showTryAgainButtonState = mutableStateOf(false)

    companion object {
        const val EXTRA_CALL_ID = "extra_call_id"
        const val CALLER_NAME_DEFAULT = "Barclays Assistant"
        const val EXTRA_CALLER_NUMBER_KEY = "EXTRA_CALLER_NUMBER"
        const val ACTION_TYPE_KEY = "ACTION_TYPE"
        const val ACTION_ANSWER_CALL_VALUE = "ANSWER_CALL"

        const val RESULT_CALL_ANSWERED = Activity.RESULT_FIRST_USER + 1
        const val RESULT_AUTH_CANCELLED_OR_FAILED = Activity.RESULT_FIRST_USER + 2

        private const val LOG_TAG = "FingerprintAuthActivity"
        private const val LOG_MSG_ONCREATE = "onCreate - Dummy Success Flow"
        private const val LOG_MSG_OUTSIDE_CLICK = "Outside click, cancelling dummy auth."
        private const val LOG_MSG_CANCEL_CLICK = "Cancel button clicked in dummy auth."
        private const val LOG_MSG_DUMMY_AUTH_SUCCESS = "Dummy authentication successful!"
        private const val LOG_MSG_FINISHING_ANSWERED = "ANSWERED"
        private const val LOG_MSG_FINISHING_NOT_ANSWERED = "NOT ANSWERED"
        private const val LOG_MSG_ONDESTROY = "onDestroy"
        private const val LOG_MSG_FINISHING_ACTIVITY = "Finishing activity with result: "


        private const val DELAY_SHOW_SUCCESS_MESSAGE_MS = 1200L
        private const val DELAY_AUTHENTICATION_MS = 1500L

        fun newIntent(context: Context, callId: String?): Intent {
            return Intent(context, FingerprintAuthForCallActivity::class.java).apply {
                putExtra(EXTRA_CALL_ID, callId)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_NO_HISTORY or
                        Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(LOG_TAG, LOG_MSG_ONCREATE)

        callIdToAnswer = intent.getStringExtra(EXTRA_CALL_ID)

        setContent {
            InAppCallingTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = {
                                Log.d(LOG_TAG, LOG_MSG_OUTSIDE_CLICK)
                                finishWithResult(false)
                            }
                        )
                        .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.6f)),
                    color = Color.Transparent
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 32.dp)
                    ) {
                        FingerprintAuthScreenContent(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .background(
                                    MaterialTheme.colorScheme.surfaceVariant,
                                    shape = MaterialTheme.shapes.medium
                                )
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = {

                                    }
                                ),
                            status = authStatusState.value,
                            statusMessage = statusMessageState.value,
                            onCancel = {
                                Log.d(LOG_TAG, LOG_MSG_CANCEL_CLICK)
                                finishWithResult(false)
                            }
                        )
                    }
                }
            }
        }

        authStatusState.value = FingerprintAuthStatus.SCANNING
        statusMessageState.value = AUTH_STATUS_AUTHENTICATING

        Handler(Looper.getMainLooper()).postDelayed({
            Log.d(LOG_TAG, LOG_MSG_DUMMY_AUTH_SUCCESS)
            authStatusState.value = FingerprintAuthStatus.SUCCESS
            statusMessageState.value = AUTH_STATUS_AUTHENTICATED_PROCEEDING

            CallHandler.answerCall(this, callIdToAnswer)

            Handler(Looper.getMainLooper()).postDelayed({
                finishWithResult(true)
            }, DELAY_SHOW_SUCCESS_MESSAGE_MS)

        }, DELAY_AUTHENTICATION_MS)
    }

    private fun finishWithResult(answered: Boolean) {
        if (!isFinishing) {
            val resultCode = if (answered) RESULT_CALL_ANSWERED else RESULT_AUTH_CANCELLED_OR_FAILED
            setResult(resultCode)
            val resultMessage = if (answered) LOG_MSG_FINISHING_ANSWERED else LOG_MSG_FINISHING_NOT_ANSWERED
            Log.d(LOG_TAG, "$LOG_MSG_FINISHING_ACTIVITY$resultMessage")

            val answerIntent = Intent(applicationContext, IncomingCallActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                putExtra(EXTRA_CALLER_NAME, CALLER_NAME_DEFAULT)
                putExtra(EXTRA_CALLER_NUMBER_KEY, "")
                putExtra(ACTION_TYPE_KEY, ACTION_ANSWER_CALL_VALUE)
            }
            answerIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(answerIntent)

            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(LOG_TAG, LOG_MSG_ONDESTROY)
    }
}