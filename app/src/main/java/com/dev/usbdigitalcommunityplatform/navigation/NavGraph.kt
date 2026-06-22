package com.dev.usbdigitalcommunityplatform.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import com.dev.usbdigitalcommunityplatform.ui.auth.LanguageSelectionScreen
import com.dev.usbdigitalcommunityplatform.ui.auth.LoginScreen
import com.dev.usbdigitalcommunityplatform.ui.auth.OtpScreen
import com.dev.usbdigitalcommunityplatform.ui.auth.ProfileSetupScreen
import com.dev.usbdigitalcommunityplatform.ui.auth.SplashScreen

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
            ProfileSetupScreen() {
                // Home Screen Later
            }
        }
    }
}