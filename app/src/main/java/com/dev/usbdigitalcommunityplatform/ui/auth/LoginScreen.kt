package com.dev.usbdigitalcommunityplatform.ui.auth

import androidx.compose.ui.text.TextStyle
import androidx.compose.foundation.background
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.border
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dev.usbdigitalcommunityplatform.ui.auth.AuthManager.sendOtp
import com.dev.usbdigitalcommunityplatform.ui.localization.TranslationManager
import com.dev.usbdigitalcommunityplatform.ui.theme.USBDigitalCommunityPlatformTheme
import com.dev.usbdigitalcommunityplatform.utils.findActivity

// iOS-style colors
private val iOSBlue = Color(0xFF007AFF)
private val iOSSystemGray6 = Color(0xFFF2F2F7)
private val iOSSystemGray = Color(0xFF8E8E93)
private val iOSLabel = Color(0xFF000000)
private val iOSSecondaryLabel = Color(0xFF6C6C70)
private val iOSSeparator = Color(0xFFC7C7CC)
private val iOSDestructive = Color(0xFFFF3B30)

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {

    var phoneNumber by remember { mutableStateOf("") }
    var phoneError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val haptic = LocalHapticFeedback.current
    val context = LocalContext.current
    val activity = context.findActivity()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp)
    ) {

        Spacer(modifier = Modifier.height(100.dp))

        // Title — iOS large title style
        Text(
            text = TranslationManager.getText("Welcome Back"),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = iOSLabel,
            letterSpacing = (-0.5).sp,
            lineHeight = 38.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Subtitle — iOS secondary label
        Text(
            text = TranslationManager.getText("Sign in with your mobile number to continue"),
            fontSize = 15.sp,
            fontWeight = FontWeight.Normal,
            color = iOSSecondaryLabel,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(36.dp))

        // Input label — iOS small caps style
        Text(
            text = TranslationManager.getText("MOBILE NUMBER"),
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = iOSSecondaryLabel,
            letterSpacing = 0.6.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Phone input — iOS grouped inset style
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .background(
                    color = iOSSystemGray6,
                    shape = RoundedCornerShape(14.dp)
                )
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "+91",
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                color = iOSLabel
            )

            Spacer(modifier = Modifier.width(12.dp))

            Box(
                modifier = Modifier
                    .width(1.5.dp)
                    .height(22.dp)
                    .background(iOSSeparator)
            )

            TextField(
                value = phoneNumber,
                onValueChange = {
                    if (it.length <= 10) phoneNumber = it
                    if (phoneError) phoneError = false
                },
                textStyle = TextStyle(
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Normal,
                    color = iOSLabel
                ),
                modifier = Modifier.weight(1f),
                placeholder = {
                    Text(
                        text = TranslationManager.getText("Enter 10-digit number"),
                        fontSize = 17.sp,
                        color = iOSSystemGray
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = iOSBlue
                )
            )
        }

        // Error message — iOS right-aligned small red
        if (phoneError) {
            Text(
                text = if (errorMessage.isNotEmpty()) errorMessage
                else TranslationManager.getText("Enter a valid mobile number"),
                color = iOSDestructive,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .padding(top = 6.dp, end = 4.dp)
                    .align(Alignment.End)
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        // CTA Button — iOS filled blue rounded rect
        Button(
            onClick = {
                if (phoneNumber.length == 10) {
                    phoneError = false
                    errorMessage = ""
                    isLoading = true
                    activity?.let {
                        sendOtp(
                            phoneNumber = phoneNumber,
                            activity = it,
                            onCodeSent = { isLoading = false; onLoginSuccess() },
                            onSuccess = { isLoading = false; onLoginSuccess() },
                            onFailed = { error ->
                                isLoading = false
                                errorMessage = error
                                phoneError = true
                            }
                        )
                    } ?: run {
                        isLoading = false
                        errorMessage = TranslationManager.getText("Activity not found")
                        phoneError = true
                    }
                } else {
                    phoneError = true
                    errorMessage = TranslationManager.getText("Enter a 10-digit number")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = iOSBlue,
                disabledContainerColor = iOSBlue.copy(alpha = 0.4f)
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 0.dp,
                pressedElevation = 0.dp
            )
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = TranslationManager.getText("Continue"),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = (-0.1).sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Footer note — iOS footnote style
        Text(
            text = "By continuing you agree to our Terms and Privacy Policy",
            fontSize = 13.sp,
            color = iOSSystemGray,
            lineHeight = 18.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun LoginScreenPreview() {
    USBDigitalCommunityPlatformTheme {
        LoginScreen(onLoginSuccess = {})
    }
}