package com.dev.usbdigitalcommunityplatform.ui.home.common

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Data class representing a bottom navigation item.
 */
data class NavItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)
