package com.room209.app.data.api

import com.room209.app.data.model.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface RoomApiService {

    // Auth
    @POST("auth/login")
    suspend fun login(@Body request: AuthRequest): Response<AuthResponse>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @GET("auth/me")
    suspend fun getCurrentUser(): Response<User>

    // Room
    @GET("rooms/{roomId}")
    suspend fun getRoomDetails(@Path("roomId") roomId: Long): Response<Room>

    @GET("rooms/number/{roomNumber}")
    suspend fun getRoomByNumber(@Path("roomNumber") roomNumber: String): Response<Room>

    @GET("rooms/{roomId}/roommates")
    suspend fun getRoommates(@Path("roomId") roomId: Long): Response<List<User>>

    @PUT("rooms/{roomId}/presence")
    suspend fun updatePresence(
        @Path("roomId") roomId: Long,
        @Body request: PresenceUpdateRequest
    ): Response<User>

    @PUT("rooms/fcm-token")
    suspend fun updateFcmToken(@Body body: Map<String, String>): Response<Unit>

    // Feed
    @GET("rooms/{roomId}/posts")
    suspend fun getFeed(
        @Path("roomId") roomId: Long,
        @Query("category") category: PostCategory? = null
    ): Response<List<Post>>

    @POST("rooms/{roomId}/posts")
    suspend fun createPost(
        @Path("roomId") roomId: Long,
        @Body request: PostCreateRequest
    ): Response<Post>

    @GET("rooms/{roomId}/posts/{postId}/comments")
    suspend fun getComments(
        @Path("roomId") roomId: Long,
        @Path("postId") postId: Long
    ): Response<List<Comment>>

    @POST("rooms/{roomId}/posts/{postId}/comments")
    suspend fun addComment(
        @Path("roomId") roomId: Long,
        @Path("postId") postId: Long,
        @Body request: CommentRequest
    ): Response<Comment>

    @POST("rooms/{roomId}/posts/{postId}/like")
    suspend fun toggleLike(
        @Path("roomId") roomId: Long,
        @Path("postId") postId: Long
    ): Response<Post>

    // Plans
    @GET("rooms/{roomId}/plans")
    suspend fun getPlans(@Path("roomId") roomId: Long): Response<List<Plan>>

    @POST("rooms/{roomId}/plans")
    suspend fun createPlan(
        @Path("roomId") roomId: Long,
        @Body request: PlanCreateRequest
    ): Response<Plan>

    @PUT("rooms/{roomId}/plans/{planId}/rsvp")
    suspend fun updateRsvp(
        @Path("roomId") roomId: Long,
        @Path("planId") planId: Long,
        @Body body: Map<String, String>
    ): Response<Plan>

    // Chores
    @GET("rooms/{roomId}/chores")
    suspend fun getChores(@Path("roomId") roomId: Long): Response<List<Chore>>

    @POST("rooms/{roomId}/chores")
    suspend fun createChore(
        @Path("roomId") roomId: Long,
        @Body request: ChoreCreateRequest
    ): Response<Chore>

    @PATCH("rooms/{roomId}/chores/{choreId}/toggle")
    suspend fun toggleChore(
        @Path("roomId") roomId: Long,
        @Path("choreId") choreId: Long
    ): Response<Chore>

    // Polls
    @GET("rooms/{roomId}/polls/active")
    suspend fun getActivePoll(@Path("roomId") roomId: Long): Response<Poll?>

    @POST("rooms/{roomId}/polls")
    suspend fun createPoll(
        @Path("roomId") roomId: Long,
        @Body request: PollCreateRequest
    ): Response<Poll>

    @POST("rooms/{roomId}/polls/{pollId}/options/{optionId}/vote")
    suspend fun vote(
        @Path("roomId") roomId: Long,
        @Path("pollId") pollId: Long,
        @Path("optionId") optionId: Long
    ): Response<Poll>

    // Media
    @Multipart
    @POST("media/upload")
    suspend fun uploadMedia(
        @Part file: MultipartBody.Part,
        @Query("folder") folder: String = "feed"
    ): Response<Map<String, String>>
}
