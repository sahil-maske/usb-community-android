package com.dev.usbdigitalcommunityplatform

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dev.usbdigitalcommunityplatform.navigation.AppNavGraph
import com.dev.usbdigitalcommunityplatform.ui.home.bottembar.ReusableBottomNav
import com.dev.usbdigitalcommunityplatform.ui.home.common.NavItem
import com.dev.usbdigitalcommunityplatform.ui.home.member.MemberRoutes
import com.dev.usbdigitalcommunityplatform.ui.home.member.ProfileViewModel
import com.dev.usbdigitalcommunityplatform.ui.home.vendor.VendorRoutes
import com.dev.usbdigitalcommunityplatform.ui.theme.USBDigitalCommunityPlatformTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            USBDigitalCommunityPlatformTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val profileViewModel: ProfileViewModel = viewModel()
    val userRole = profileViewModel.userProfile.role

    // Routes where Bottom Nav should be visible — only Home + Profile now
    val memberScreens = listOf(
        MemberRoutes.HOME,
        MemberRoutes.PROFILE
    )
    val vendorScreens = listOf(
        VendorRoutes.HOME,
        VendorRoutes.PROFILE
    )

    val showBottomNav = currentRoute in memberScreens || currentRoute in vendorScreens
    val isVendor = userRole.equals("Vendor", ignoreCase = true) || currentRoute in vendorScreens

    // Only 2 tabs: Home and Profile — routes swap based on role
    val bottomNavItems = if (isVendor) {
        listOf(
            NavItem(route = VendorRoutes.HOME, title = "Home", icon = Icons.Filled.Home),
            NavItem(route = VendorRoutes.PROFILE, title = "Profile", icon = Icons.Filled.Person)
        )
    } else {
        listOf(
            NavItem(route = MemberRoutes.HOME, title = "Home", icon = Icons.Filled.Home),
            NavItem(route = MemberRoutes.PROFILE, title = "Profile", icon = Icons.Filled.Person)
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomNav) {
                Box(modifier = Modifier.padding(bottom = 16.dp)) {
                    ReusableBottomNav(
                        items = bottomNavItems,
                        currentRoute = currentRoute ?: "",
                        onItemClick = { route ->
                            if (currentRoute != route) {
                                navController.navigate(route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            AppNavGraph(navController = navController)
        }
    }
}