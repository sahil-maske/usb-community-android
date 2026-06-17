package com.dev.usbdigitalcommunityplatform.ui.auth

import androidx.compose.ui.text.TextStyle
import androidx.compose.foundation.background
import android.app.Activity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.border
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dev.usbdigitalcommunityplatform.ui.auth.AuthManager.sendOtp
import com.dev.usbdigitalcommunityplatform.ui.theme.USBDigitalCommunityPlatformTheme

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {

    var phoneNumber by remember {
        mutableStateOf("")
    }

    var phoneError by remember {
        mutableStateOf(false)
    }
    val haptic = LocalHapticFeedback.current

    val context = LocalContext.current
    val activity = context as Activity

    var errorMessage by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(22.dp)

    ) {
        Spacer(
            modifier = Modifier.height(120.dp)
        )

        Text(
            text = "Welcome Back",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(
            modifier = Modifier.height(8.dp)
        )
        Text(
            text = "Sign in with your mobile number to continue",
            fontSize = 16.sp
        )
        Spacer(
            modifier = Modifier.height(40.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .border(
                    width = 1.dp,
                    color = Color.LightGray,
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(horizontal = 16.dp),

            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "+91",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(
                modifier = Modifier.width(12.dp)
            )

            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(24.dp)
                    .background(Color.LightGray)
            )

            Spacer(
                modifier = Modifier.width(12.dp)
            )

            TextField(
                value = phoneNumber,
                onValueChange = {
                    if (it.length <= 10) {
                        phoneNumber = it
                    }
                    if (phoneError) phoneError = false
                },
                textStyle = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                ),

                modifier = Modifier.weight(1f),

                placeholder = {
                    Text("Mobile Number")
                },

                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),

                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
        }

        if (phoneError) {
            Text(
                text = if (errorMessage.isNotEmpty()) errorMessage else "Enter a valid mobile number",
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp, end = 4.dp).align(Alignment.End)
            )
        }

        Spacer(
            modifier = Modifier.height(32.dp)
        )
        Button(
            onClick = {
                if (phoneNumber.length == 10) {
                    phoneError = false
                    errorMessage = ""
                    sendOtp(
                        phoneNumber = phoneNumber,
                        activity = activity,
                        onCodeSent = {
                            onLoginSuccess()
                        },
                        onSuccess = {
                            onLoginSuccess()
                        },
                        onFailed = { error ->
                            errorMessage = error
                            phoneError = true
                        }
                    )
                } else {
                    phoneError = true
                    errorMessage = "Enter a 10-digit number"
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
                text = "Continue",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    USBDigitalCommunityPlatformTheme {
        LoginScreen(onLoginSuccess = {})
    }
}
