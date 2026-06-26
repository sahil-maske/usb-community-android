package com.dev.usbdigitalcommunityplatform.ui.cards

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val CardDark   = Color(0xFF0A1628)
private val CardMid    = Color(0xFF1A3A6B)
private val CardLight  = Color(0xFF2563B0)
private val CardAccent = Color(0xFF4A90D9)
private val Green      = Color(0xFF34C759)
private val GoldLight  = Color(0xFFFFD700)
private val White70    = Color.White.copy(alpha = 0.7f)
private val White40    = Color.White.copy(alpha = 0.4f)
private val White15    = Color.White.copy(alpha = 0.15f)

@Composable
fun DigitalIDCard(
    name: String,
    role: String,
    userId: String,
    phone: String,      // back side pe dikhega
    state: String,      // back side pe dikhega
    occupation: String  // back side pe dikhega
) {
    var isFlipped by remember { mutableStateOf(false) }

    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(700)
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .graphicsLayer {
                    rotationY = rotation
                    cameraDistance = 14f * density
                }
                .clickable { isFlipped = !isFlipped }
        ) {
            if (!isFlipped) {
                FrontSide(name, role, userId)
            } else {
                BackSide(name, role, userId, phone, state, occupation)
            }
        }

        Spacer(Modifier.height(12.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text("↺", fontSize = 14.sp, color = Color(0xFF8E8E93))
            Spacer(Modifier.width(4.dp))
            Text("Tap card to flip", fontSize = 12.sp, color = Color(0xFF8E8E93))
        }
    }
}

@Composable
fun FrontSide(name: String, role: String, userId: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(colors = listOf(CardDark, CardMid, CardLight)),
                shape = RoundedCornerShape(24.dp)
            )
    ) {
        // shimmer line top
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color.Transparent, White40, Color.Transparent)
                    ),
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                )
        )

        // circle glow top right
        Box(
            modifier = Modifier
                .size(160.dp)
                .align(Alignment.TopEnd)
                .offset(x = 40.dp, y = (-40).dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(CardAccent.copy(alpha = 0.25f), Color.Transparent)
                    ),
                    shape = CircleShape
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(22.dp)
        ) {
            // top row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .background(White15, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🛡", fontSize = 14.sp)
                    }
                    Spacer(Modifier.width(8.dp))
                    Text("USB Digital ID", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                }
                Text(")))", fontSize = 16.sp, color = White70, letterSpacing = (-2).sp)
            }

            Spacer(Modifier.height(20.dp))

            Text(userId, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White, letterSpacing = 2.sp)

            Spacer(Modifier.height(12.dp))

            Text("✓ Verified", fontSize = 12.sp, color = Green, fontWeight = FontWeight.SemiBold)

            Spacer(Modifier.weight(1f))

            Box(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(White15))

            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(name, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                    Text(role, fontSize = 12.sp, color = White70)
                }
                Box(
                    modifier = Modifier
                        .background(White15, RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text("★  Premium", fontSize = 11.sp, color = GoldLight, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
fun BackSide(
    name: String,
    role: String,
    userId: String,
    phone: String,
    state: String,
    occupation: String
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer { rotationY = 180f }
            .background(
                brush = Brush.linearGradient(colors = listOf(CardDark, CardMid, CardLight)),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(22.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // QR box
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .background(Color.White, RoundedCornerShape(18.dp))
                    .border(1.dp, White15, RoundedCornerShape(18.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .background(Color.White.copy(alpha = 0.15f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        // initials from name
                        val initials = name
                            .split(" ")
                            .filter { it.isNotBlank() }
                            .mapNotNull { it.firstOrNull()?.uppercaseChar() }
                            .take(2)
                            .joinToString("")
                            .ifEmpty { "?" }

                        Text(
                            text = initials,
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Spacer(Modifier.height(4.dp))
                    Text("Scan", fontSize = 10.sp, color = CardMid)
                }
            }

            Spacer(Modifier.width(16.dp))

            // user private info
            Column(verticalArrangement = Arrangement.Center) {
                Text(name, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Spacer(Modifier.height(2.dp))
                Text(userId, fontSize = 11.sp, color = White70, letterSpacing = 1.sp)

                Spacer(Modifier.height(8.dp))

                // phone
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("📞", fontSize = 11.sp)
                    Spacer(Modifier.width(4.dp))
                    Text(phone, fontSize = 12.sp, color = White70)
                }

                Spacer(Modifier.height(4.dp))

                // state
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("📍", fontSize = 11.sp)
                    Spacer(Modifier.width(4.dp))
                    Text(state, fontSize = 12.sp, color = White70)
                }

                Spacer(Modifier.height(4.dp))

                // occupation
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("💼", fontSize = 11.sp)
                    Spacer(Modifier.width(4.dp))
                    Text(occupation, fontSize = 12.sp, color = White70)
                }

                Spacer(Modifier.height(10.dp))


            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF2F2F7)
@Composable
fun DigitalIDCardPreview() {
    Column(modifier = Modifier.padding(24.dp)) {
        DigitalIDCard(
            name = "Sahil Maske",
            role = "Member",
            userId = "USB000154",
            phone = "+91 9876543210",
            state = "Maharashtra",
            occupation = "Software Engineer"
        )
    }
}