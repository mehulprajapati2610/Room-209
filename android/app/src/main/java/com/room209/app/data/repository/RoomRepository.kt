package com.room209.app.data.repository

import android.content.Context
import android.util.Log
import com.room209.app.data.SessionManager
import com.room209.app.data.api.ApiClient
import com.room209.app.data.model.*
import com.room209.app.data.websocket.RoomStompClient
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RoomRepository(context: Context) {

    private val tag = "RoomRepository"
    val sessionManager = SessionManager(context)
    val apiClient = ApiClient(context)
    val stompClient = RoomStompClient(apiClient.okHttpClient)
    private val scope = CoroutineScope(Dispatchers.IO)
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    // Reactive StateFlows
    private val _room = MutableStateFlow<Room?>(null)
    val room: StateFlow<Room?> = _room.asStateFlow()

    private val _roommates = MutableStateFlow<List<User>>(emptyList())
    val roommates: StateFlow<List<User>> = _roommates.asStateFlow()

    private val _feed = MutableStateFlow<List<Post>>(emptyList())
    val feed: StateFlow<List<Post>> = _feed.asStateFlow()

    private val _plans = MutableStateFlow<List<Plan>>(emptyList())
    val plans: StateFlow<List<Plan>> = _plans.asStateFlow()

    private val _chores = MutableStateFlow<List<Chore>>(emptyList())
    val chores: StateFlow<List<Chore>> = _chores.asStateFlow()

    private val _activePoll = MutableStateFlow<Poll?>(null)
    val activePoll: StateFlow<Poll?> = _activePoll.asStateFlow()

    init {
        // Listen to STOMP WebSocket messages
        scope.launch {
            stompClient.messages.collect { message ->
                handleIncomingWsMessage(message.destination, message.body)
            }
        }
    }

    fun initializeRealtime(roomId: Long) {
        stompClient.connect()
        stompClient.subscribe("/topic/room.$roomId.presence")
        stompClient.subscribe("/topic/room.$roomId.feed")
        stompClient.subscribe("/topic/room.$roomId.plans")
        stompClient.subscribe("/topic/room.$roomId.chores")
        stompClient.subscribe("/topic/room.$roomId.polls")
    }

    private fun handleIncomingWsMessage(destination: String, body: String) {
        Log.d(tag, "Incoming WS for $destination: $body")
        val roomId = sessionManager.getRoomId()
        scope.launch {
            when {
                destination.contains(".presence") -> {
                    refreshRoom(roomId)
                    refreshRoommates(roomId)
                }
                destination.contains(".feed") -> {
                    refreshFeed(roomId, null)
                }
                destination.contains(".plans") -> {
                    refreshPlans(roomId)
                }
                destination.contains(".chores") -> {
                    refreshChores(roomId)
                }
                destination.contains(".polls") -> {
                    refreshActivePoll(roomId)
                }
            }
        }
    }

    // Refresh Data from PostgreSQL via REST
    suspend fun refreshRoom(roomId: Long) {
        try {
            val response = apiClient.apiService.getRoomDetails(roomId)
            if (response.isSuccessful) {
                _room.value = response.body()
            }
        } catch (e: Exception) {
            Log.e(tag, "Failed to refresh room", e)
        }
    }

    suspend fun refreshRoommates(roomId: Long) {
        try {
            val response = apiClient.apiService.getRoommates(roomId)
            if (response.isSuccessful) {
                _roommates.value = response.body() ?: emptyList()
            }
        } catch (e: Exception) {
            Log.e(tag, "Failed to refresh roommates", e)
        }
    }

    suspend fun refreshFeed(roomId: Long, category: PostCategory?) {
        try {
            val response = apiClient.apiService.getFeed(roomId, category)
            if (response.isSuccessful) {
                _feed.value = response.body() ?: emptyList()
            }
        } catch (e: Exception) {
            Log.e(tag, "Failed to refresh feed", e)
        }
    }

    suspend fun refreshPlans(roomId: Long) {
        try {
            val response = apiClient.apiService.getPlans(roomId)
            if (response.isSuccessful) {
                _plans.value = response.body() ?: emptyList()
            }
        } catch (e: Exception) {
            Log.e(tag, "Failed to refresh plans", e)
        }
    }

    suspend fun refreshChores(roomId: Long) {
        try {
            val response = apiClient.apiService.getChores(roomId)
            if (response.isSuccessful) {
                _chores.value = response.body() ?: emptyList()
            }
        } catch (e: Exception) {
            Log.e(tag, "Failed to refresh chores", e)
        }
    }

    suspend fun refreshActivePoll(roomId: Long) {
        try {
            val response = apiClient.apiService.getActivePoll(roomId)
            if (response.isSuccessful) {
                _activePoll.value = response.body()
            }
        } catch (e: Exception) {
            Log.e(tag, "Failed to refresh active poll", e)
        }
    }

    // Action Mutations
    suspend fun updatePresence(roomId: Long, status: PresenceStatus): Boolean {
        return try {
            val response = apiClient.apiService.updatePresence(roomId, PresenceUpdateRequest(status))
            if (response.isSuccessful) {
                refreshRoommates(roomId)
                true
            } else false
        } catch (e: Exception) {
            false
        }
    }

    suspend fun createPost(roomId: Long, category: PostCategory, content: String, mediaUrl: String? = null): Boolean {
        return try {
            val response = apiClient.apiService.createPost(roomId, PostCreateRequest(category, content, mediaUrl))
            if (response.isSuccessful) {
                refreshFeed(roomId, null)
                true
            } else false
        } catch (e: Exception) {
            false
        }
    }

    suspend fun toggleLike(roomId: Long, postId: Long) {
        try {
            apiClient.apiService.toggleLike(roomId, postId)
            refreshFeed(roomId, null)
        } catch (e: Exception) {
            Log.e(tag, "Failed to toggle like", e)
        }
    }

    suspend fun createPlan(roomId: Long, title: String, description: String?, scheduledTime: String, location: String?): Boolean {
        return try {
            val response = apiClient.apiService.createPlan(roomId, PlanCreateRequest(title, description, scheduledTime, location))
            if (response.isSuccessful) {
                refreshPlans(roomId)
                true
            } else false
        } catch (e: Exception) {
            false
        }
    }

    suspend fun updateRsvp(roomId: Long, planId: Long, status: RsvpStatus) {
        try {
            apiClient.apiService.updateRsvp(roomId, planId, mapOf("status" to status.name))
            refreshPlans(roomId)
        } catch (e: Exception) {
            Log.e(tag, "Failed to update RSVP", e)
        }
    }

    suspend fun createChore(roomId: Long, title: String, description: String?, assignedToUserId: Long?, dueDate: String?, category: String): Boolean {
        return try {
            val response = apiClient.apiService.createChore(roomId, ChoreCreateRequest(title, description, assignedToUserId, dueDate, category))
            if (response.isSuccessful) {
                refreshChores(roomId)
                true
            } else false
        } catch (e: Exception) {
            false
        }
    }

    suspend fun toggleChore(roomId: Long, choreId: Long) {
        try {
            apiClient.apiService.toggleChore(roomId, choreId)
            refreshChores(roomId)
        } catch (e: Exception) {
            Log.e(tag, "Failed to toggle chore", e)
        }
    }

    suspend fun createPoll(roomId: Long, question: String, options: List<String>): Boolean {
        return try {
            val response = apiClient.apiService.createPoll(roomId, PollCreateRequest(question, options))
            if (response.isSuccessful) {
                refreshActivePoll(roomId)
                true
            } else false
        } catch (e: Exception) {
            false
        }
    }

    suspend fun vote(roomId: Long, pollId: Long, optionId: Long): Boolean {
        return try {
            val response = apiClient.apiService.vote(roomId, pollId, optionId)
            if (response.isSuccessful) {
                refreshActivePoll(roomId)
                true
            } else false
        } catch (e: Exception) {
            false
        }
    }
}
