package com.dev.usbdigitalcommunityplatform.ui.home.vendor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dev.usbdigitalcommunityplatform.ui.cards.DigitalIDCard
import com.dev.usbdigitalcommunityplatform.ui.home.member.MemberRoutes
import com.dev.usbdigitalcommunityplatform.ui.home.member.ProfileViewModel
import com.dev.usbdigitalcommunityplatform.ui.theme.USBDigitalCommunityPlatformTheme

// ── Data classes ──────────────────────────────────────────────

data class VendorQuickAction(
    val icon: ImageVector,
    val label: String,
    val route: String,
    val accentColor: Color
)

// ── Route constants ─────────────────────────────────────────────
object VendorRoutes {
    const val HOME     = "vendor_home"
    const val LISTINGS = "vendor_listings"
    const val REQUESTS = "vendor_requests"
    const val CHAT     = "vendor_chat_list"
    const val PROFILE  = "vendor_profile_edit"
}

// ── Entry point ──────────────────────────────────────────────────

@Composable
fun VendorHomeScreen(
    viewModel: ProfileViewModel = viewModel(),
    onNavigate: (String) -> Unit = {}
) {
    val profile = viewModel.userProfile
    VendorHomeScreenContent(
        userName   = profile.name,
        userRole   = profile.role,
        phone      = profile.phoneNumber,
        state      = profile.state,
        occupation = profile.occupation,
        usbId      = profile.usbId,
        onNavigate = onNavigate
    )
}

// ── Main content ───────────────────────────────────────────────

@Composable
fun VendorHomeScreenContent(
    userName: String,
    userRole: String,
    phone: String,
    state: String,
    occupation: String,
    usbId: String = "",
    onNavigate: (String) -> Unit = {}
) {
    val quickActions = listOf(
        VendorQuickAction(Icons.Filled.Inventory2,        "My Listings", VendorRoutes.LISTINGS, Color(0xFFFF9500)),
        VendorQuickAction(Icons.AutoMirrored.Filled.Message, "Inquiries",   VendorRoutes.REQUESTS, Color(0xFF5856D6)),
        VendorQuickAction(Icons.Filled.Work,            "List Job",  MemberRoutes.JOBS, Color(0xFF007AFF)),
        VendorQuickAction(Icons.Filled.StarRate,          "Reviews",     "",                    Color(0xFFFF9F0A))
    )

    // NOTE: Bottom nav render nahi hota yaha — MainScreen.kt me Scaffold
    // ke bottomBar slot me ReusableBottomNav handle karta hai isse,
    // Member aur Vendor dono screens ke liye same shared bar.
    val context = LocalContext.current


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9F9F9))
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {



            // ── Greeting + Avatar ─────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Good Morning 👋", fontSize = 15.sp, color = Color(0xFF6C6C70))
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = userName.ifBlank { "Loading..." },
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF000000)
                    )
                    Spacer(Modifier.height(3.dp))
                    Text(
                        text = "USB Digital Community Platform",
                        fontSize = 12.sp,
                        color = Color(0xFF8E8E93)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF007AFF))
                        .clickable { onNavigate(VendorRoutes.PROFILE) },
                    contentAlignment = Alignment.Center
                ) {
                    val initials = userName
                        .split(" ")
                        .filter { it.isNotBlank() }
                        .mapNotNull { it.firstOrNull()?.uppercaseChar() }
                        .take(2)
                        .joinToString("")
                        .ifEmpty { "?" }
                    Text(
                        text = initials,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // ── Digital ID Card ────────────────────────────────
            Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                DigitalIDCard(
                    name       = userName.ifBlank { "Vendor" },
                    role       = userRole.ifBlank { "Vendor" },
                    userId     = usbId.ifBlank { "USB000000" },
                    phone      = phone.ifBlank { "+91 00000 00000" },
                    state      = state.ifBlank { "N/A" },
                    occupation = occupation.ifBlank { "N/A" }
                )
            }

            Spacer(Modifier.height(24.dp))

            // ── Quick Actions ──────────────────────────────────
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(
                    text = "Quick Actions",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF000000)
                )
                Spacer(Modifier.height(14.dp))

                val rows = quickActions.chunked(2)
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    rows.forEach { rowItems ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            rowItems.forEach { action ->
                                VendorQuickActionCard(
                                    action   = action,
                                    modifier = Modifier.weight(1f),
                                    onClick  = { if (action.route.isNotEmpty()) onNavigate(action.route) }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // ── Recent Activity ────────────────────────────────
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(
                    text = "Recent Activity",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF000000)
                )
                Spacer(Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(14.dp))
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No recent activity yet",
                        fontSize = 13.sp,
                        color = Color(0xFF8E8E93)
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Extra bottom padding — floating ReusableBottomNav (from MainScreen's
            // Scaffold) should not cover this content
            Spacer(Modifier.height(90.dp))
        }
    }
}

// ── Quick Action Card ─────────────────────────────────────────
@Composable
fun VendorQuickActionCard(
    action: VendorQuickAction,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .background(Color.White, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(action.accentColor.copy(alpha = 0.12f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = action.icon,
                contentDescription = action.label,
                tint = action.accentColor,
                modifier = Modifier.size(22.dp)
            )
        }
        Spacer(Modifier.height(8.dp))
        Text(
            text = action.label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF000000),
            textAlign = TextAlign.Center
        )
    }
}

// ── Preview ───────────────────────────────────────────────────

@Preview(showBackground = true, backgroundColor = 0xFFF9F9F9)
@Composable
fun VendorHomeScreenPreview() {
    USBDigitalCommunityPlatformTheme {
        VendorHomeScreenContent(
            userName   = "Sahil Vendor",
            userRole   = "Vendor",
            phone      = "+91 9876543210",
            state      = "Maharashtra",
            occupation = "Supplier",
            usbId      = "USB000002"
        )
    }
}