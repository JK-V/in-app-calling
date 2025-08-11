package com.jkv.in_appcalling

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jkv.in_appcalling.ui.theme.InAppCallingTheme

@Composable
fun IncomingCallScreen(
    callerInfo: CallerInfo,
    onAnswer: () -> Unit,
    onDecline: () -> Unit,
    // onRespondWithMessage: () -> Unit // Optional for "Quick Reply"
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            // Using a gradient or specific image can also be nice for call screens
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(), // Column takes full size of the Box
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Caller Info Section (Top/Center) - Takes up most space
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center, // Center content vertically in this part
                modifier = Modifier.weight(1f) // Pushes buttons to the bottom
            ) {
                Spacer(modifier = Modifier.height(32.dp)) // Top padding
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
                    style = MaterialTheme.typography.displaySmall, // Larger text for name
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Incoming Call",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.weight(1f)) // Pushes content above this down a bit if screen is tall
            }

            // Action Buttons Section (Bottom)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        bottom = 48.dp,
                        start = 16.dp,
                        end = 16.dp
                    ), // Padding from the bottom edge
                horizontalArrangement = Arrangement.SpaceAround, // SpaceEvenly also works well
                verticalAlignment = Alignment.CenterVertically
            ) {
                CallActionButton(
                    iconResId = R.drawable.call_decline_icon, // Ensure this exists
                    label = "Decline",
                    onClick = onDecline,
                    buttonColor = MaterialTheme.colorScheme.error, // Standard Material red for errors/decline
                    iconColor = MaterialTheme.colorScheme.onError
                )

                CallActionButton(
                    iconResId = R.drawable.call_accept_icon, // Ensure this exists
                    label = "Answer",
                    onClick = onAnswer,
                    buttonColor = Color(0xFF00897B), // A nice Teal/Green for Answer
                    iconColor = Color.White
                )
            }

            // Optional: Respond with message button (could be placed above the main action buttons)
            // TextButton(
            //     onClick = { /* onRespondWithMessage() */ },
            //     modifier = Modifier.padding(bottom = 16.dp)
            // ) {
            //     Icon(painterResource(id = R.drawable.message_icon), contentDescription = "Quick Reply")
            //     Spacer(modifier = Modifier.width(8.dp))
            //     Text("Respond with Message")
            // }
        }
    }
}

@Composable
fun CallActionButton(
    @DrawableRes iconResId: Int,
    label: String,
    onClick: () -> Unit,
    buttonColor: Color,
    iconColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        FilledIconButton(
            onClick = onClick,
            modifier = Modifier.size(72.dp), // Larger buttons for call actions
            shape = CircleShape,
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = buttonColor,
                contentColor = iconColor
            )
        ) {
            Icon(
                painter = painterResource(id = iconResId),
                contentDescription = label,
                modifier = Modifier.size(36.dp) // Larger icon within the button
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = label, color = MaterialTheme.colorScheme.onSurface)
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 780)
@Composable
fun IncomingCallScreenPreview() {
    val sampleCallerInfo = CallerInfo(
        name = "John Appleseed",
        number = "(555) 123-4567",
        avatarResId = R.drawable.default_avatar // Make sure R.drawable.default_avatar exists
    )
    InAppCallingTheme { // Replace with your actual theme name
        IncomingCallScreen(
            callerInfo = sampleCallerInfo,
            onAnswer = { println("Preview: Call Answered") },
            onDecline = { println("Preview: Call Declined") }
            // onRespondWithMessage = { println("Preview: Respond with message") }
        )
    }
}