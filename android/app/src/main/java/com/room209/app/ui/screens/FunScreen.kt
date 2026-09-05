package com.room209.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.room209.app.data.model.Poll
import com.room209.app.data.model.PollOption
import com.room209.app.data.repository.RoomRepository
import com.room209.app.ui.components.*
import com.room209.app.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun FunScreen(
    repository: RoomRepository,
    onOpenActionSheet: () -> Unit,
    onProfileClick: () -> Unit
) {
    val activePoll by repository.activePoll.collectAsState()
    val scope = rememberCoroutineScope()
    val roomId = repository.sessionManager.getRoomId()

    LaunchedEffect(roomId) {
        repository.refreshActivePoll(roomId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceCanvas)
    ) {
        RoomTopBar(
            title = "LOUNGE & FUN",
            subtitle = "ROOM POLLS & CASUAL CHATTER",
            onProfileClick = onProfileClick
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Active Poll Section
            item {
                Text(
                    text = "ACTIVE ROOM POLL",
                    style = LabelCaps,
                    color = TextMuted
                )
            }

            item {
                val poll = activePoll
                if (poll == null) {
                    EmptyStateCard(
                        title = "No Active Polls",
                        subtitle = "Ask Room 209 anything — weekend dinner plans, movie picks, or room rules.",
                        actionText = "START A POLL",
                        onAction = onOpenActionSheet
                    )
                } else {
                    PollCard(
                        poll = poll,
                        onVote = { optionId ->
                            scope.launch { repository.vote(roomId, poll.id, optionId) }
                        }
                    )
                }
            }

            // Casual Room Rules / Quote Board Tile
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "ROOM 209 PROTOCOL",
                    style = LabelCaps,
                    color = TextMuted
                )
                Spacer(modifier = Modifier.height(8.dp))
                WarmCard {
                    Text(
                        text = "RESIDENCE PRINCIPLES",
                        style = LabelCaps,
                        color = AccentPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "1. Respect Quiet Hours (11:00 PM – 7:00 AM)\n2. Dishes cleared within 2 hours\n3. Notify roommates 24h prior to overnight visitors",
                        style = AppTypography.bodyMedium,
                        color = TextPrimary,
                        lineHeight = 22.sp
                    )
                }
            }
        }
    }
}

@Composable
fun PollCard(
    poll: Poll,
    onVote: (Long) -> Unit
) {
    WarmCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "COMMUNAL VOTE",
                style = LabelCaps,
                color = AccentPrimary
            )
            Text(
                text = "${poll.totalVotes} VOTES",
                style = LabelCaps.copy(fontSize = 10.sp),
                color = TextMuted
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = poll.question,
            style = AppTypography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Poll Options
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            poll.options.forEach { option ->
                val isSelected = poll.userVotedOptionId == option.id

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSelected) AccentSurface else SurfaceSubtle)
                        .border(
                            1.dp,
                            if (isSelected) AccentPrimary else BorderHairline,
                            RoundedCornerShape(10.dp)
                        )
                        .clickable(enabled = !poll.hasVoted) {
                            onVote(option.id)
                        }
                        .padding(12.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = option.optionText,
                                style = AppTypography.bodyMedium,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                color = TextPrimary
                            )
                            Text(
                                text = "${option.percentage}%",
                                style = LabelCaps.copy(fontSize = 11.sp),
                                color = if (isSelected) AccentPrimary else TextSecondary
                            )
                        }

                        if (poll.hasVoted) {
                            Spacer(modifier = Modifier.height(6.dp))
                            LinearProgressIndicator(
                                progress = { option.percentage / 100f },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(4.dp)
                                    .clip(PillShape),
                                color = AccentPrimary,
                                trackColor = BorderHairline
                            )
                        }
                    }
                }
            }
        }
    }
}
