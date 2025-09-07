package com.jkv.in_appcalling.ui.notification

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
fun RequestNotificationPermissionAndShow() {
    val CALLER_NAME = "Bank Assistant"
    val CALLER_NUMBER = "+44 901902903"
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            CallNotificationManager.showIncomingCallNotification(
                context,
                CALLER_NAME,
                CALLER_NUMBER
            )
        } else {
            Toast.makeText(context, "Notification permission denied", Toast.LENGTH_LONG).show()
        }
    }

    Button(onClick = {
        when {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED -> {
                CallNotificationManager.showIncomingCallNotification(
                    context,
                    CALLER_NAME,
                    CALLER_NUMBER
                )
            }
            else -> {
                launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }) {
        Text("Show Dummy Call Notification")
    }
}