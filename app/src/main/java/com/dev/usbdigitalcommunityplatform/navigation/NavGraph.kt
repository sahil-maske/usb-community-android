package com.dev.usbdigitalcommunityplatform.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.dev.usbdigitalcommunityplatform.ui.auth.LanguageSelectionScreen
import com.dev.usbdigitalcommunityplatform.ui.auth.LoginScreen
import com.dev.usbdigitalcommunityplatform.ui.auth.OtpScreen
import com.dev.usbdigitalcommunityplatform.ui.auth.ProfileSetupScreen
import com.dev.usbdigitalcommunityplatform.ui.auth.RoleSelectionScreen
import com.dev.usbdigitalcommunityplatform.ui.auth.SplashScreen
import com.dev.usbdigitalcommunityplatform.ui.settings.BatteryOptimizationGuideScreen
import com.dev.usbdigitalcommunityplatform.ui.home.admin.AdminHomeScreen
import com.dev.usbdigitalcommunityplatform.ui.home.member.JobListScreen
import com.dev.usbdigitalcommunityplatform.ui.home.member.MemberHomeScreen
import com.dev.usbdigitalcommunityplatform.ui.home.member.MemberRoutes
import com.dev.usbdigitalcommunityplatform.ui.home.vendor.VendorHomeScreen
// 🔽 CA & Legal screens
import com.dev.usbdigitalcommunityplatform.ui.home.ca.CAScreen
import com.dev.usbdigitalcommunityplatform.ui.home.legal.LegalHomeScreen
// 🔽 Reusable bottom nav (shared between Member & Vendor)
import com.dev.usbdigitalcommunityplatform.ui.home.vendor.VendorRoutes

private const val ANIM_DURATION = 400

@Composable
fun AppNavGraph(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {

        // ═══════════════════════════════════════════════════════
        // AUTH FLOW
        // ═══════════════════════════════════════════════════════

        composable(
            route = "splash",
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(ANIM_DURATION)) },
            exitTransition  = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(ANIM_DURATION)) }
        ) {
            SplashScreen(
                onNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = "language",
            enterTransition    = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left,  tween(ANIM_DURATION)) },
            exitTransition     = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left,  tween(ANIM_DURATION)) },
            popEnterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(ANIM_DURATION)) },
            popExitTransition  = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(ANIM_DURATION)) }
        ) {
            LanguageSelectionScreen(onContinue = { navController.navigate("login") })
        }

        composable(
            route = "login",
            enterTransition    = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left,  tween(ANIM_DURATION)) },
            exitTransition     = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left,  tween(ANIM_DURATION)) },
            popEnterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(ANIM_DURATION)) },
            popExitTransition  = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(ANIM_DURATION)) }
        ) {
            LoginScreen(
                onOtpSent = { navController.navigate("otp") },
                onAutoVerified = { routeAfterAuth(navController) }
            )
        }

        composable(
            route = "otp",
            enterTransition    = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left,  tween(ANIM_DURATION)) },
            exitTransition     = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left,  tween(ANIM_DURATION)) },
            popEnterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(ANIM_DURATION)) },
            popExitTransition  = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(ANIM_DURATION)) }
        ) {
            OtpScreen(
                onVerify = { routeAfterAuth(navController) }
            )
        }

        composable(
            route = "profile_setup",
            enterTransition    = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left,  tween(ANIM_DURATION)) },
            exitTransition     = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left,  tween(ANIM_DURATION)) },
            popEnterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(ANIM_DURATION)) },
            popExitTransition  = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(ANIM_DURATION)) }
        ) {
            ProfileSetupScreen(
                onComplete = { navController.navigate("role_selection") },
                onAdminDetected = {
                    navController.navigate("admin_home") {
                        popUpTo("profile_setup") { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = "role_selection",
            enterTransition    = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left,  tween(ANIM_DURATION)) },
            exitTransition     = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left,  tween(ANIM_DURATION)) },
            popEnterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(ANIM_DURATION)) },
            popExitTransition  = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(ANIM_DURATION)) }
        ) {
            // NOTE: RoleSelectionScreen ab sirf Member aur Vendor dikhayega (Admin hidden/auto-detected)
            RoleSelectionScreen(navController = navController, onRoleSelected = {})
        }

        // ═══════════════════════════════════════════════════════
        // HOME SCREENS (per role) — sirf 3 roles: Member, Vendor, Admin
        // ═══════════════════════════════════════════════════════

        composable(
            route = "admin_home",
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(ANIM_DURATION)) },
            exitTransition  = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(ANIM_DURATION)) }
        ) { AdminHomeScreen() }

        composable(
            route = "member_home",
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(ANIM_DURATION)) },
            exitTransition  = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(ANIM_DURATION)) }
        ) {
            MemberHomeScreen(onNavigate = { route -> navController.navigate(route) })
        }

        // ⭐ vendor_home — same wrapper, vendor ke apne nav items ke saath
        composable(
            route = "vendor_home",
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(ANIM_DURATION)) },
            exitTransition  = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(ANIM_DURATION)) }
        ) {
            VendorHomeScreen(onNavigate = { route -> navController.navigate(route) })
        }

        composable(
            route = "battery_optimization_guide",
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up, tween(ANIM_DURATION)) },
            exitTransition  = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Down, tween(ANIM_DURATION)) }
        ) {
            BatteryOptimizationGuideScreen(onBack = { navController.popBackStack() })
        }

        // ═══════════════════════════════════════════════════════
        // MEMBER QUICK ACTIONS
        // ═══════════════════════════════════════════════════════

        // Jobs → JobListScreen (Apply model)
        composable(
            route = MemberRoutes.JOBS,
            enterTransition   = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left,  tween(ANIM_DURATION)) },
            exitTransition    = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left,  tween(ANIM_DURATION)) },
            popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(ANIM_DURATION)) }
        ) {
            JobListScreen(onBack = { navController.popBackStack() })
        }

        // CA Finance
        composable(
            route = MemberRoutes.CA_FINANCE,
            enterTransition   = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left,  tween(ANIM_DURATION)) },
            exitTransition    = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left,  tween(ANIM_DURATION)) },
            popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(ANIM_DURATION)) }
        ) {
            CAScreen(onBack = { navController.popBackStack() })
        }

        // Legal Help
        composable(
            route = MemberRoutes.LEGAL_HELP,
            enterTransition   = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left,  tween(ANIM_DURATION)) },
            exitTransition    = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left,  tween(ANIM_DURATION)) },
            popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(ANIM_DURATION)) }
        ) {
            LegalHomeScreen(onBack = { navController.popBackStack() })
        }

        // 🔽 Vendors (placeholder — real ServiceListScreen jab bane, isse replace kar dena)
        composable(
            route = MemberRoutes.VENDORS,
            enterTransition   = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left,  tween(ANIM_DURATION)) },
            exitTransition    = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left,  tween(ANIM_DURATION)) },
            popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(ANIM_DURATION)) }
        ) {
            ComingSoonScreen(
                title = "Vendors",
                icon = "🏪",
                description = "Browse verified vendors and business service\nproviders in your area. Coming soon.",
                onBack = { navController.popBackStack() }
            )
        }

        // 🔽 Community
        composable(
            route = MemberRoutes.COMMUNITY,
            enterTransition   = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left,  tween(ANIM_DURATION)) },
            exitTransition    = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left,  tween(ANIM_DURATION)) },
            popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(ANIM_DURATION)) }
        ) {
            ComingSoonScreen(
                title = "Community",
                icon = "👥",
                description = "Connect with your community — events, discussions,\nand local gatherings will appear here soon.",
                onBack = { navController.popBackStack() }
            )
        }

        // 🔽 Notices (Notifications icon click se yaha aata hai)
        composable(
            route = MemberRoutes.NOTICES,
            enterTransition   = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left,  tween(ANIM_DURATION)) },
            exitTransition    = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left,  tween(ANIM_DURATION)) },
            popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(ANIM_DURATION)) }
        ) {
            ComingSoonScreen(
                title = "Notices",
                icon = "🔔",
                description = "All announcements, government scheme updates,\nand community notices will show up here.",
                onBack = { navController.popBackStack() }
            )
        }

        // 🔽 Profile — ScreenWithBottomNav se wrap kiya taaki nav Profile pe bhi dikhe
        composable(
            route = MemberRoutes.PROFILE,
            enterTransition   = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left,  tween(ANIM_DURATION)) },
            exitTransition    = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left,  tween(ANIM_DURATION)) },
            popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(ANIM_DURATION)) }
        ) {
            ComingSoonScreen(
                title = "Profile",
                icon = "👤",
                description = "Full profile editing, ID card details, and settings\nwill be available here soon.",
                onBack = { navController.popBackStack() }
            )
        }

        // 🔽 Documents, Discover, Requests, Chat (abhi placeholder)
        composable(MemberRoutes.DOCUMENTS) {
            ComingSoonScreen(
                title = "Documents",
                icon = "📄",
                description = "Upload and manage your important documents\nsecurely from here.",
                onBack = { navController.popBackStack() }
            )
        }

        composable(MemberRoutes.DISCOVER) {
            ComingSoonScreen(
                title = "Discover",
                icon = "🔍",
                description = "Browse all service providers across every\ncategory in one place.",
                onBack = { navController.popBackStack() }
            )
        }

        composable(MemberRoutes.REQUESTS) {
            ComingSoonScreen(
                title = "Requests",
                icon = "📨",
                description = "Track requests you've sent and received\nfrom other members.",
                onBack = { navController.popBackStack() }
            )
        }

        composable(MemberRoutes.CHAT) {
            ComingSoonScreen(
                title = "Chat",
                icon = "💬",
                description = "Your conversations with accepted connections\nwill appear here.",
                onBack = { navController.popBackStack() }
            )
        }

        // ═══════════════════════════════════════════════════════
        // VENDOR SPECIFIC ROUTES
        // ═══════════════════════════════════════════════════════

    }
}

// ── Shared post-auth routing logic (used by both OTP verify and instant auto-verify) ──

private fun routeAfterAuth(navController: NavHostController) {
    val uid = FirebaseAuth.getInstance().currentUser?.uid

    if (uid == null) {
        // Safety fallback — kabhi na ho, but agar Auth session missing hai
        navController.navigate("profile_setup") {
            popUpTo("login") { inclusive = true }
        }
        return
    }

    FirebaseFirestore.getInstance()
        .collection("users")
        .document(uid)
        .get()
        .addOnSuccessListener { snapshot ->
            val name = snapshot.getString("name")
            val role = snapshot.getString("role")

            val destination = when {
                // Naya user, ya profile setup abhi nahi hua
                !snapshot.exists() || name.isNullOrBlank() -> "profile_setup"
                // Profile hai but role select nahi kiya abhi tak
                role.isNullOrBlank() -> "role_selection"
                // Role already selected hai — seedha uske home screen pe bhejo
                role.equals("Vendor", ignoreCase = true) -> "vendor_home"
                role.equals("Admin", ignoreCase = true)  -> "admin_home"
                else -> "member_home" // default/Member
            }

            navController.navigate(destination) {
                popUpTo("login") { inclusive = true }
            }
        }
        .addOnFailureListener {
            // Firestore fetch fail hua (network issue, etc.) — safe fallback
            navController.navigate("profile_setup") {
                popUpTo("login") { inclusive = true }
            }
        }
}

// ── Reusable "Coming Soon" screen — better UI, used across all pending features ──

private val ComingSoonBlue = Color(0xFF007AFF)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComingSoonScreen(
    title: String,
    icon: String,
    description: String,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title, fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .background(ComingSoonBlue.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(icon, fontSize = 40.sp)
            }

            Spacer(Modifier.height(24.dp))

            Text(
                text = "$title — Coming Soon",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF000000),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(10.dp))

            Text(
                text = description,
                fontSize = 14.sp,
                color = Color(0xFF8E8E93),
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )
        }
    }
}