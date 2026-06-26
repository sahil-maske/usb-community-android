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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// card colors
private val CardDark    = Color(0xFF0A1628)
private val CardMid     = Color(0xFF1A3A6B)
private val CardLight   = Color(0xFF2563B0)
private val CardAccent  = Color(0xFF4A90D9)
private val Green       = Color(0xFF34C759)
private val GoldLight   = Color(0xFFFFD700)
private val White70     = Color.White.copy(alpha = 0.7f)
private val White40     = Color.White.copy(alpha = 0.4f)
private val White15     = Color.White.copy(alpha = 0.15f)

@Composable
fun DigitalIDCard(
    name: String,
    role: String,
    userId: String
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
            if (rotation <= 90f) {
                FrontSide(name, role, userId)
            } else {
                BackSide(name, role, userId)
            }
        }

        Spacer(Modifier.height(12.dp))

        // tap hint
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
                brush = Brush.linearGradient(
                    colors = listOf(CardDark, CardMid, CardLight)
                ),
                shape = RoundedCornerShape(24.dp)
            )
    ) {
        // shimmer line top — premium feel
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color.Transparent,
                            White40,
                            Color.Transparent
                        )
                    ),
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                )
        )

        // circle glow — top right decoration
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

            // top row — logo + NFC
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // shield icon box
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .background(White15, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🛡", fontSize = 14.sp)
                    }
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "USB Digital ID",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
                // NFC waves
                Text(")))", fontSize = 16.sp, color = White70, letterSpacing = (-2).sp)
            }

            Spacer(Modifier.height(20.dp))

            // user ID — center, big, gold tint
            Text(
                text = userId,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                letterSpacing = 2.sp
            )

            Spacer(Modifier.height(12.dp))

            // verified badge
            Text(
                text = "✓ Verified",
                fontSize = 12.sp,
                color = Green,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.weight(1f))

            // bottom divider line
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(0.5.dp)
                    .background(White15)
            )

            Spacer(Modifier.height(10.dp))

            // name + role bottom
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(name, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                    Text(role, fontSize = 12.sp, color = White70)
                }
                // gold star — premium member indicator
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
fun BackSide(name: String, role: String, userId: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer { rotationY = 180f }
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(CardDark, CardMid, CardLight)
                ),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(22.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // QR white box
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(Color.White, RoundedCornerShape(18.dp))
                    .border(1.dp, White15, RoundedCornerShape(18.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("▦", fontSize = 36.sp, color = CardDark)
                    Spacer(Modifier.height(4.dp))
                    Text("Scan", fontSize = 10.sp, color = CardMid)
                }
            }

            Spacer(Modifier.width(18.dp))

            // right side info
            Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🛡", fontSize = 12.sp)
                    Spacer(Modifier.width(4.dp))
                    Text("USB Digital ID", fontSize = 11.sp, color = White70)
                }

                Spacer(Modifier.height(10.dp))

                Text(name, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Spacer(Modifier.height(3.dp))
                Text(userId, fontSize = 13.sp, color = White70, letterSpacing = 1.sp)
                Spacer(Modifier.height(3.dp))
                Text(role, fontSize = 12.sp, color = White40)

                Spacer(Modifier.height(12.dp))

                // verified badge
                Box(
                    modifier = Modifier
                        .background(Green, RoundedCornerShape(20.dp))
                        .padding(horizontal = 12.dp, vertical = 5.dp)
                ) {
                    Text("✓  Verified", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
                }

                Spacer(Modifier.height(8.dp))

                Text(
                    "Scan to Verify",
                    fontSize = 11.sp,
                    color = White40,
                    textAlign = TextAlign.Start
                )
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
            userId = "USB000154"
        )
    }
}