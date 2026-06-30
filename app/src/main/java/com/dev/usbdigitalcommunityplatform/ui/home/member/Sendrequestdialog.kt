package com.dev.usbdigitalcommunityplatform.ui.home.member

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dev.usbdigitalcommunityplatform.ui.theme.USBDigitalCommunityPlatformTheme

/**
 * Bottom sheet jo Send Request button dabane pe khulta hai.
 * User yahan apna message likhta hai, phir Confirm karta hai.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendRequestDialog(
    targetUser: ServiceUser,
    roleColor: Color,
    onDismiss: () -> Unit,
    onConfirm: (message: String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState        = sheetState,
        containerColor    = Color.White
    ) {
        SendRequestDialogContent(
            targetUser = targetUser,
            roleColor  = roleColor,
            onConfirm  = onConfirm
        )
    }
}

@Composable
fun SendRequestDialogContent(
    targetUser: ServiceUser,
    roleColor: Color,
    onConfirm: (message: String) -> Unit
) {
    var message by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 32.dp)
    ) {
        Text(
            text       = "${targetUser.name} ko request bhejo",
            fontSize   = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color      = Color(0xFF000000)
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text     = "Apna kaam ya zaroorat short mein likho",
            fontSize = 13.sp,
            color    = Color(0xFF8E8E93)
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value         = message,
            onValueChange = { if (it.length <= 300) message = it },
            placeholder   = { Text("Jaise: Mujhe packaging ka kaam chahiye, kal se start kar sakta hoon") },
            modifier      = Modifier
                .fillMaxWidth()
                .height(120.dp),
            shape         = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor   = roleColor,
                unfocusedBorderColor = Color(0xFFE0E0E0)
            )
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text     = "${message.length}/300",
            fontSize = 11.sp,
            color    = Color(0xFF8E8E93),
            modifier = Modifier.fillMaxWidth(),
            textAlign = androidx.compose.ui.text.style.TextAlign.End
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick  = { onConfirm(message.trim()) },
            enabled  = message.trim().isNotEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape    = RoundedCornerShape(12.dp),
            colors   = ButtonDefaults.buttonColors(
                containerColor         = roleColor,
                disabledContainerColor = roleColor.copy(alpha = 0.4f)
            )
        ) {
            Text(
                text       = "Request Bhejo",
                color      = Color.White,
                fontSize   = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SendRequestDialogPreview() {
    val sampleUser = ServiceUser(
        uid = "1",
        name = "Sahil Maske",
        role = "Developer",
        city = "Pune",
        rating = 4.5f,
        reviewCount = 10,
        description = "I am a mobile app developer."
    )
    USBDigitalCommunityPlatformTheme {
        Surface(color = Color.White) {
            SendRequestDialogContent(
                targetUser = sampleUser,
                roleColor  = Color(0xFF6200EE),
                onConfirm  = {}
            )
        }
    }
}
