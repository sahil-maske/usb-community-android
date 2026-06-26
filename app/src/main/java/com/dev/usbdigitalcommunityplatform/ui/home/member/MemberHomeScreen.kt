package com.dev.usbdigitalcommunityplatform.ui.home.member

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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

// quick action item
data class QuickAction(val icon: String, val label: String)

@Composable
fun MemberHomeScreen(viewModel: ProfileViewModel = viewModel()) {
    val profile = viewModel.userProfile
    MemberHomeScreenContent(
        userName = profile.name,
        userRole = profile.role,
        phone = profile.phone,
        state = profile.state,
        occupation = profile.occupation
    )
}

@Composable
fun MemberHomeScreenContent(
    userName: String,
    userRole: String,
    phone: String,
    state: String,
    occupation: String
) {

    val quickActions = listOf(
        QuickAction("💼", "Jobs"),
        QuickAction("👥", "Community"),
        QuickAction("📄", "Documents"),
        QuickAction("❓", "Help"),
        QuickAction("🔔", "Notices"),
        QuickAction("📅", "Events")
    )

    val latestUpdates = listOf(
        "New Government Scheme for Students 2024" to "2h ago",
        "Community Meeting on Sunday" to "5h ago",
        "Document Submission Deadline" to "1d ago"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9F9F9))
            .verticalScroll(rememberScrollState())
    ) {

        // ── Top Bar ───────────────────────────────────────────
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
                modifier = Modifier.size(24.dp)
            )
        }

        // ── Greeting + Avatar ─────────────────────────────────
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
                Text("USB Digital Community Platform", fontSize = 12.sp, color = Color(0xFF8E8E93))
            }

            // avatar circle with initials
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF007AFF)),
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

        // ── Digital ID Card ───────────────────────────────────
        Box(modifier = Modifier.padding(horizontal = 20.dp)) {
            DigitalIDCard(
                name = userName.ifBlank { "Member" },
                role = userRole.ifBlank { "Member" },
                userId = "USB000154",
                phone = phone.ifBlank { "+91 00000 00000" },
                state = state.ifBlank { "N/A" },
                occupation = occupation.ifBlank { "N/A" }
            )
        }

        Spacer(Modifier.height(24.dp))

        // ── Quick Actions ─────────────────────────────────────
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Text("Quick Actions", fontSize = 17.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF000000))
            Spacer(Modifier.height(14.dp))

            // 3 columns, 2 rows
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                quickActions.chunked(3).forEach { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        rowItems.forEach { action ->
                            QuickActionCard(action = action, modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // ── Latest Updates ────────────────────────────────────
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Latest Updates", fontSize = 17.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF000000))
                Text("See All", fontSize = 14.sp, color = Color(0xFF007AFF))
            }

            Spacer(Modifier.height(12.dp))

            latestUpdates.forEach { (title, time) ->
                UpdateItem(title = title, time = time)
                Spacer(Modifier.height(8.dp))
            }
        }

        Spacer(Modifier.height(32.dp))

        // ── Bottom Nav ────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            BottomNavItem(icon = "🏠", label = "Home", isSelected = true)
            BottomNavItem(icon = "💼", label = "Jobs", isSelected = false)
            BottomNavItem(icon = "👥", label = "Community", isSelected = false)
            BottomNavItem(icon = "👤", label = "Profile", isSelected = false)
        }
    }
}

// ── Quick Action Card ─────────────────────────────────────────
@Composable
fun QuickActionCard(action: QuickAction, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(action.icon, fontSize = 26.sp)
        Spacer(Modifier.height(6.dp))
        Text(action.label, fontSize = 12.sp, color = Color(0xFF000000))
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
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(Color(0xFF007AFF), CircleShape)
            )
            Spacer(Modifier.width(10.dp))
            Column {
                Text(title, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF000000))
                Spacer(Modifier.height(2.dp))
                Text(time, fontSize = 12.sp, color = Color(0xFF8E8E93))
            }
        }
        Text("›", fontSize = 20.sp, color = Color(0xFF8E8E93))
    }
}

// ── Bottom Nav Item ───────────────────────────────────────────
@Composable
fun BottomNavItem(icon: String, label: String, isSelected: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(icon, fontSize = 22.sp)
        Spacer(Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            color = if (isSelected) Color(0xFF007AFF) else Color(0xFF8E8E93),
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF9F9F9)
@Composable
fun MemberHomeScreenPreview() {
    USBDigitalCommunityPlatformTheme {
        MemberHomeScreenContent(
            userName = "Sahil Maske",
            userRole = "Member",
            phone = "+91 9876543210",
            state = "Maharashtra",
            occupation = "Developer"
        )
    }
}