package com.jkv.in_appcalling

// Potentially in a data model file
data class ContactDetails(
    val id: String,
    val name: String,
    val phoneNumber: String,
    val avatarResId: Int, // Drawable resource ID for the avatar
    val email: String? = null,
    val address: String? = null
)

// Simplified for now, can be expanded for actual call logs
data class CallLogItem(
    val id: String,
    val type: String, // e.g., "Outgoing", "Incoming", "Missed"
    val date: String,
    val duration: String? = null
)