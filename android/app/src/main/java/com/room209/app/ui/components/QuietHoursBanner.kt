package com.room209.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.room209.app.ui.theme.*

@Composable
fun QuietHoursBanner(
    isActiveNow: Boolean = false,
    startTime: String = "23:00",
    endTime: String = "07:00",
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceElevated)
            .border(1.dp, BorderHairline, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Moon Icon Well
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(AccentSurface),
                    contentAlignment = Alignment.Center
                ) {
                    IconMoon(color = AccentPrimary)
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "QUIET HOURS",
                        style = LabelCaps,
                        color = TextMuted
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "$startTime — $endTime",
                        style = AppTypography.headlineSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    Text(
                        text = if (isActiveNow) "In effect — please keep noise to a minimum" else "Automatic noise restriction mode",
                        style = AppTypography.bodySmall,
                        color = TextSecondary
                    )
                }
            }

            // Status Badge
            Box(
                modifier = Modifier
                    .clip(PillShape)
                    .background(if (isActiveNow) AccentSupportingLight else SurfaceSubtle)
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Text(
                    text = if (isActiveNow) "ACTIVE" else "STANDBY",
                    style = LabelCaps.copy(fontSize = 10.sp),
                    color = if (isActiveNow) AccentSupporting else TextMuted
                )
            }
        }
    }
}
