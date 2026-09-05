package com.room209.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.room209.app.data.model.Plan
import com.room209.app.data.model.RsvpStatus
import com.room209.app.data.repository.RoomRepository
import com.room209.app.ui.components.*
import com.room209.app.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun PlansScreen(
    repository: RoomRepository,
    onOpenActionSheet: () -> Unit,
    onProfileClick: () -> Unit
) {
    val plans by repository.plans.collectAsState()
    val scope = rememberCoroutineScope()
    val roomId = repository.sessionManager.getRoomId()

    LaunchedEffect(roomId) {
        repository.refreshPlans(roomId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceCanvas)
    ) {
        // Top Header
        RoomTopBar(
            title = "ROOM PLANS",
            subtitle = "SHARED TIMELINE & EVENTS",
            onProfileClick = onProfileClick
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            if (plans.isEmpty()) {
                item {
                    EmptyStateCard(
                        title = "No Upcoming Plans",
                        subtitle = "Group study sessions, weekend outings, and room dinners will appear here.",
                        actionText = "SCHEDULE EVENT",
                        onAction = onOpenActionSheet,
                        modifier = Modifier.padding(top = 20.dp)
                    )
                }
            } else {
                items(plans, key = { it.id }) { plan ->
                    PlanCard(
                        plan = plan,
                        onRsvp = { status ->
                            scope.launch { repository.updateRsvp(roomId, plan.id, status) }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun PlanCard(
    plan: Plan,
    onRsvp: (RsvpStatus) -> Unit
) {
    WarmCard {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertizontally
        ) {
            Text(
                text = plan.scheduledTime.uppercase(),
                style = LabelCaps,
                color = AccentPrimary
            )
            Box(
                modifier = Modifier
                    .clip(PillShape)
                    .background(AccentSurface)
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = plan.status,
                    style = LabelCaps.copy(fontSize = 9.sp),
                    color = AccentPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Title
        Text(
            text = plan.title,
            style = AppTypography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary
        )

        // Location
        if (!plan.location.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Location: ${plan.location}",
                style = AppTypography.bodySmall,
                color = TextSecondary
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Divider
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(BorderHairline)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // RSVP Buttons Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertizontally
        ) {
            Text(
                text = "ARE YOU GOING?",
                style = LabelCaps.copy(fontSize = 10.sp),
                color = TextMuted
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier
                        .clip(PillShape)
                        .background(AccentSurface)
                        .border(1.dp, AccentPrimary, PillShape)
                        .clickable { onRsvp(RsvpStatus.ATTENDING) }
                        .padding(horizontal = 12.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = "ATTENDING",
                        style = LabelCaps.copy(fontSize = 9.sp),
                        color = AccentPrimary
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(PillShape)
                        .background(SurfaceSubtle)
                        .border(1.dp, BorderHairline, PillShape)
                        .clickable { onRsvp(RsvpStatus.DECLINED) }
                        .padding(horizontal = 12.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = "DECLINE",
                        style = LabelCaps.copy(fontSize = 9.sp),
                        color = TextSecondary
                    )
                }
            }
        }
    }
}
