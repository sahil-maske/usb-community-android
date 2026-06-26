package com.dev.usbdigitalcommunityplatform.ui.home.member

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberHomeScreen(
    viewModel: ProfileViewModel = viewModel()
) {

    Scaffold(

        topBar = {

            TopAppBar(

                title = {

                    Text(
                        text = "USB Community",
                        fontWeight = FontWeight.Bold
                    )

                },

                actions = {

                    IconButton(
                        onClick = { }
                    ) {

                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = "Notifications"
                        )

                    }

                }

            )

        }

    ) { padding ->

        Column(

            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp)

        ) {

            Text(
                text = "Good Morning 👋",
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = viewModel.userProfile.name.ifBlank { "Loading..." },
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

        }

    }

}
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun MemberHomeScreenPreview() {
    MemberHomeScreen()
}
