package com.room209.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.room209.app.ui.navigation.Screen
import com.room209.app.ui.theme.*

@Composable
fun RoomTopBar(
    title: String = "ROOM 209",
    subtitle: String? = "SUITE 209 · PRIVATE RESIDENCE",
    onProfileClick: () -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceCanvas)
            .padding(horizontal = 20.dp, vertical = 14.dp)
    ) {
        Column {
            Text(
                text = title,
                style = AppTypography.headlineMedium,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = LabelCaps.copy(fontSize = 10.sp),
                    color = TextMuted
                )
            }
        }

        // Profile Avatar Action
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(SurfaceElevated)
                .border(1.dp, BorderHairline, CircleShape)
                .clickable { onProfileClick() },
            contentAlignment = Alignment.Center
        ) {
            IconProfile(color = TextSecondary, modifier = Modifier.size(18.dp))
        }
    }
}

@Composable
fun RoomBottomBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit,
    onOpenActionSheet: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceElevated)
            .border(width = 1.dp, color = BorderHairline, shape = RoundedCornerShape(0.dp))
            .navigationBarsPadding()
            .height(64.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            // 1. Home
            BottomNavItem(
                label = "HOME",
                isSelected = currentRoute == Screen.Home.route,
                onClick = { onNavigate(Screen.Home.route) },
                icon = { isSel -> IconHome(color = if (isSel) AccentPrimary else TextMuted) }
            )

            // 2. Feed
            BottomNavItem(
                label = "FEED",
                isSelected = currentRoute == Screen.Feed.route,
                onClick = { onNavigate(Screen.Feed.route) },
                icon = { isSel -> IconFeed(color = if (isSel) AccentPrimary else TextMuted) }
            )

            // 3. Central Action Sheet Button
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(AccentPrimary)
                    .clickable { onOpenActionSheet() }
            ) {
                IconPlus(color = Color.White)
            }

            // 4. Plans
            BottomNavItem(
                label = "PLANS",
                isSelected = currentRoute == Screen.Plans.route,
                onClick = { onNavigate(Screen.Plans.route) },
                icon = { isSel -> IconPlans(color = if (isSel) AccentPrimary else TextMuted) }
            )

            // 5. Fun
            BottomNavItem(
                label = "FUN",
                isSelected = currentRoute == Screen.Fun.route,
                onClick = { onNavigate(Screen.Fun.route) },
                icon = { isSel -> IconFun(color = if (isSel) AccentPrimary else TextMuted) }
            )
        }
    }
}

@Composable
private fun BottomNavItem(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    icon: @Composable (Boolean) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .width(58.dp)
            .fillMaxHeight()
            .clickable { onClick() }
    ) {
        icon(isSelected)
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = label,
            style = LabelCaps.copy(fontSize = 9.sp),
            color = if (isSelected) AccentPrimary else TextMuted
        )
    }
}
