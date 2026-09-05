package com.room209.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.room209.app.data.model.Chore
import com.room209.app.data.model.Post
import com.room209.app.data.model.PostCategory
import com.room209.app.data.repository.RoomRepository
import com.room209.app.ui.components.*
import com.room209.app.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    repository: RoomRepository,
    onNavigateToFeed: () -> Unit,
    onNavigateToPlans: () -> Unit,
    onOpenActionSheet: () -> Unit,
    onProfileClick: () -> Unit
) {
    val room by repository.room.collectAsState()
    val roommates by repository.roommates.collectAsState()
    val feed by repository.feed.collectAsState()
    val chores by repository.chores.collectAsState()
    val scope = rememberCoroutineScope()

    val roomId = repository.sessionManager.getRoomId()

    LaunchedEffect(roomId) {
        repository.refreshRoom(roomId)
        repository.refreshRoommates(roomId)
        repository.refreshFeed(roomId, null)
        repository.refreshChores(roomId)
    }

    val announcements = remember(feed) {
        feed.filter { it.category == PostCategory.ANNOUNCEMENT }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceCanvas),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        // 1. Top Bar
        item {
            RoomTopBar(
                title = room?.name ?: "ROOM 209",
                subtitle = "SUITE 209 · PRIVATE RESIDENCE",
                onProfileClick = onProfileClick
            )
        }

        // 2. Roommates Presence Section
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "RESIDENTS (${roommates.size})",
                        style = LabelCaps,
                        color = TextMuted
                    )
                    Text(
                        text = "TAP TO VIEW",
                        style = LabelCaps.copy(fontSize = 10.sp),
                        color = AccentPrimary
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (roommates.isEmpty()) {
                        // Fallback resident avatars
                        Text(
                            text = "Loading residents...",
                            style = AppTypography.bodySmall,
                            color = TextMuted,
                            modifier = Modifier.padding(16.dp)
                        )
                    } else {
                        roommates.forEach { resident ->
                            PresenceBead(
                                user = resident,
                                onClick = onProfileClick
                            )
                        }
                    }
                }
            }
        }

        // 3. Quiet Hours Banner
        item {
            Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)) {
                QuietHoursBanner(
                    isActiveNow = room?.isQuietHoursActiveNow ?: false,
                    startTime = room?.quietHoursStart ?: "23:00",
                    endTime = room?.quietHoursEnd ?: "07:00"
                )
            }
        }

        // 4. Urgent Announcements
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "ANNOUNCEMENTS",
                        style = LabelCaps,
                        color = TextMuted
                    )
                    if (announcements.isNotEmpty()) {
                        Text(
                            text = "VIEW FEED",
                            style = LabelCaps.copy(fontSize = 10.sp),
                            color = AccentPrimary,
                            modifier = Modifier.clickable { onNavigateToFeed() }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (announcements.isEmpty()) {
                    EmptyStateCard(
                        title = "No Announcements",
                        subtitle = "Room notices and urgent alerts will appear here.",
                        actionText = "POST NOTICE",
                        onAction = onOpenActionSheet
                    )
                } else {
                    announcements.take(2).forEach { post ->
                        WarmCard(modifier = Modifier.padding(bottom = 8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = post.author.name.uppercase(),
                                    style = LabelCaps.copy(fontSize = 10.sp),
                                    color = AccentPrimary
                                )
                                Text(
                                    text = post.createdAt?.take(10) ?: "RECENT",
                                    style = LabelCaps.copy(fontSize = 9.sp),
                                    color = TextMuted
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = post.content,
                                style = AppTypography.bodyMedium,
                                color = TextPrimary
                            )
                        }
                    }
                }
            }
        }

        // 5. Chores & Duties Snapshot
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "CHORES & DUTIES",
                        style = LabelCaps,
                        color = TextMuted
                    )
                    Text(
                        text = "ADD TASK",
                        style = LabelCaps.copy(fontSize = 10.sp),
                        color = AccentPrimary,
                        modifier = Modifier.clickable { onOpenActionSheet() }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (chores.isEmpty()) {
                    EmptyStateCard(
                        title = "All Caught Up",
                        subtitle = "No chores assigned. Tap + to set up room duties.",
                        actionText = "ASSIGN CHORE",
                        onAction = onOpenActionSheet
                    )
                } else {
                    chores.forEach { chore ->
                        WarmCard(modifier = Modifier.padding(bottom = 8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    // Checkbox box
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .size(22.dp)
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(if (chore.completed) AccentPrimary else SurfaceElevated)
                                            .border(
                                                1.5.dp,
                                                if (chore.completed) AccentPrimary else BorderStrong,
                                                RoundedCornerShape(6.dp)
                                            )
                                            .clickable {
                                                scope.launch { repository.toggleChore(roomId, chore.id) }
                                            }
                                    ) {
                                        if (chore.completed) {
                                            IconCheck(color = Color.White, modifier = Modifier.size(14.dp))
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Column {
                                        Text(
                                            text = chore.title,
                                            style = AppTypography.bodyMedium,
                                            fontWeight = FontWeight.Medium,
                                            color = if (chore.completed) TextMuted else TextPrimary
                                        )
                                        Text(
                                            text = "Assigned: ${chore.assignedTo?.name ?: "Unassigned"} · Due ${chore.dueDate ?: "Soon"}",
                                            style = AppTypography.bodySmall,
                                            color = TextSecondary
                                        )
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .clip(PillShape)
                                        .background(AccentSurface)
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    Text(
                                        text = chore.category,
                                        style = LabelCaps.copy(fontSize = 9.sp),
                                        color = AccentPrimary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
