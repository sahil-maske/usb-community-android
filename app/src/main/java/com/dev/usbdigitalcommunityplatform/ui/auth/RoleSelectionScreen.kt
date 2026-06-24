package com.dev.usbdigitalcommunityplatform.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

// Colors are now defined in Colors.kt to avoid conflicting declarations


data class RoleItem(
    val title: String,
    val icon: String,
    val description: String
)

@Composable
fun RoleSelectionScreen(onRoleSelected: (String) -> Unit) {

    var selectedRole by remember { mutableStateOf("") }
    var isLoading    by remember { mutableStateOf(false) }

    val roles = listOf(
        RoleItem("Member",   "👤", "Basic community access"),
        RoleItem("Lawyer",   "⚖️", "Legal help & consultation"),
        RoleItem("Employee", "💼", "Job & work related services"),
        RoleItem("CA",       "📊", "Financial & tax services"),
        RoleItem("Vendor",   "🛒", "Sell products & services")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9F9F9)) // off-white — premium feel
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState())
    ) {

        Spacer(Modifier.height(80.dp))

        // step indicator — "Step 3 of 3"
        Text(
            text = "STEP 3 OF 3",
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = Blue,
            letterSpacing = 1.sp
        )

        Spacer(Modifier.height(10.dp))

        Text(
            text = "Select Your Role",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Black,
            letterSpacing = (-0.5).sp
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Choose how you want to use USB Community Platform",
            fontSize = 15.sp,
            color = Gray,
            lineHeight = 22.sp
        )

        Spacer(Modifier.height(32.dp))

        roles.forEach { role ->
            RoleCard(
                role = role,
                isSelected = selectedRole == role.title,
                onClick = { selectedRole = role.title }
            )
            Spacer(Modifier.height(10.dp))
        }

        Spacer(Modifier.height(32.dp))

        Button(
            onClick = {
                isLoading = true
                val currentUser = FirebaseAuth.getInstance().currentUser

                if (currentUser != null) {
                    // pehle Firestore se user ka existing role dekho
                    FirebaseFirestore.getInstance()
                        .collection("users")
                        .document(currentUser.uid)
                        .get()
                        .addOnSuccessListener { document ->
                            val existingRole = document.getString("role")

                            if (existingRole == "Admin") {
                                // agar Firestore mein pehle se Admin hai
                                // toh directly admin page pe bhejo
                                isLoading = false
                                onRoleSelected("Admin")
                            } else {
                                // normal user — jo select kiya woh save karo
                                FirebaseFirestore.getInstance()
                                    .collection("users")
                                    .document(currentUser.uid)
                                    .update("role", selectedRole)
                                    .addOnSuccessListener {
                                        isLoading = false
                                        onRoleSelected(selectedRole)
                                    }
                                    .addOnFailureListener {
                                        isLoading = false
                                    }
                            }
                        }
                }
            },
            enabled = selectedRole.isNotEmpty() && !isLoading,
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
                Text(
                    text = "Continue as $selectedRole".takeIf { selectedRole.isNotEmpty() } ?: "Continue",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }

        Spacer(Modifier.height(32.dp))
    }
}

@Composable
fun RoleCard(role: RoleItem, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            // shadow sirf selected card pe — utha hua feel
            .shadow(
                elevation = if (isSelected) 6.dp else 0.dp,
                shape = RoundedCornerShape(
                    topStart = 6.dp,
                    topEnd = 20.dp,
                    bottomStart = 20.dp,
                    bottomEnd = 20.dp
                ),
                ambientColor = Blue.copy(alpha = 0.15f),
                spotColor = Blue.copy(alpha = 0.15f)
            )
            .background(
                color = if (isSelected) BlueBg else Color.White,
                shape = RoundedCornerShape(
                    topStart = 6.dp,   // SIM card cut
                    topEnd = 20.dp,
                    bottomStart = 20.dp,
                    bottomEnd = 20.dp
                )
            )
            .border(
                width = if (isSelected) 1.5.dp else 1.dp,
                color = if (isSelected) Blue else Color(0xFFE5E5EA),
                shape = RoundedCornerShape(
                    topStart = 6.dp,
                    topEnd = 20.dp,
                    bottomStart = 20.dp,
                    bottomEnd = 20.dp
                )
            )
            .clickable { onClick() }
            .padding(horizontal = 18.dp, vertical = 14.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {

                // icon box
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .background(
                            color = if (isSelected) Blue.copy(alpha = 0.12f) else InputBg,
                            shape = RoundedCornerShape(13.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(role.icon, fontSize = 22.sp)
                }

                Spacer(Modifier.width(14.dp))

                Column {
                    Text(
                        text = role.title,
                        fontSize = 17.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                        color = if (isSelected) Blue else Black
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = role.description,
                        fontSize = 13.sp,
                        color = LightGray
                    )
                }
            }

            // iOS radio button
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .background(
                        color = if (isSelected) Blue else Color.Transparent,
                        shape = RoundedCornerShape(11.dp)
                    )
                    .border(
                        width = 1.5.dp,
                        color = if (isSelected) Blue else Color(0xFFC7C7CC),
                        shape = RoundedCornerShape(11.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(Color.White, RoundedCornerShape(4.dp))
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF9F9F9)
@Composable
fun RoleSelectionScreenPreview() {
    RoleSelectionScreen(onRoleSelected = {})
}