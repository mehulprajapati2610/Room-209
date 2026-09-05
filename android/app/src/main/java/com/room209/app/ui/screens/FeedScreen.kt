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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.room209.app.data.model.Post
import com.room209.app.data.model.PostCategory
import com.room209.app.data.repository.RoomRepository
import com.room209.app.ui.components.*
import com.room209.app.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun FeedScreen(
    repository: RoomRepository,
    onOpenShare: () -> Unit,
    onProfileClick: () -> Unit
) {
    val feed by repository.feed.collectAsState()
    val scope = rememberCoroutineScope()
    val roomId = repository.sessionManager.getRoomId()

    var selectedCategory by remember { mutableStateOf("ALL") }

    LaunchedEffect(selectedCategory) {
        val cat = when (selectedCategory) {
            "ANNOUNCEMENT" -> PostCategory.ANNOUNCEMENT
            "MAINTENANCE" -> PostCategory.MAINTENANCE
            "CHIT-CHAT" -> PostCategory.CHIT_CHAT
            else -> PostCategory.ALL
        }
        repository.refreshFeed(roomId, cat)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceCanvas)
    ) {
        // Top Header
        RoomTopBar(
            title = "COMMUNAL FEED",
            subtitle = "ROOM 209 CHRONICLES",
            onProfileClick = onProfileClick
        )

        // Category Filter Chips
        FilterChipsRow(
            categories = listOf("ALL", "ANNOUNCEMENT", "MAINTENANCE", "CHIT-CHAT"),
            selectedCategory = selectedCategory,
            onSelect = { selectedCategory = it },
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
        )

        // Feed Stream
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (feed.isEmpty()) {
                item {
                    EmptyStateCard(
                        title = "No Posts Yet",
                        subtitle = "Be the first to share an update, note, or announcement with Room 209.",
                        actionText = "WRITE UPDATE",
                        onAction = onOpenShare,
                        modifier = Modifier.padding(top = 20.dp)
                    )
                }
            } else {
                items(feed, key = { it.id }) { post ->
                    FeedPostCard(
                        post = post,
                        onLike = { scope.launch { repository.toggleLike(roomId, post.id) } }
                    )
                }
            }
        }
    }
}

@Composable
fun FeedPostCard(
    post: Post,
    onLike: () -> Unit
) {
    WarmCard {
        // Author Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertizontally,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertizontally) {
                // Author Initials Avatar
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(SurfaceSubtle)
                        .border(1.dp, BorderHairline, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = post.author.name.firstOrNull()?.toString()?.uppercase() ?: "R",
                        style = AppTypography.labelLarge,
                        color = TextPrimary
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = post.author.name,
                        style = AppTypography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    Text(
                        text = "${post.author.bedNumber ?: "Resident"} · ${post.createdAt?.take(10) ?: "Today"}",
                        style = AppTypography.bodySmall,
                        color = TextSecondary
                    )
                }
            }

            // Category Pill
            Box(
                modifier = Modifier
                    .clip(PillShape)
                    .background(AccentSurface)
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = post.category.name.replace("_", "-"),
                    style = LabelCaps.copy(fontSize = 9.sp),
                    color = AccentPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Content
        Text(
            text = post.content,
            style = AppTypography.bodyMedium,
            color = TextPrimary,
            lineHeight = 22.sp
        )

        // Cloudinary Media Image Attachment (if present)
        if (!post.mediaUrl.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(10.dp))
            AsyncImage(
                model = post.mediaUrl,
                contentDescription = "Post image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, BorderHairline, RoundedCornerShape(8.dp))
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Divider
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(BorderHairline)
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Reaction & Comment Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertizontally,
                modifier = Modifier.clickable { onLike() }
            ) {
                Text(
                    text = "AGREE",
                    style = LabelCaps.copy(fontSize = 10.sp),
                    color = if (post.likesCount > 0) AccentPrimary else TextMuted
                )
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .clip(PillShape)
                        .background(SurfaceSubtle)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "${post.likesCount}",
                        style = LabelCaps.copy(fontSize = 9.sp),
                        color = TextPrimary
                    )
                }
            }

            Text(
                text = "${post.commentsCount} REPLIES",
                style = LabelCaps.copy(fontSize = 10.sp),
                color = TextMuted
            )
        }
    }
}
