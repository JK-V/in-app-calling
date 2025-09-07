package com.jkv.in_appcalling.ui.screens.callingprofile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jkv.in_appcalling.ui.model.CallLogItem
import com.jkv.in_appcalling.ui.model.ContactDetails
import com.jkv.in_appcalling.R
import com.jkv.in_appcalling.ui.theme.InAppCallingTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CallingProfileScreen(
    contact: ContactDetails,
    callHistory: List<CallLogItem>,
    onCall: (String) -> Unit,
    onMessage: (String) -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(contact.name) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Image(
                    painter = painterResource(id = contact.avatarResId),
                    contentDescription = "${contact.name} Avatar",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondaryContainer),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = contact.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = contact.phoneNumber,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = { onCall(contact.phoneNumber) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.call_icon),
                            contentDescription = "Call",
                            modifier = Modifier.size(ButtonDefaults.IconSize)
                        )
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text("Call")
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                Text(
                    text = "Details",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
                Divider()
            }

            if (contact.email != null) {
                item {
                    ProfileDetailItem(label = "Email", value = contact.email)
                }
            }
            if (contact.address != null) {
                item {
                    ProfileDetailItem(label = "Address", value = contact.address)
                }
            }

            if (callHistory.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Call History",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )
                    Divider()
                }
                items(callHistory) { logItem ->
                    CallHistoryItemRow(logItem = logItem)
                }
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun ProfileDetailItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(0.4f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(0.6f)
        )
    }
}

@Composable
fun CallHistoryItemRow(logItem: CallLogItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = logItem.type,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = logItem.date,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (logItem.duration != null) {
                Text(
                    text = logItem.duration,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 780)
@Composable
fun CallingProfileScreenPreview() {
    val sampleContact = ContactDetails(
        id = "1",
        name = "Jane Doe",
        phoneNumber = "+1 (123) 456-7890",
        avatarResId = R.drawable.default_avatar,
        email = "jane.doe@example.com",
        address = "123 Main St, Anytown, USA"
    )

    val sampleCallHistory = listOf(
        CallLogItem(
            id = "log1",
            type = "Outgoing Call",
            date = "Yesterday, 10:30 AM",
            duration = "5 min 20s"
        ),
        CallLogItem(
            id = "log2",
            type = "Incoming Call",
            date = "Yesterday, 2:15 PM",
            duration = "10 min 05s"
        ),
        CallLogItem(id = "log3", type = "Missed Call", date = "Today, 9:00 AM", duration = null),
        CallLogItem(
            id = "log4",
            type = "Outgoing Call",
            date = "Today, 11:45 AM",
            duration = "0 min 30s"
        )
    )

    InAppCallingTheme {
        CallingProfileScreen(
            contact = sampleContact,
            callHistory = sampleCallHistory,
            onCall = { phoneNumber ->
                println("Preview: Call initiated to $phoneNumber")
            },
            onMessage = { phoneNumber ->
                println("Preview: Message initiated to $phoneNumber")
            },
            onNavigateBack = {
                println("Preview: Navigate back clicked")
            }
        )
    }
}
