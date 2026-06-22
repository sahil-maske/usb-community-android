package com.dev.usbdigitalcommunityplatform.ui.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dev.usbdigitalcommunityplatform.ui.localization.TranslationManager
import com.dev.usbdigitalcommunityplatform.ui.model.Language
import com.dev.usbdigitalcommunityplatform.ui.theme.USBDigitalCommunityPlatformTheme

private val iOSBlue = Color(0xFF007AFF)
private val iOSLabel = Color(0xFF000000)
private val iOSSecondaryLabel = Color(0xFF6C6C70)
private val iOSSystemGray6 = Color(0xFFF2F2F7)
private val iOSSystemBlueLight = Color(0xFFEAF2FF)
private val iOSSeparator = Color(0xFFC7C7CC)

@Composable
fun LanguageSelectionScreen(
    onContinue: () -> Unit
) {
    var selectedLanguage by remember {
        mutableStateOf(Language(name = "English", code = "en"))
    }

    val languages = listOf(
        Language("English", "en"),
        Language("हिंदी", "hi"),
        Language("भोजपुरी", "bj"),
        Language("मैथिली", "mi"),
        Language("अवधी", "av"),
        Language("मराठी", "mr")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(100.dp))

        Text(
            text = TranslationManager.getText("select_language"),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = iOSLabel,
            letterSpacing = (-0.5).sp,
            lineHeight = 38.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = TranslationManager.getText("Please select your preferred language to continue"),
            fontSize = 15.sp,
            fontWeight = FontWeight.Normal,
            color = iOSSecondaryLabel,
            lineHeight = 22.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )

        Spacer(modifier = Modifier.height(32.dp))

        languages.forEach { language ->
            val isSelected = selectedLanguage == language

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .clickable { selectedLanguage = language },
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) iOSSystemBlueLight else iOSSystemGray6
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = language.name,
                        fontSize = 17.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                        color = if (isSelected) iOSBlue else iOSLabel
                    )

                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Selected",
                            tint = iOSBlue,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                TranslationManager.currentLanguage = selectedLanguage.code
                onContinue()
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
                text = TranslationManager.getText("continue"),
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
fun LanguageSelectionScreenPreview() {
    USBDigitalCommunityPlatformTheme {
        LanguageSelectionScreen(onContinue = {})
    }
}