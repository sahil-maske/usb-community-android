package com.dev.usbdigitalcommunityplatform.ui.auth

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.dev.usbdigitalcommunityplatform.ui.theme.USBDigitalCommunityPlatformTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeoutOrNull

@Composable
fun SplashScreen(onNavigate: (String) -> Unit) {
    var startAnimation by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        startAnimation = true
        
        // Wait for minimum splash duration
        delay(2000)

        // Robust Auth Check: Use AuthStateListener to wait for Firebase to initialize
        val auth = FirebaseAuth.getInstance()
        var currentUser = auth.currentUser

        if (currentUser == null) {
            // If null, it might still be loading from disk cache. 
            // We wait for the first definitive state or timeout.
            withTimeoutOrNull(3000) { // 3 second timeout for auth initialization
                kotlinx.coroutines.suspendCancellableCoroutine { continuation ->
                    val listener = object : FirebaseAuth.AuthStateListener {
                        override fun onAuthStateChanged(firebaseAuth: FirebaseAuth) {
                            val user = firebaseAuth.currentUser
                            if (user != null) {
                                auth.removeAuthStateListener(this)
                                if (continuation.isActive) {
                                    continuation.resume(user, onCancellation = null)
                                }
                            }
                        }
                    }
                    auth.addAuthStateListener(listener)
                    continuation.invokeOnCancellation { auth.removeAuthStateListener(listener) }
                }
            }
            // Re-check after waiting/timeout
            currentUser = auth.currentUser
        }

        Log.d("AUTH_DEBUG", "Final currentUser UID = ${currentUser?.uid}")

        if (currentUser != null) {
            try {
                val document = FirebaseFirestore.getInstance().collection("users")
                    .document(currentUser.uid)
                    .get()
                    .await()

                if (document.exists()) {
                    val role = document.getString("role")?.lowercase() ?: ""
                    Log.d("Splash", "Role = $role")

                    val route = when (role) {
                        "admin" -> "admin_home"
                        "vendor" -> "vendor_home"
                        "member", "employer", "lawyer", "ca" -> "member_home"
                        else -> "role_selection"
                    }
                    onNavigate(route)
                } else {
                    Log.d("Splash", "User profile not found")
                    onNavigate("profile_setup")
                }
            } catch (e: Exception) {
                Log.e("Splash", "Error fetching user profile", e)
                onNavigate("member_home") // Safe fallback if already authenticated
            }
        } else {
            onNavigate("language")
        }
    }

    SplashContent(startAnimation)
}

@Composable
fun SplashContent(startAnimation: Boolean) {
    // Professional Blue & White palette
    val backgroundColor = Color.White
    val primaryBlue = Color(0xFF1A73E8) // Modern professional blue
    val secondaryText = Color(0xFF5F6368)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(24.dp)
        ) {
            // Lottie Animation Placeholder
            // Note: Add your Lottie JSON file to res/raw/community_animation.json
            val composition by rememberLottieComposition(
                // Use LottieCompositionSpec.RawRes(R.raw.community_animation) once file is added
                LottieCompositionSpec.Url("https://assets9.lottiefiles.com/packages/lf20_jjmcy8mj.json") // Community example URL
            )

            AnimatedVisibility(
                visible = startAnimation,
                enter = fadeIn(animationSpec = tween(durationMillis = 1000))
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    LottieAnimation(
                        composition = composition,
                        iterations = LottieConstants.IterateForever,
                        modifier = Modifier.size(280.dp)
                    )

                    if (FirebaseAuth.getInstance().currentUser != null) {
                        Log.d("Splash", "User Logged In")
                    } else {
                        Log.d("Splash", "User Not Logged In")
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "USB Digital Community Platform",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = primaryBlue,
                        textAlign = TextAlign.Center,
                        lineHeight = 34.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Connecting Communities, Opportunities & Services",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = secondaryText,
                        textAlign = TextAlign.Center,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }
        
        // Bottom Tagline
        AnimatedVisibility(
            visible = startAnimation,
            enter = fadeIn(animationSpec = tween(durationMillis = 1500)),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Text(
                text = "Powered by USB Digital",
                modifier = Modifier.padding(bottom = 32.dp),
                fontSize = 12.sp,
                color = secondaryText.copy(alpha = 0.7f),
                fontWeight = FontWeight.Light
            )
        }
    }
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun SplashScreenPreview() {
    USBDigitalCommunityPlatformTheme {
        SplashContent(startAnimation = true)
    }
}

@Preview(showBackground = true, device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
fun SplashScreenTabletPreview() {
    USBDigitalCommunityPlatformTheme {
        SplashContent(startAnimation = true)
    }
}
