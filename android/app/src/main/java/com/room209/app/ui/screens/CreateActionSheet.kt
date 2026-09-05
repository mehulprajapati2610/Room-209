package com.room209.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.room209.app.data.repository.RoomRepository
import com.room209.app.ui.components.*
import com.room209.app.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateActionSheet(
    repository: RoomRepository,
    onDismiss: () -> Unit,
    onOpenShareFeed: () -> Unit
) {
    var showChoreDialog by remember { mutableStateOf(false) }
    var showPlanDialog by remember { mutableStateOf(false) }
    var showPollDialog by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        shape = BottomSheetShape,
        containerColor = SurfaceElevated,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .width(36.dp)
                    .height(4.dp)
                    .clip(PillShape)
                    .background(BorderStrong)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 36.dp)
        ) {
            Text(
                text = "SUITE 209",
                style = LabelCaps,
                color = TextMuted
            )
            Text(
                text = "Create Room Action",
                style = AppTypography.headlineMedium,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            Text(
                text = "Add tasks, share updates, or schedule events for the room.",
                style = AppTypography.bodySmall,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 1. Post to Feed
            ActionTile(
                title = "Post to Feed",
                subtitle = "Share announcements, photos, or room updates",
                badgeText = "FEED",
                onClick = {
                    onDismiss()
                    onOpenShareFeed()
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 2. Assign Chore
            ActionTile(
                title = "Assign Room Chore",
                subtitle = "Rotate cleaning, trash, or maintenance duties",
                badgeText = "CHORES",
                onClick = { showChoreDialog = true }
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 3. Schedule Plan
            ActionTile(
                title = "Schedule Room Plan",
                subtitle = "Set up study sessions, grocery runs, or group outings",
                badgeText = "PLANS",
                onClick = { showPlanDialog = true }
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 4. Create Poll
            ActionTile(
                title = "Create Room Poll",
                subtitle = "Vote on weekend movies, dinners, or room rules",
                badgeText = "FUN",
                onClick = { showPollDialog = true }
            )
        }
    }

    // Dialogs
    if (showChoreDialog) {
        CreateChoreDialog(
            repository = repository,
            onDismiss = {
                showChoreDialog = false
                onDismiss()
            }
        )
    }

    if (showPlanDialog) {
        CreatePlanDialog(
            repository = repository,
            onDismiss = {
                showPlanDialog = false
                onDismiss()
            }
        )
    }

    if (showPollDialog) {
        CreatePollDialog(
            repository = repository,
            onDismiss = {
                showPollDialog = false
                onDismiss()
            }
        )
    }
}

@Composable
private fun ActionTile(
    title: String,
    subtitle: String,
    badgeText: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceElevated)
            .border(1.dp, BorderHairline, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = AppTypography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
                Text(
                    text = subtitle,
                    style = AppTypography.bodySmall,
                    color = TextSecondary
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .clip(PillShape)
                    .background(AccentSurface)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = badgeText,
                    style = LabelCaps.copy(fontSize = 10.sp),
                    color = AccentPrimary
                )
            }
        }
    }
}

@Composable
fun CreateChoreDialog(
    repository: RoomRepository,
    onDismiss: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("CLEANING") }
    var dueDate by remember { mutableStateOf("This Friday") }
    val scope = rememberCoroutineScope()
    val roomId = repository.sessionManager.getRoomId()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Assign Room Chore", style = AppTypography.headlineSmall, color = TextPrimary) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Chore Title (e.g. Clean Kitchen Counter)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Category (CLEANING, TRASH, BATHROOM)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = dueDate,
                    onValueChange = { dueDate = it },
                    label = { Text("Due Date / Day") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            WarmButton(
                text = "SAVE CHORE",
                enabled = title.isNotBlank(),
                onClick = {
                    scope.launch {
                        repository.createChore(
                            roomId = roomId,
                            title = title.trim(),
                            description = null,
                            assignedToUserId = null,
                            dueDate = dueDate.trim(),
                            category = category.trim().uppercase()
                        )
                        onDismiss()
                    }
                }
            )
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("CANCEL", color = TextMuted, style = LabelCaps)
            }
        }
    )
}

@Composable
fun CreatePlanDialog(
    repository: RoomRepository,
    onDismiss: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("Saturday 7:00 PM") }
    var location by remember { mutableStateOf("Hostel Lounge") }
    val scope = rememberCoroutineScope()
    val roomId = repository.sessionManager.getRoomId()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Schedule Room Plan", style = AppTypography.headlineSmall, color = TextPrimary) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Event Title (e.g. Weekend Grocery Run)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = time,
                    onValueChange = { time = it },
                    label = { Text("Scheduled Time") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Location") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            WarmButton(
                text = "SCHEDULE PLAN",
                enabled = title.isNotBlank(),
                onClick = {
                    scope.launch {
                        repository.createPlan(
                            roomId = roomId,
                            title = title.trim(),
                            description = null,
                            scheduledTime = time.trim(),
                            location = location.trim()
                        )
                        onDismiss()
                    }
                }
            )
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("CANCEL", color = TextMuted, style = LabelCaps)
            }
        }
    )
}

@Composable
fun CreatePollDialog(
    repository: RoomRepository,
    onDismiss: () -> Unit
) {
    var question by remember { mutableStateOf("") }
    var option1 by remember { mutableStateOf("") }
    var option2 by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val roomId = repository.sessionManager.getRoomId()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create Room Poll", style = AppTypography.headlineSmall, color = TextPrimary) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = question,
                    onValueChange = { question = it },
                    label = { Text("Question (e.g. Dinner Tonight?)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = option1,
                    onValueChange = { option1 = it },
                    label = { Text("Option 1") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = option2,
                    onValueChange = { option2 = it },
                    label = { Text("Option 2") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            WarmButton(
                text = "START POLL",
                enabled = question.isNotBlank() && option1.isNotBlank() && option2.isNotBlank(),
                onClick = {
                    scope.launch {
                        repository.createPoll(
                            roomId = roomId,
                            question = question.trim(),
                            options = listOf(option1.trim(), option2.trim())
                        )
                        onDismiss()
                    }
                }
            )
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("CANCEL", color = TextMuted, style = LabelCaps)
            }
        }
    )
}
