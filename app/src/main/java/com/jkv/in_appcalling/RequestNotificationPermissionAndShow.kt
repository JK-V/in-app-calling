package com.jkv.in_appcalling// In your Activity or Composable where you trigger the notification
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
        val context = LocalContext.current
        val launcher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Permission is granted. Show the notification.
                CallNotificationManager.showIncomingCallNotification(
                    context,
                    "Barc Assitant",
                    "+44 901902903"
                )
            } else {
                // Permission denied. Handle appropriately (e.g., show a message).
                Toast.makeText(context, "Notification permission denied", Toast.LENGTH_LONG).show()
            }
        }

        Button(onClick = {
            when {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // Permission is already granted
                    CallNotificationManager.showIncomingCallNotification(
                        context,
                        "Barc Assitant",
                        "+44 901902903"
                    )
                }
                // TODO: Should show rationale before requesting if needed (not shown here for brevity)
                // ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.POST_NOTIFICATIONS) -> { ... }
                else -> {
                    launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }) {
            Text("Show Dummy Call Notification")
        }
    }