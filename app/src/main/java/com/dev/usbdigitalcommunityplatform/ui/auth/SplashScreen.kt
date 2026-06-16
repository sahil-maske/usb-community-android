package com.dev.usbdigitalcommunityplatform.ui.auth

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
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onTimeout: () -> Unit = {}) {
    var startAnimation by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(3000) // 3 seconds delay for professional feel (2s requested + animation time)
        onTimeout()
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
