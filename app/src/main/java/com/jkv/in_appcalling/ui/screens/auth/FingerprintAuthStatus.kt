package com.jkv.in_appcalling.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jkv.in_appcalling.R
import com.jkv.in_appcalling.ui.theme.InAppCallingTheme

enum class FingerprintAuthStatus {
    IDLE,
    SCANNING,
    SUCCESS,
    ERROR,
    NOT_AVAILABLE
}

@Composable
fun FingerprintAuthScreenContent(
    modifier: Modifier,
    status: FingerprintAuthStatus,
    statusMessage: String,
    onCancel: (() -> Unit)? = null,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Biometric Authentication",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = when (status) {
                FingerprintAuthStatus.IDLE -> "Place your finger on the sensor to authenticate."
                FingerprintAuthStatus.SCANNING -> "Scanning..."
                FingerprintAuthStatus.SUCCESS -> "Authentication Successful!"
                FingerprintAuthStatus.ERROR -> "Authentication Failed."
                FingerprintAuthStatus.NOT_AVAILABLE -> "Fingerprint authentication is not available or not set up on this device."
            },
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        val iconColor = when (status) {
            FingerprintAuthStatus.IDLE, FingerprintAuthStatus.SCANNING -> MaterialTheme.colorScheme.primary
            FingerprintAuthStatus.SUCCESS -> Color(0xFF00C853)
            FingerprintAuthStatus.ERROR, FingerprintAuthStatus.NOT_AVAILABLE -> MaterialTheme.colorScheme.error
        }

        Image(
            painter = painterResource(id = R.drawable.fingerprint_svg),
            contentDescription = "Fingerprint Icon",
            modifier = Modifier.size(80.dp),
            colorFilter = ColorFilter.tint(iconColor)
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (statusMessage.isNotBlank()) {
            Text(
                text = statusMessage,
                style = MaterialTheme.typography.bodySmall,
                color = if (status == FingerprintAuthStatus.ERROR || status == FingerprintAuthStatus.NOT_AVAILABLE) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.height(40.dp)
            )
        } else {
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Preview(showBackground = true, name = "Idle State")
@Composable
fun FingerprintAuthScreenIdlePreview() {
    InAppCallingTheme {
        FingerprintAuthScreenContent(
            status = FingerprintAuthStatus.IDLE,
            statusMessage = "Touch the fingerprint sensor.",
            onCancel = {},
            modifier = Modifier
        )
    }
}

@Preview(showBackground = true, name = "Success State")
@Composable
fun FingerprintAuthScreenSuccessPreview() {
    InAppCallingTheme {
        FingerprintAuthScreenContent(
            status = FingerprintAuthStatus.SUCCESS,
            statusMessage = "Redirecting...",
            onCancel = {},
            modifier = Modifier
        )
    }
}

@Preview(showBackground = true, name = "Error State")
@Composable
fun FingerprintAuthScreenErrorPreview() {
    InAppCallingTheme {
        FingerprintAuthScreenContent(
            status = FingerprintAuthStatus.ERROR,
            statusMessage = "Too many attempts. Try again later.",
            onCancel = {},
            modifier = Modifier
        )
    }
}

@Preview(showBackground = true, name = "Not Available State")
@Composable
fun FingerprintAuthScreenNotAvailablePreview() {
    InAppCallingTheme {
        FingerprintAuthScreenContent(
            status = FingerprintAuthStatus.NOT_AVAILABLE,
            statusMessage = "Please enable fingerprint in your device settings.",
            onCancel = {},
            modifier = Modifier
        )
    }
}
