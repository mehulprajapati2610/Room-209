package com.room209.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.room209.app.data.model.PresenceStatus
import com.room209.app.data.repository.RoomRepository
import com.room209.app.ui.components.WarmButton
import com.room209.app.ui.components.WarmCard
import com.room209.app.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    repository: RoomRepository,
    onBack: () -> Unit,
    onLogout: () -> Unit
) {
    val currentUser = repository.sessionManager.getUser()
    val currentRoomNumber = repository.sessionManager.getRoomNumber()
    val roomId = repository.sessionManager.getRoomId()
    val scope = rememberCoroutineScope()

    var presenceStatus by remember { mutableStateOf(currentUser?.presenceStatus ?: PresenceStatus.IN_ROOM) }
    var quietHourAlerts by remember { mutableStateOf(true) }
    var choreAlerts by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceCanvas)
            .statusBarsPadding()
    ) {
        // Top Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertizontally
        ) {
            Text(
                text = "BACK",
                style = LabelCaps,
                color = AccentPrimary,
                modifier = Modifier.clickable { onBack() }
            )
            Text(
                text = "RESIDENT PROFILE",
                style = AppTypography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.width(36.dp))
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Profile Card
            item {
                WarmCard {
                    Row(verticalAlignment = Alignment.CenterVertizontally) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(AccentSurface)
                                .border(1.dp, AccentPrimary, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = currentUser?.name?.firstOrNull()?.toString()?.uppercase() ?: "R",
                                style = AppTypography.headlineMedium,
                                color = AccentPrimary
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column {
                            Text(
                                text = currentUser?.name ?: "Resident",
                                style = AppTypography.headlineSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )
                            Text(
                                text = "${currentUser?.roomRole ?: "Resident"} · ${currentUser?.bedNumber ?: "Bed 1"}",
                                style = AppTypography.bodySmall,
                                color = TextSecondary
                            )
                            Text(
                                text = "Assigned to Room $currentRoomNumber",
                                style = LabelCaps.copy(fontSize = 10.sp),
                                color = AccentPrimary
                            )
                        }
                    }
                }
            }

            // Live Presence Status Selector
            item {
                Text(
                    text = "SET YOUR LIVE PRESENCE",
                    style = LabelCaps,
                    color = TextMuted
                )
                Spacer(modifier = Modifier.height(8.dp))

                WarmCard {
                    val statuses = listOf(
                        PresenceStatus.IN_ROOM to "IN ROOM",
                        PresenceStatus.STUDYING_QUIET to "STUDYING / QUIET",
                        PresenceStatus.AWAY to "AWAY",
                        PresenceStatus.DO_NOT_DISTURB to "DO NOT DISTURB"
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        statuses.forEach { (status, label) ->
                            val isSelected = presenceStatus == status
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) AccentSurface else SurfaceSubtle)
                                    .border(
                                        1.dp,
                                        if (isSelected) AccentPrimary else BorderHairline,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .clickable {
                                        presenceStatus = status
                                        scope.launch {
                                            repository.updatePresence(roomId, status)
                                        }
                                    }
                                    .padding(horizontal = 14.dp, vertical = 10.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertizontally
                                ) {
                                    Text(
                                        text = label,
                                        style = AppTypography.bodyMedium,
                                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                        color = if (isSelected) AccentPrimary else TextPrimary
                                    )
                                    if (isSelected) {
                                        Box(
                                            modifier = Modifier
                                                .size(8.dp)
                                                .clip(CircleShape)
                                                .background(AccentPrimary)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Notification Settings
            item {
                Text(
                    text = "PREFERENCES",
                    style = LabelCaps,
                    color = TextMuted
                )
                Spacer(modifier = Modifier.height(8.dp))

                WarmCard {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertizontally
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Quiet Hours Alerts",
                                style = AppTypography.bodyMedium,
                                color = TextPrimary
                            )
                            Text(
                                text = "Notify at 11:00 PM when quiet hours start",
                                style = AppTypography.bodySmall,
                                color = TextSecondary
                            )
                        }
                        Switch(
                            checked = quietHourAlerts,
                            onCheckedChange = { quietHourAlerts = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = AccentPrimary
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertizontally
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Chore Duty Reminders",
                                style = AppTypography.bodyMedium,
                                color = TextPrimary
                            )
                            Text(
                                text = "Heads up when your scheduled duty is due",
                                style = AppTypography.bodySmall,
                                color = TextSecondary
                            )
                        }
                        Switch(
                            checked = choreAlerts,
                            onCheckedChange = { choreAlerts = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = AccentPrimary
                            )
                        )
                    }
                }
            }

            // Logout Action
            item {
                Spacer(modifier = Modifier.height(8.dp))
                WarmButton(
                    text = "SWITCH RESIDENT / LOGOUT",
                    onClick = onLogout
                )
            }
        }
    }
}
