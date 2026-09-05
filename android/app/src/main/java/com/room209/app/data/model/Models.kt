package com.room209.app.data.model

import com.squareup.moshi.JsonClass

enum class PresenceStatus {
    IN_ROOM,
    AWAY,
    STUDYING_QUIET,
    DO_NOT_DISTURB
}

enum class PostCategory {
    ALL,
    ANNOUNCEMENT,
    MAINTENANCE,
    CHIT_CHAT
}

enum class RsvpStatus {
    ATTENDING,
    TENTATIVE,
    DECLINED
}

@JsonClass(generateAdapter = true)
data class User(
    val id: Long,
    val email: String,
    val name: String,
    val bedNumber: String? = null,
    val roomRole: String? = null,
    val presenceStatus: PresenceStatus = PresenceStatus.IN_ROOM,
    val avatarUrl: String? = null
)

@JsonClass(generateAdapter = true)
data class Room(
    val id: Long,
    val roomNumber: String,
    val name: String,
    val quietHoursStart: String? = null,
    val quietHoursEnd: String? = null,
    val quietHoursEnabled: Boolean = true,
    val isQuietHoursActiveNow: Boolean = false,
    val members: List<User> = emptyList()
)

@JsonClass(generateAdapter = true)
data class Post(
    val id: Long,
    val author: User,
    val category: PostCategory,
    val content: String,
    val mediaUrl: String? = null,
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val createdAt: String? = null
)

@JsonClass(generateAdapter = true)
data class Comment(
    val id: Long,
    val author: User,
    val content: String,
    val createdAt: String? = null
)

@JsonClass(generateAdapter = true)
data class PlanRsvp(
    val userId: Long,
    val userName: String,
    val status: RsvpStatus
)

@JsonClass(generateAdapter = true)
data class Plan(
    val id: Long,
    val title: String,
    val description: String? = null,
    val scheduledTime: String,
    val location: String? = null,
    val status: String = "UPCOMING",
    val createdBy: User,
    val rsvps: List<PlanRsvp> = emptyList()
)

@JsonClass(generateAdapter = true)
data class Chore(
    val id: Long,
    val title: String,
    val description: String? = null,
    val assignedTo: User? = null,
    val dueDate: String? = null,
    val completed: Boolean = false,
    val category: String = "CLEANING"
)

@JsonClass(generateAdapter = true)
data class PollOption(
    val id: Long,
    val optionText: String,
    val voteCount: Int = 0,
    val percentage: Int = 0
)

@JsonClass(generateAdapter = true)
data class Poll(
    val id: Long,
    val question: String,
    val createdBy: User,
    val active: Boolean = true,
    val createdAt: String? = null,
    val totalVotes: Int = 0,
    val options: List<PollOption> = emptyList(),
    val hasVoted: Boolean = false,
    val userVotedOptionId: Long? = null
)

// Network DTOs
@JsonClass(generateAdapter = true)
data class AuthRequest(val email: String, val password: String)

@JsonClass(generateAdapter = true)
data class RegisterRequest(
    val email: String,
    val password: String,
    val name: String,
    val bedNumber: String = "Bed",
    val roomRole: String = "Resident"
)

@JsonClass(generateAdapter = true)
data class AuthResponse(
    val token: String,
    val user: User,
    val roomId: Long,
    val roomNumber: String
)

@JsonClass(generateAdapter = true)
data class PostCreateRequest(
    val category: PostCategory,
    val content: String,
    val mediaUrl: String? = null
)

@JsonClass(generateAdapter = true)
data class CommentRequest(val content: String)

@JsonClass(generateAdapter = true)
data class PlanCreateRequest(
    val title: String,
    val description: String? = null,
    val scheduledTime: String,
    val location: String? = null
)

@JsonClass(generateAdapter = true)
data class ChoreCreateRequest(
    val title: String,
    val description: String? = null,
    val assignedToUserId: Long? = null,
    val dueDate: String? = null,
    val category: String = "CLEANING"
)

@JsonClass(generateAdapter = true)
data class PollCreateRequest(
    val question: String,
    val options: List<String>
)

@JsonClass(generateAdapter = true)
data class PresenceUpdateRequest(
    val status: PresenceStatus
)
