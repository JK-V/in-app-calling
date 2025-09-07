package com.jkv.in_appcalling.ui.screens.ongoingcall

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
import com.jkv.in_appcalling.R
import com.jkv.in_appcalling.ui.model.CallerInfo
import com.jkv.in_appcalling.ui.screens.incomingcall.CallActionButton
import com.jkv.in_appcalling.ui.theme.InAppCallingTheme
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

@Composable
fun OngoingCallScreen(
    callerInfo: CallerInfo,
    onHangUp: () -> Unit
) {
    var callDurationSeconds by remember { mutableStateOf(0L) }

    LaunchedEffect(key1 = Unit) {
        while (true) {
            delay(1000)
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
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
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
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Ongoing voice call ",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = formattedDuration,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 48.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CallActionButton(
                    iconResId = R.drawable.mic_on_icon,
                    label = "Mute",
                    onClick = { },
                    buttonColor = MaterialTheme.colorScheme.secondary,
                    iconColor = MaterialTheme.colorScheme.onSecondary
                )

                CallActionButton(
                    iconResId = R.drawable.speaker_on_icon,
                    label = "Speaker",
                    onClick = { },
                    buttonColor = MaterialTheme.colorScheme.secondary,
                    iconColor = MaterialTheme.colorScheme.onSecondary
                )

                CallActionButton(
                    iconResId = R.drawable.call_decline_icon,
                    label = "Hang Up",
                    onClick = onHangUp,
                    buttonColor = MaterialTheme.colorScheme.error,
                    iconColor = MaterialTheme.colorScheme.onError
                )
            }
        }
    }
}

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
