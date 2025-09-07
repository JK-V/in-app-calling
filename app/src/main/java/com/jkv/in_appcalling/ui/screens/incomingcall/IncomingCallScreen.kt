package com.jkv.in_appcalling.ui.screens.incomingcall

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jkv.in_appcalling.ui.model.CallerInfo
import com.jkv.in_appcalling.R
import com.jkv.in_appcalling.ui.theme.InAppCallingTheme

@Composable
fun IncomingCallScreen(
    callerInfo: CallerInfo,
    onAnswer: () -> Unit,
    onDecline: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse_transition")

    val answerButtonScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 700, delayMillis = 100),
            repeatMode = RepeatMode.Reverse
        ), label = "answer_button_pulse"
    )


    val declineButtonScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.10f, // Scale up to 110%
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800, delayMillis = 200), // Slightly different timing
            repeatMode = RepeatMode.Reverse
        ), label = "decline_button_pulse"
    )


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
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
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 60.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = callerInfo.avatarResId),
                    contentDescription = "Caller Avatar",
                    modifier = Modifier
                        .size(140.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondaryContainer),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = callerInfo.name,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(16.dp))
//                Text(
//                    text = callerInfo.number,
//                    style = MaterialTheme.typography.titleMedium,
//                    color = MaterialTheme.colorScheme.onSurfaceVariant
//                )
                Text(
                    text = "Incoming voice call",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 72.dp, start = 24.dp, end = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CallActionButton(
                    iconResId = R.drawable.call_decline_icon,
                    label = "Decline",
                    onClick = onDecline,
                    buttonColor = MaterialTheme.colorScheme.error,
                    iconColor = MaterialTheme.colorScheme.onError,
                    scale = declineButtonScale // Apply animation scale
                )

                // Optional: Respond with message button - uncomment and implement if needed
                // if (onRespondWithMessage != null) {
                //     CallActionButton(
                //         iconResId = R.drawable.ic_message, // Create this icon
                //         label = "Message",
                //         onClick = onRespondWithMessage,
                //         buttonColor = MaterialTheme.colorScheme.secondary,
                //         iconColor = MaterialTheme.colorScheme.onSecondary,
                //         scale = 1f // Or animate it too
                //     )
                // }

                CallActionButton(
                    iconResId = R.drawable.call_accept_icon,
                    label = "Answer",
                    onClick = onAnswer,
                    buttonColor = Color(0xFF00C853),
                    iconColor = Color.White,
                    scale = answerButtonScale
                )
            }
        }
    }
}

@Composable
fun CallActionButton(
    iconResId: Int,
    label: String,
    onClick: () -> Unit,
    buttonColor: Color,
    iconColor: Color,
    modifier: Modifier = Modifier,
    scale: Float = 1f
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier
                .scale(scale)
                .size(72.dp)
                .background(color = buttonColor, shape = CircleShape)
        ) {
            Icon(
                painter = painterResource(id = iconResId),
                contentDescription = label,
                tint = iconColor,
                modifier = Modifier.size(36.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 780)
@Composable
fun IncomingCallScreenPreview() {
    InAppCallingTheme {
        val previewCallerInfo = CallerInfo(
            name = "Alex Bell",
            number = "Incoming voice call",
            avatarResId = R.drawable.default_avatar
        )
        IncomingCallScreen(
            callerInfo = previewCallerInfo,
            onAnswer = { /* Preview action */ },
            onDecline = { /* Preview action */ }
        )
    }
}
