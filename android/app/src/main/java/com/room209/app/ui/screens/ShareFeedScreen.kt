package com.room209.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.room209.app.data.model.PostCategory
import com.room209.app.data.repository.RoomRepository
import com.room209.app.ui.components.FilterChipsRow
import com.room209.app.ui.components.WarmButton
import com.room209.app.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun ShareFeedScreen(
    repository: RoomRepository,
    onDismiss: () -> Unit
) {
    var content by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("CHIT-CHAT") }
    var mediaUrl by remember { mutableStateOf("") }
    var isPosting by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val roomId = repository.sessionManager.getRoomId()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceElevated)
            .statusBarsPadding()
            .padding(20.dp)
    ) {
        // Top Action Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "CANCEL",
                style = LabelCaps,
                color = TextSecondary,
                modifier = Modifier.clickable { onDismiss() }
            )

            Text(
                text = "NEW ROOM POST",
                style = AppTypography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )

            Box(
                modifier = Modifier
                    .clip(PillShape)
                    .background(if (content.isNotBlank() && !isPosting) AccentPrimary else SurfaceSubtle)
                    .clickable(enabled = content.isNotBlank() && !isPosting) {
                        isPosting = true
                        val category = when (selectedCategory) {
                            "ANNOUNCEMENT" -> PostCategory.ANNOUNCEMENT
                            "MAINTENANCE" -> PostCategory.MAINTENANCE
                            else -> PostCategory.CHIT_CHAT
                        }
                        scope.launch {
                            val success = repository.createPost(
                                roomId = roomId,
                                category = category,
                                content = content.trim(),
                                mediaUrl = if (mediaUrl.isNotBlank()) mediaUrl.trim() else null
                            )
                            isPosting = false
                            if (success) {
                                onDismiss()
                            }
                        }
                    }
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text(
                    text = if (isPosting) "POSTING..." else "PUBLISH",
                    style = LabelCaps.copy(fontSize = 10.sp),
                    color = if (content.isNotBlank() && !isPosting) Color.White else TextMuted
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Category Selector Chips
        Text(
            text = "SELECT CATEGORY",
            style = LabelCaps,
            color = TextMuted
        )
        Spacer(modifier = Modifier.height(8.dp))
        FilterChipsRow(
            categories = listOf("CHIT-CHAT", "ANNOUNCEMENT", "MAINTENANCE"),
            selectedCategory = selectedCategory,
            onSelect = { selectedCategory = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Content Input Area
        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            placeholder = {
                Text(
                    text = "Write a message, room announcement, or reminder for Room 209...",
                    style = AppTypography.bodyLarge,
                    color = TextMuted
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(SurfaceElevated),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AccentPrimary,
                unfocusedBorderColor = BorderHairline,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
            ),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Optional Media / Cloudinary URL field
        OutlinedTextField(
            value = mediaUrl,
            onValueChange = { mediaUrl = it },
            placeholder = {
                Text(
                    text = "Optional: Paste image URL or Cloudinary asset link",
                    style = AppTypography.bodySmall,
                    color = TextMuted
                )
            },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AccentPrimary,
                unfocusedBorderColor = BorderHairline,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
            ),
            shape = RoundedCornerShape(10.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Publish Button
        WarmButton(
            text = if (isPosting) "PUBLISHING POST..." else "POST TO ROOM 209",
            enabled = content.isNotBlank() && !isPosting,
            onClick = {
                isPosting = true
                val category = when (selectedCategory) {
                    "ANNOUNCEMENT" -> PostCategory.ANNOUNCEMENT
                    "MAINTENANCE" -> PostCategory.MAINTENANCE
                    else -> PostCategory.CHIT_CHAT
                }
                scope.launch {
                    val success = repository.createPost(
                        roomId = roomId,
                        category = category,
                        content = content.trim(),
                        mediaUrl = if (mediaUrl.isNotBlank()) mediaUrl.trim() else null
                    )
                    isPosting = false
                    if (success) {
                        onDismiss()
                    }
                }
            }
        )
    }
}
