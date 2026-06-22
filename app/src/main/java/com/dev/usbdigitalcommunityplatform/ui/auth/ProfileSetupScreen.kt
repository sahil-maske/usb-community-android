package com.dev.usbdigitalcommunityplatform.ui.auth

import androidx.compose.material3.ExperimentalMaterial3Api
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dev.usbdigitalcommunityplatform.ui.localization.TranslationManager
import com.dev.usbdigitalcommunityplatform.ui.model.UserProfile

val Black   = Color(0xFF000000)
val Gray    = Color(0xFF6C6C70)
val Blue    = Color(0xFF007AFF)
val InputBg = Color(0xFFF2F2F7)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSetupScreen(onComplete: () -> Unit) {

    var fullName   by remember { mutableStateOf("") }
    var selectedState by remember { mutableStateOf("") }
    var occupation by remember { mutableStateOf("") }
    var expanded   by remember { mutableStateOf(false) }
    var isLoading  by remember { mutableStateOf(false) }

    val states = listOf(
        "Maharashtra",
        "Bihar",
        "Uttar Pradesh",
        "Jharkhand",
        "Madhya Pradesh"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {

        Spacer(Modifier.height(80.dp))

        Text("Set Up Profile", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Black)
        Spacer(Modifier.height(8.dp))
        Text("Tell us a little about yourself", fontSize = 15.sp, color = Gray)

        Spacer(Modifier.height(36.dp))

        // full name
        Text("FULL NAME", fontSize = 12.sp, color = Gray)
        Spacer(Modifier.height(8.dp))
        InputField(value = fullName, onChange = { fullName = it }, hint = "Enter your full name")

        Spacer(Modifier.height(16.dp))

        // state dropdown — iOS style
        Text("STATE", fontSize = 12.sp, color = Gray)
        Spacer(Modifier.height(8.dp))

        // ExposedDropdownMenuBox = Compose ka built-in dropdown — clickable box + menu
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            Text("STATE", fontSize = 12.sp, color = Gray)
            Spacer(Modifier.height(8.dp))
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                // iOS jesa gray box — same as InputField
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .background(InputBg, RoundedCornerShape(14.dp))
                        .padding(horizontal = 16.dp)
                        .menuAnchor(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (selectedState.isEmpty()) "Select your state" else selectedState,
                        fontSize = 17.sp,
                        color = if (selectedState.isEmpty()) Gray else Black
                    )

                    // arrow — upar jab open, neeche jab band
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = Gray
                    )
                }

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(Color.White)
                ) {
                    states.forEach { stateName ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = stateName,
                                    fontSize = 17.sp,
                                    // selected wala blue ho jata hai — iOS style
                                    color = if (stateName == selectedState) Blue else Black,
                                    fontWeight = if (stateName == selectedState) FontWeight.SemiBold else FontWeight.Normal
                                )
                            },
                            onClick = {
                                selectedState = stateName
                                expanded = false
                            }
                        )
                    }
                }
            }

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(Color.White)
            ) {
                states.forEach { stateName ->
                    DropdownMenuItem(
                        text = { Text(stateName, fontSize = 17.sp, color = Black) },
                        onClick = {
                            selectedState = stateName
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // occupation
        Text("OCCUPATION", fontSize = 12.sp, color = Gray)
        Spacer(Modifier.height(8.dp))
        InputField(value = occupation, onChange = { occupation = it }, hint = "What do you do?")

        Spacer(Modifier.height(48.dp))

        Button(
            onClick = {
                isLoading = true
                val auth = FirebaseAuth.getInstance()
                val firestore = FirebaseFirestore.getInstance()
                val currentUser = auth.currentUser

                if (currentUser != null) {
                    val userProfile = UserProfile(
                        uid = currentUser.uid,
                        phoneNumber = currentUser.phoneNumber ?: "",
                        name = fullName,
                        state = selectedState,
                        occupation = occupation,
                        role = "",
                        language = TranslationManager.currentLanguage
                    )
                    firestore.collection("users")
                        .document(currentUser.uid)
                        .set(userProfile)
                        .addOnSuccessListener {
                            isLoading = false
                            onComplete()
                        }
                        .addOnFailureListener {
                            isLoading = false
                        }
                }
            },
            enabled = fullName.isNotBlank() && selectedState.isNotBlank() && !isLoading,
            modifier = Modifier.fillMaxWidth().height(54.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Blue,
                disabledContainerColor = Blue.copy(alpha = 0.4f)
            ),
            elevation = ButtonDefaults.buttonElevation(0.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(22.dp)
                )
            } else {
                Text("Continue", fontSize = 17.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
            }
        }

        Spacer(Modifier.height(32.dp))
    }
}

@Composable
fun InputField(value: String, onChange: (String) -> Unit, hint: String) {
    TextField(
        value = value,
        onValueChange = onChange,
        placeholder = { Text(hint, color = Gray) },
        singleLine = true,
        textStyle = TextStyle(fontSize = 17.sp, color = Black),
        modifier = Modifier.fillMaxWidth().height(52.dp),
        shape = RoundedCornerShape(14.dp),
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
        colors = TextFieldDefaults.colors(
            focusedContainerColor   = InputBg,
            unfocusedContainerColor = InputBg,
            focusedIndicatorColor   = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor             = Blue
        )
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun ProfileSetupScreenPreview() {
    ProfileSetupScreen(onComplete = {})
}