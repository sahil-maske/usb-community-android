package com.dev.usbdigitalcommunityplatform.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.compose.composable
import com.dev.usbdigitalcommunityplatform.ui.auth.RoleSelectionScreen
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
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
import com.dev.usbdigitalcommunityplatform.ui.home.vendor.VendorHomeScreen

@Composable
fun AppNavGraph() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {

        composable(
            route = "splash",
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(300)
                )
            }
        ) {
            SplashScreen(
                onTimeout = {
                    navController.navigate("language") {
                        popUpTo("splash") {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(
            route = "language",
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(300)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(300)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(300)
                )
            }
        ) {
            LanguageSelectionScreen(
                onContinue = {
                    navController.navigate("login")
                }
            )
        }

        composable(
            route = "login",
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(300)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(300)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(300)
                )
            }
        ) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("otp")
                }
            )
        }

        composable(
            route = "otp",
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(300)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(300)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(300)
                )
            }
        ) {
            OtpScreen(
                onVerify = {
                    navController.navigate("profile_setup") {
                        popUpTo("login") {
                            inclusive = true
                        }
                    }
                }
            )
        }


        composable(
            route = "profile_setup",
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(300)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(300)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(300)
                )
            }
        ) {
            ProfileSetupScreen(
                onComplete = {
                    navController.navigate("role_selection")
                },
                onAdminDetected = {
                    navController.navigate("admin_home")
                }
            )
        }
        composable("role_selection") {

            RoleSelectionScreen(
                navController = navController,
                onRoleSelected = { role ->

                    // Firestore save later

                }
            )
        }

        composable("admin_home") { AdminHomeScreen() }
        composable("member_home") { MemberHomeScreen() }
        composable("employer_home") { EmployerHomeScreen() }
        composable("lawyer_home") { LawyerHomeScreen() }
        composable("ca_home") { CAHomeScreen() }
        composable("vendor_home") { VendorHomeScreen() }
    }
}
