package com.dev.usbdigitalcommunityplatform.ui.home.member

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dev.usbdigitalcommunityplatform.ui.cards.DigitalIDCard
import com.dev.usbdigitalcommunityplatform.ui.theme.USBDigitalCommunityPlatformTheme

// ── Data classes ──────────────────────────────────────────────

data class QuickAction(
    val icon: String,
    val label: String,
    val route: String         // navigation ke liye
)

data class BottomNavItem(
    val icon: String,
    val label: String,
    val route: String
)

// ── Route constants — baad mein NavGraph mein use karo ────────
object MemberRoutes {
    const val HOME       = "member_home"
    const val JOBS       = "service_list/employer"      // Employers ki list
    const val LEGAL      = "service_list/lawyer"        // Lawyers ki list
    const val CA         = "service_list/ca"            // CAs ki list
    const val VENDORS    = "service_list/vendor"        // Vendors ki list
    const val COMMUNITY  = "community"
    const val NOTICES    = "notices"
    const val DOCUMENTS  = "documents"
    const val DISCOVER   = "discover"
    const val REQUESTS   = "requests"
    const val CHAT       = "chat_list"
    const val PROFILE    = "profile"
}

// ── Entry point ───────────────────────────────────────────────

@Composable
fun MemberHomeScreen(
    viewModel: ProfileViewModel = viewModel(),
    onNavigate: (String) -> Unit = {}
) {
    val profile = viewModel.userProfile
    MemberHomeScreenContent(
        userName   = profile.name,
        userRole   = profile.role,
        phone      = profile.phoneNumber,
        state      = profile.state,
        occupation = profile.occupation,
        usbId      = profile.usbId,
        onNavigate = onNavigate
    )
}

// ── Main content ──────────────────────────────────────────────

@Composable
fun MemberHomeScreenContent(
    userName: String,
    userRole: String,
    phone: String,
    state: String,
    occupation: String,
    usbId: String = "",
    onNavigate: (String) -> Unit = {}
) {
    // ── 7 Quick Actions ───────────────────────────────────────
    // Member kisse milta hai:
    //   Jobs      → Employers
    //   Legal     → Lawyers
    //   CA        → Chartered Accountants
    //   Vendors   → Business service providers
    //   Community → Events/Utsav
    //   Notices   → Announcements
    //   Documents → Apne docs save/share
    val quickActions = listOf(
        QuickAction("💼", "Jobs",        MemberRoutes.JOBS),
        QuickAction("⚖️", "Legal Help",  MemberRoutes.LEGAL),
        QuickAction("🧾", "CA / Finance",MemberRoutes.CA),
        QuickAction("🏪", "Vendors",     MemberRoutes.VENDORS),
        QuickAction("👥", "Community",   MemberRoutes.COMMUNITY),
        QuickAction("🔔", "Notices",     MemberRoutes.NOTICES),
        QuickAction("📁", "Documents",   MemberRoutes.DOCUMENTS)
    )

    // ── 5 Bottom Nav tabs ─────────────────────────────────────
    // Home → current screen
    // Discover → browse all service providers
    // Requests → sent/received requests
    // Chat → accepted connections ka chat
    // Profile → apna profile
    val bottomNavItems = listOf(
        BottomNavItem("🏠", "Home",     MemberRoutes.HOME),
        BottomNavItem("🔍", "Discover", MemberRoutes.DISCOVER),
        BottomNavItem("📨", "Requests", MemberRoutes.REQUESTS),
        BottomNavItem("💬", "Chat",     MemberRoutes.CHAT),
        BottomNavItem("👤", "Profile",  MemberRoutes.PROFILE)
    )

    var selectedTab by remember { mutableStateOf(MemberRoutes.HOME) }

    val latestUpdates = listOf(
        "New Government Scheme for Students 2024" to "2h ago",
        "Community Meeting on Sunday"             to "5h ago",
        "Document Submission Deadline Extended"   to "1d ago"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9F9F9))
    ) {
        // scrollable body — bottom nav ke upar
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {

            // ── Top Bar ───────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("≡", fontSize = 24.sp, color = Color(0xFF000000))
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "Notifications",
                    tint = Color(0xFF000000),
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onNavigate(MemberRoutes.NOTICES) }
                )
            }

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

                // Avatar — initials circle
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF007AFF))
                        .clickable { onNavigate(MemberRoutes.PROFILE) },
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

            // ── Digital ID Card ───────────────────────────────
            Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                DigitalIDCard(
                    name       = userName.ifBlank { "Member" },
                    role       = userRole.ifBlank { "Member" },
                    userId     = usbId.ifBlank { "USB000000" },
                    phone      = phone.ifBlank { "+91 00000 00000" },
                    state      = state.ifBlank { "N/A" },
                    occupation = occupation.ifBlank { "N/A" }
                )
            }

            Spacer(Modifier.height(24.dp))

            // ── Quick Actions (7 items — 3+3+1 layout) ────────
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(
                    text = "Quick Actions",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF000000)
                )
                Spacer(Modifier.height(14.dp))

                // Row 1: Jobs, Legal Help, CA/Finance
                // Row 2: Vendors, Community, Notices
                // Row 3: Documents (centered)
                val rows = quickActions.chunked(3)
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    rows.forEachIndexed { index, rowItems ->
                        if (rowItems.size == 3) {
                            // Full row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                rowItems.forEach { action ->
                                    QuickActionCard(
                                        action   = action,
                                        modifier = Modifier.weight(1f),
                                        onClick  = { onNavigate(action.route) }
                                    )
                                }
                            }
                        } else {
                            // Last row — Documents alone, centered
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                rowItems.forEach { action ->
                                    QuickActionCard(
                                        action   = action,
                                        modifier = Modifier.width(110.dp),
                                        onClick  = { onNavigate(action.route) }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // ── Latest Updates ────────────────────────────────
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Latest Updates",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF000000)
                    )
                    Text(
                        text = "See All",
                        fontSize = 14.sp,
                        color = Color(0xFF007AFF),
                        modifier = Modifier.clickable { onNavigate(MemberRoutes.NOTICES) }
                    )
                }

                Spacer(Modifier.height(12.dp))

                latestUpdates.forEach { (title, time) ->
                    UpdateItem(title = title, time = time)
                    Spacer(Modifier.height(8.dp))
                }
            }

            Spacer(Modifier.height(16.dp))
        }

        // ── Bottom Nav (fixed at bottom) ──────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            bottomNavItems.forEach { item ->
                BottomNavItemView(
                    icon       = item.icon,
                    label      = item.label,
                    isSelected = selectedTab == item.route,
                    onClick    = {
                        selectedTab = item.route
                        if (item.route != MemberRoutes.HOME) {
                            onNavigate(item.route)
                        }
                    }
                )
            }
        }
    }
}

// ── Quick Action Card ─────────────────────────────────────────

@Composable
fun QuickActionCard(
    action: QuickAction,
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
        Text(action.icon, fontSize = 26.sp)
        Spacer(Modifier.height(6.dp))
        Text(
            text = action.label,
            fontSize = 11.sp,
            color = Color(0xFF000000),
            textAlign = TextAlign.Center
        )
    }
}

// ── Update Item ───────────────────────────────────────────────

@Composable
fun UpdateItem(title: String, time: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(14.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(Color(0xFF007AFF), CircleShape)
            )
            Spacer(Modifier.width(10.dp))
            Column {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF000000)
                )
                Spacer(Modifier.height(2.dp))
                Text(text = time, fontSize = 12.sp, color = Color(0xFF8E8E93))
            }
        }
        Text("›", fontSize = 20.sp, color = Color(0xFF8E8E93))
    }
}

// ── Bottom Nav Item ───────────────────────────────────────────

@Composable
fun BottomNavItemView(
    icon: String,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Text(icon, fontSize = 22.sp)
        Spacer(Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            color      = if (isSelected) Color(0xFF007AFF) else Color(0xFF8E8E93),
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

// ── Preview ───────────────────────────────────────────────────

@Preview(showBackground = true, backgroundColor = 0xFFF9F9F9)
@Composable
fun MemberHomeScreenPreview() {
    USBDigitalCommunityPlatformTheme {
        MemberHomeScreenContent(
            userName   = "Sahil Maske",
            userRole   = "Member",
            phone      = "+91 9876543210",
            state      = "Maharashtra",
            occupation = "Developer",
            usbId      = "USB000001"
        )
    }
}