package com.dev.usbdigitalcommunityplatform.ui.model

import androidx.compose.ui.semantics.Role

data class UserProfile(
    val uid: String ="",
    val phoneNumber: String = "",
    val name: String = "",
    val role: String = "",
    val language: String = "",
    val state: String = "",
    val occupation: String = "",
     val phone: String = "",
)