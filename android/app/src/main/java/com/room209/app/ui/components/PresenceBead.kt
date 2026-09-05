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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.room209.app.data.model.PresenceStatus
import com.room209.app.data.model.User
import com.room209.app.ui.theme.*

@Composable
fun PresenceBead(
    user: User,
    onClick: () -> Unit = {}
) {
    val statusColor = when (user.presenceStatus) {
        PresenceStatus.IN_ROOM -> PresenceInRoomColor
        PresenceStatus.STUDYING_QUIET -> PresenceStudyingColor
        PresenceStatus.AWAY -> PresenceAwayColor
        PresenceStatus.DO_NOT_DISTURB -> PresenceDNDColor
    }

    val statusText = when (user.presenceStatus) {
        PresenceStatus.IN_ROOM -> "IN ROOM"
        PresenceStatus.STUDYING_QUIET -> "QUIET"
        PresenceStatus.AWAY -> "AWAY"
        PresenceStatus.DO_NOT_DISTURB -> "DND"
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(76.dp)
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier.size(52.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            // Avatar Circle
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(SurfaceSubtle)
                    .border(1.dp, BorderHairline, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                val initials = user.name.split(" ")
                    .mapNotNull { it.firstOrNull()?.toString() }
                    .take(2)
                    .joinToString("")
                    .uppercase()

                Text(
                    text = if (initials.isNotEmpty()) initials else "R",
                    style = AppTypography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
            }

            // Presence Indicator Dot
            Box(
                modifier = Modifier
                    .size(14.dp)
                    .clip(CircleShape)
                    .background(SurfaceElevated)
                    .padding(2.dp)
                    .clip(CircleShape)
                    .background(statusColor)
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Name
        Text(
            text = user.name.split(" ").firstOrNull() ?: user.name,
            style = AppTypography.bodySmall,
            fontWeight = FontWeight.Medium,
            color = TextPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        // Bed Number or Status Pill
        Box(
            modifier = Modifier
                .padding(top = 2.dp)
                .background(AccentSurface, RoundedCornerShape(4.dp))
                .padding(horizontal = 4.dp, vertical = 1.dp)
        ) {
            Text(
                text = user.bedNumber ?: statusText,
                style = LabelCaps.copy(fontSize = 9.sp),
                color = AccentPrimary
            )
        }
    }
}
