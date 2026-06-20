package com.dev.usbdigitalcommunityplatform.ui.auth

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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

@Composable
fun OtpScreen(onVerify: () -> Unit) {
    var otp by remember {
        mutableStateOf("")
    }

    var otpError by remember {
        mutableStateOf(false)
    }
    val focusRequester = remember { FocusRequester() }

    val haptic = LocalHapticFeedback.current

    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {

        delay(300)

        focusRequester.requestFocus()

        keyboardController?.show()
    }


    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Spacer(
                modifier = Modifier.height(120.dp)
            )

            Text(
                text = TranslationManager.getText("Verify Phone Number"),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = TranslationManager.getText("Enter the 6-digit code sent to your mobile number"),
                fontSize = 16.sp
            )
            Spacer(
                modifier = Modifier.height(48.dp)
            )
            BasicTextField(
                value = otp,
                onValueChange = { value ->

                    otp = value
                        .filter { it.isDigit() }
                        .take(6)

                    otpError = false
                },

                modifier = Modifier
                    .size(1.dp)
                    .alpha(0f)
                    .focusRequester(focusRequester),

                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )

            Row(
                modifier = Modifier.fillMaxWidth()
                    .clickable {

                        focusRequester.requestFocus()

                        keyboardController?.show()
                    },
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                repeat(6) { index ->
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .border(
                                width = 1.5.dp,
                                color = when {

                                    otpError ->
                                        Color.Red

                                    index == otp.length &&
                                            otp.length < 6 ->
                                        Color(0xFF4F6EF7)

                                    else ->
                                        Color.LightGray
                                },

                                shape = RoundedCornerShape(14.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = otp.getOrNull(index)?.toString() ?: "",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            if (otpError) {
                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Text(
                    text = TranslationManager.getText("⚠ Invalid verification code"),
                    color = Color.Red,
                    fontSize = 14.sp
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
                                    haptic.performHapticFeedback(
                                        HapticFeedbackType.LongPress
                                    )
                                }
                            }
                    } else {

                        otpError = true

                        haptic.performHapticFeedback(
                            HapticFeedbackType.LongPress
                        )
                    }
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),

                shape = RoundedCornerShape(18.dp),

                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4F6EF7)
                )
            ) {

                Text(
                    text = TranslationManager.getText("Verify"),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }


    }
}

@Preview(showBackground = true)
@Composable
fun OtpScreenPreview() {
    USBDigitalCommunityPlatformTheme {
        OtpScreen(onVerify = {})
    }
}
