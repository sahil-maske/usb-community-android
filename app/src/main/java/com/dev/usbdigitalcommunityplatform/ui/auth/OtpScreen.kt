package com.dev.usbdigitalcommunityplatform.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dev.usbdigitalcommunityplatform.ui.auth.AuthManager.verificationId
import com.dev.usbdigitalcommunityplatform.ui.localization.TranslationManager
import com.dev.usbdigitalcommunityplatform.ui.theme.USBDigitalCommunityPlatformTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.delay

private val iOSBlue = Color(0xFF007AFF)
private val iOSLabel = Color(0xFF000000)
private val iOSSecondaryLabel = Color(0xFF6C6C70)
private val iOSSystemGray5 = Color(0xFFE5E5EA)
private val iOSSystemGray6 = Color(0xFFF2F2F7)
private val iOSDestructive = Color(0xFFFF3B30)

@Composable
fun OtpScreen(onVerify: () -> Unit) {

    var otp by remember { mutableStateOf("") }
    var otpError by remember { mutableStateOf(false) }

    val focusRequester = remember { FocusRequester() }
    val haptic = LocalHapticFeedback.current
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        delay(300)
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp)
    ) {

        Spacer(modifier = Modifier.height(100.dp))

        Text(
            text = TranslationManager.getText("Verify Phone Number"),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = iOSLabel,
            letterSpacing = (-0.5).sp,
            lineHeight = 38.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = TranslationManager.getText("Enter the 6-digit code sent to your mobile number"),
            fontSize = 15.sp,
            fontWeight = FontWeight.Normal,
            color = iOSSecondaryLabel,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Hidden input field
        BasicTextField(
            value = otp,
            onValueChange = { value ->
                otp = value.filter { it.isDigit() }.take(6)
                otpError = false
            },
            modifier = Modifier
                .size(1.dp)
                .alpha(0f)
                .focusRequester(focusRequester),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        // OTP boxes
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    focusRequester.requestFocus()
                    keyboardController?.show()
                },
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            repeat(6) { index ->
                val isFocused = index == otp.length && otp.length < 6
                val isFilled = index < otp.length

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(58.dp)
                        .background(
                            color = if (isFilled) iOSSystemGray6 else Color.White,
                            shape = RoundedCornerShape(14.dp)
                        )
                        .border(
                            width = if (isFocused) 2.dp else 1.5.dp,
                            color = when {
                                otpError -> iOSDestructive
                                isFocused -> iOSBlue
                                isFilled -> iOSSystemGray5
                                else -> iOSSystemGray5
                            },
                            shape = RoundedCornerShape(14.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = otp.getOrNull(index)?.toString() ?: "",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = iOSLabel
                    )
                }
            }
        }

        if (otpError) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = TranslationManager.getText("Invalid verification code"),
                color = iOSDestructive,
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Resend row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Didn't receive a code? ",
                fontSize = 15.sp,
                color = iOSSecondaryLabel
            )
            Text(
                text = "Resend",
                fontSize = 15.sp,
                color = iOSBlue,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            enabled = otp.length == 6,
            onClick = {
                if (otp.length == 6) {
                    val credential = PhoneAuthProvider.getCredential(verificationId, otp)
                    FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                otpError = false
                                onVerify()
                            } else {
                                otpError = true
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            }
                        }
                } else {
                    otpError = true
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
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
            Text(
                text = TranslationManager.getText("Verify"),
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = (-0.1).sp
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun OtpScreenPreview() {
    USBDigitalCommunityPlatformTheme {
        OtpScreen(onVerify = {})
    }
}