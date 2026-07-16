package com.dev.usbdigitalcommunityplatform.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.BatteryAlert
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.PowerSettingsNew
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val iOSBlue = Color(0xFF007AFF)
private val iOSLabel = Color(0xFF000000)
private val iOSSecondaryLabel = Color(0xFF6C6C70)
private val iOSSystemGray6 = Color(0xFFF2F2F7)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BatteryOptimizationGuideScreen(onBack: () -> Unit) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("App Persistence Guide", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.BatteryAlert,
                contentDescription = null,
                tint = iOSBlue,
                modifier = Modifier
                    .size(64.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(Modifier.height(24.dp))

            Text(
                text = "Keep Your Session Active",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = iOSLabel,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = "To ensure you don't get logged out automatically, please allow USB Community to run in the background. OEM devices like Vivo require manual whitelisting.",
                fontSize = 15.sp,
                color = iOSSecondaryLabel,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )

            Spacer(Modifier.height(32.dp))

            // Step 1: Standard Android Exemption
            GuideStep(
                number = 1,
                title = "Battery Optimization",
                description = "Grant exemption to prevent the system from putting the app to sleep.",
                icon = Icons.Outlined.PowerSettingsNew,
                buttonText = "Request Exemption",
                onClick = {  }
            )



            Spacer(Modifier.height(40.dp))
            
            Card(
                colors = CardDefaults.cardColors(containerColor = iOSSystemGray6),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Outlined.Info, contentDescription = null, tint = iOSSecondaryLabel)
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = "Tip: Also 'Lock' the app in the Recent Apps screen by swiping down on the app card.",
                        fontSize = 13.sp,
                        color = iOSSecondaryLabel,
                        lineHeight = 18.sp
                    )
                }
            }
            
            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
fun GuideStep(
    number: Int,
    title: String,
    description: String,
    icon: ImageVector,
    buttonText: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(iOSSystemGray6, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(iOSBlue, RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(number.toString(), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
            Spacer(Modifier.width(12.dp))
            Text(title, fontWeight = FontWeight.SemiBold, fontSize = 17.sp, color = iOSLabel)
        }
        
        Spacer(Modifier.height(8.dp))
        
        Text(description, fontSize = 14.sp, color = iOSSecondaryLabel, lineHeight = 20.sp)
        
        Spacer(Modifier.height(16.dp))
        
        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = iOSBlue),
            shape = RoundedCornerShape(10.dp)
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text(buttonText)
        }
    }
}
