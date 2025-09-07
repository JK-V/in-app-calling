package com.jkv.in_appcalling.ui.model

data class ContactDetails(
    val id: String,
    val name: String,
    val phoneNumber: String,
    val avatarResId: Int,
    val email: String? = null,
    val address: String? = null
)

data class CallLogItem(
    val id: String,
    val type: String, // e.g., "Outgoing", "Incoming", "Missed"
    val date: String,
    val duration: String? = null
)