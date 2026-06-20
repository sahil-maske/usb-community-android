package com.dev.usbdigitalcommunityplatform.ui.auth

import androidx.compose.foundation.clickable
import com.dev.usbdigitalcommunityplatform.ui.model.Language
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dev.usbdigitalcommunityplatform.ui.localization.TranslationManager
import com.dev.usbdigitalcommunityplatform.ui.theme.USBDigitalCommunityPlatformTheme

@Composable
fun LanguageSelectionScreen(
    onContinue: () -> Unit
){

    var selectedLanguage by remember { mutableStateOf(
        Language(
            name = "English",
            code = "en"
        )
    ) }

    val languages = listOf(
        Language("English", "en"),
        Language("हिंदी","hi"),
        Language("भोजपुरी","bj"),
        Language("मैथिली","mi"),
        Language("अवधी","av"),
        Language("मराठी","mr")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(22.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(80.dp))

        Text(
            text = TranslationManager.getText("select_language"),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text= TranslationManager.getText("Please select your preferred language to continue"),
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )
        Spacer(modifier = Modifier.height(30.dp))

        languages.forEach { language ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clickable {
                        selectedLanguage = language
                    },

                colors = CardDefaults.cardColors(
                    containerColor =
                        if (selectedLanguage == language)
                            MaterialTheme.colorScheme.primaryContainer
                        else
                            MaterialTheme.colorScheme.surface
                )
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),

                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = language.name,
                        fontSize = 18.sp
                    )

                    if (selectedLanguage == language) {

                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Selected"
                        )

                    }

                }

            }
        }

        Spacer(modifier = Modifier.weight(1f))

        androidx.compose.material3.Button(
            onClick = {
                TranslationManager.currentLanguage =
                    selectedLanguage.code

                onContinue()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4F6EF7)
            )
        ) {
            Text(
                text = TranslationManager.getText("continue"),
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun LanguageSelectionScreenPreview() {
    USBDigitalCommunityPlatformTheme {
        LanguageSelectionScreen(onContinue = {})
    }
}
