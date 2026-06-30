package com.dev.usbdigitalcommunityplatform.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
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
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import com.dev.usbdigitalcommunityplatform.ui.auth.RoleSelectionScreen
import com.dev.usbdigitalcommunityplatform.ui.auth.LanguageSelectionScreen
import com.dev.usbdigitalcommunityplatform.ui.auth.LoginScreen
import com.dev.usbdigitalcommunityplatform.ui.auth.OtpScreen
import com.dev.usbdigitalcommunityplatform.ui.auth.ProfileSetupScreen
import com.dev.usbdigitalcommunityplatform.ui.auth.SplashScreen
import com.dev.usbdigitalcommunityplatform.ui.home.admin.AdminHomeScreen
import com.dev.usbdigitalcommunityplatform.ui.home.member.MemberHomeScreen
import com.dev.usbdigitalcommunityplatform.ui.home.employer.EmployerHomeScreen
import com.dev.usbdigitalcommunityplatform.ui.home.lawyer.LawyerHomeScreen
import com.dev.usbdigitalcommunityplatform.ui.home.ca.CAHomeScreen
import com.dev.usbdigitalcommunityplatform.ui.home.member.MemberRoutes
import com.dev.usbdigitalcommunityplatform.ui.home.member.ServiceListScreen
import com.dev.usbdigitalcommunityplatform.ui.home.member.JobListScreen
import com.dev.usbdigitalcommunityplatform.ui.home.member.MyRequestsScreen
import com.dev.usbdigitalcommunityplatform.ui.home.vendor.VendorHomeScreen

// iOS animation — spring feel
// tween(400) = smooth, easeInOut = iOS jaisa curve
private const val ANIM_DURATION = 400

@Composable
fun AppNavGraph() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {

        // ═══════════════════════════════════════════════════════
        // AUTH FLOW
        // ═══════════════════════════════════════════════════════

        composable(
            route = "splash",
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(ANIM_DURATION))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(ANIM_DURATION))
            }
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
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(ANIM_DURATION))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(ANIM_DURATION))
            },
            popEnterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(ANIM_DURATION))
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(ANIM_DURATION))
            }
        ) {
            LanguageSelectionScreen(
                onContinue = { navController.navigate("login") }
            )
        }

        composable(
            route = "login",
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(ANIM_DURATION))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(ANIM_DURATION))
            },
            popEnterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(ANIM_DURATION))
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(ANIM_DURATION))
            }
        ) {
            LoginScreen(
                onLoginSuccess = { navController.navigate("otp") }
            )
        }

        composable(
            route = "otp",
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(ANIM_DURATION))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(ANIM_DURATION))
            },
            popEnterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(ANIM_DURATION))
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(ANIM_DURATION))
            }
        ) {
            OtpScreen(
                onVerify = {
                    navController.navigate("profile_setup") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = "profile_setup",
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(ANIM_DURATION))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(ANIM_DURATION))
            },
            popEnterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(ANIM_DURATION))
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(ANIM_DURATION))
            }
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
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(ANIM_DURATION))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(ANIM_DURATION))
            },
            popEnterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(ANIM_DURATION))
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(ANIM_DURATION))
            }
        ) {
            RoleSelectionScreen(
                navController = navController,
                onRoleSelected = {}
            )
        }

        // ═══════════════════════════════════════════════════════
        // HOME SCREENS (per role)
        // ═══════════════════════════════════════════════════════

        composable(
            route = "admin_home",
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(ANIM_DURATION))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(ANIM_DURATION))
            }
        ) { AdminHomeScreen() }

        composable(
            route = "member_home",
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(ANIM_DURATION))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(ANIM_DURATION))
            }
        ) {
            MemberHomeScreen(
                onNavigate = { route ->
                    navController.navigate(route)
                }
            )
        }

        composable(
            route = "employer_home",
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(ANIM_DURATION))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(ANIM_DURATION))
            }
        ) { EmployerHomeScreen() }

        composable(
            route = "lawyer_home",
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(ANIM_DURATION))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(ANIM_DURATION))
            }
        ) { LawyerHomeScreen() }

        composable(
            route = "ca_home",
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(ANIM_DURATION))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(ANIM_DURATION))
            }
        ) { CAHomeScreen() }

        composable(
            route = "vendor_home",
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(ANIM_DURATION))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(ANIM_DURATION))
            }
        ) { VendorHomeScreen() }

        // ═══════════════════════════════════════════════════════
        // MEMBER QUICK ACTIONS — connection/service screens
        // ═══════════════════════════════════════════════════════

        // Jobs → JobListScreen (Job Listing model — seedha Apply, no message)
        composable(
            route = MemberRoutes.JOBS,
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(ANIM_DURATION))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(ANIM_DURATION))
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(ANIM_DURATION))
            }
        ) {
            JobListScreen(
                onBack = { navController.popBackStack() }
            )
        }

        // Legal Help → ServiceListScreen (Profile model — message + request)
        composable(
            route = MemberRoutes.LEGAL,
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(ANIM_DURATION))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(ANIM_DURATION))
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(ANIM_DURATION))
            }
        ) {
            ServiceListScreen(
                role   = "lawyer",
                onBack = { navController.popBackStack() }
            )
        }

        // CA / Finance → ServiceListScreen (Profile model)
        composable(
            route = MemberRoutes.CA,
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(ANIM_DURATION))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(ANIM_DURATION))
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(ANIM_DURATION))
            }
        ) {
            ServiceListScreen(
                role   = "ca",
                onBack = { navController.popBackStack() }
            )
        }

        // Vendors → ServiceListScreen (Profile model)
        composable(
            route = MemberRoutes.VENDORS,
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(ANIM_DURATION))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(ANIM_DURATION))
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(ANIM_DURATION))
            }
        ) {
            ServiceListScreen(
                role   = "vendor",
                onBack = { navController.popBackStack() }
            )
        }

        // Requests tab (bottom nav) → MyRequestsScreen (sent requests + status)
        composable(
            route = MemberRoutes.REQUESTS,
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(ANIM_DURATION))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(ANIM_DURATION))
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(ANIM_DURATION))
            }
        ) {
            MyRequestsScreen(
                onBack     = { navController.popBackStack() },
                onOpenChat = { toUserId ->
                    // TODO: Chat screen banne ke baad yahan navigate karna
                    // navController.navigate("chat/$toUserId")
                }
            )
        }

        // ── TODO: Baad mein banane wali screens ──────────────
        composable(MemberRoutes.COMMUNITY) { ScreenPlaceholder("Community") { navController.popBackStack() } }
        composable(MemberRoutes.NOTICES)   { ScreenPlaceholder("Notices")   { navController.popBackStack() } }
        composable(MemberRoutes.DOCUMENTS) { ScreenPlaceholder("Documents") { navController.popBackStack() } }
        composable(MemberRoutes.DISCOVER)  { ScreenPlaceholder("Discover")  { navController.popBackStack() } }
        composable(MemberRoutes.CHAT)      { ScreenPlaceholder("Chat List") { navController.popBackStack() } }
        composable(MemberRoutes.PROFILE)   { ScreenPlaceholder("Profile")   { navController.popBackStack() } }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenPlaceholder(title: String, onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Outlined.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Text("$title Screen Coming Soon", fontSize = 18.sp, color = Color.Gray)
        }
    }
}