package com.jkv.in_appcalling

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jkv.in_appcalling.ui.theme.InAppCallingTheme
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

@Composable
fun OngoingCallScreen(
    callerInfo: CallerInfo, // You'll pass this from IncomingCallActivity
    onHangUp: () -> Unit
) {
    var callDurationSeconds by remember { mutableStateOf(0L) }

    // Timer logic
    LaunchedEffect(key1 = Unit) { // Starts when the composable enters the composition
        while (true) {
            delay(1000) // delay for 1 second
            callDurationSeconds++
        }
    }

    val formattedDuration = remember(callDurationSeconds) {
        val hours = TimeUnit.SECONDS.toHours(callDurationSeconds)
        val minutes = TimeUnit.SECONDS.toMinutes(callDurationSeconds) % 60
        val seconds = callDurationSeconds % 60
        if (hours > 0) {
            String.format("%02d:%02d:%02d", hours, minutes, seconds)
        } else {
            String.format("%02d:%02d", minutes, seconds)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant) // Different background for ongoing call
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Caller Info and Timer (Top/Center)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = callerInfo.avatarResId),
                    contentDescription = "Caller Avatar",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondaryContainer),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = callerInfo.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = callerInfo.number,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = formattedDuration,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(text = "Ongoing Call", style = MaterialTheme.typography.labelMedium)
            }

            // Call Controls (Bottom)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 48.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Example: Mute Button
                CallActionButton( // Reusing the button from IncomingCallScreen
                    iconResId = R.drawable.mic_on_icon, // replace with actual mic icon
                    label = "Mute",
                    onClick = { /* TODO: Handle Mute */ },
                    buttonColor = MaterialTheme.colorScheme.secondary,
                    iconColor = MaterialTheme.colorScheme.onSecondary
                )

                // Example: Speaker Button
                CallActionButton(
                    iconResId = R.drawable.speaker_on_icon, // replace with actual speaker icon
                    label = "Speaker",
                    onClick = { /* TODO: Handle Speaker */ },
                    buttonColor = MaterialTheme.colorScheme.secondary,
                    iconColor = MaterialTheme.colorScheme.onSecondary
                )

                // Hang Up Button
                CallActionButton(
                    iconResId = R.drawable.call_decline_icon, // Using decline icon for hang up
                    label = "Hang Up",
                    onClick = onHangUp,
                    buttonColor = MaterialTheme.colorScheme.error,
                    iconColor = MaterialTheme.colorScheme.onError
                )
            }
        }
    }
}

// You might need to create R.drawable.mic_on_icon and R.drawable.speaker_on_icon
// or use existing ones. The CallActionButton is assumed to be defined as in previous examples.

@Preview(showBackground = true, widthDp = 360, heightDp = 780)
@Composable
fun OngoingCallScreenPreview() {
    InAppCallingTheme {
        OngoingCallScreen(
            callerInfo = CallerInfo("Jane Doe", "(555) 987-6543", R.drawable.default_avatar),
            onHangUp = {}
        )
    }
}

// ---- IMPORTANT ----
// Ensure you have these (or similarly named) drawable resources in res/drawable:
// - default_avatar.xml (or .png, .jpg for the contact image)
// - mic_on_icon.xml (Vector Drawable for microphone on)
// - mic_off_icon.xml (Vector Drawable for microphone off)
// - speaker_on_icon.xml (Vector Drawable for speaker on)
// - speaker_off_icon.xml (Vector Drawable for speaker off)
// - call_end_icon.xml (Vector Drawable for ending a call)
