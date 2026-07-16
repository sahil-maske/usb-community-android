package com.dev.usbdigitalcommunityplatform.ui.home.bottembar

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dev.usbdigitalcommunityplatform.ui.home.common.NavItem

private val SelectedColor = Color(0xFF007AFF)
private val UnselectedColor = Color(0xFF8E8E93)

@Composable
fun ReusableBottomNav(
    items: List<NavItem>,
    currentRoute: String,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(28.dp),
                ambientColor = Color.Black.copy(alpha = 0.12f),
                spotColor = Color.Black.copy(alpha = 0.12f)
            )
            .clip(RoundedCornerShape(28.dp))
            .background(Color.White.copy(alpha = 0.92f))
            .height(64.dp)
            .padding(horizontal = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEach { item ->
            val selected = currentRoute == item.route
            NavItemContent(
                item = item,
                selected = selected,
                onClick = { onItemClick(item.route) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun NavItemContent(
    item: NavItem,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val iconColor by animateColorAsState(
        targetValue = if (selected) SelectedColor else UnselectedColor,
        label = "iconColor"
    )

    val scale by animateFloatAsState(
        targetValue = if (selected) 1.08f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "iconScale"
    )

    Column(
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(if (selected) SelectedColor.copy(alpha = 0.12f) else Color.Transparent)
                .padding(horizontal = if (selected) 14.dp else 10.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.title,
                    tint = iconColor,
                    modifier = Modifier
                        .size(22.dp)
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                        }
                )

                if (selected) {
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = item.title,
                        color = iconColor,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

// ---------- Preview ----------

@Preview(showBackground = true, backgroundColor = 0xFFF2F2F7)
@Composable
private fun ReusableBottomNavPreview() {
    val items = listOf(
        NavItem(route = "home", title = "Home", icon = Icons.Filled.Home),
        NavItem(route = "profile", title = "Profile", icon = Icons.Filled.Person)
    )

    var currentRoute by remember { mutableStateOf("home") }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        ReusableBottomNav(
            items = items,
            currentRoute = currentRoute,
            onItemClick = { currentRoute = it }
        )
    }
}