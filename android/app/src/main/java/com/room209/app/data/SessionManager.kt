package com.room209.app.data

import android.content.Context
import android.content.SharedPreferences
import com.room209.app.data.model.User
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class SessionManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("room209_session", Context.MODE_PRIVATE)
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val userAdapter = moshi.adapter(User::class.java)

    companion object {
        private const val KEY_TOKEN = "jwt_token"
        private const val KEY_USER = "current_user"
        private const val KEY_ROOM_ID = "room_id"
        private const val KEY_ROOM_NUM = "room_num"
    }

    fun saveAuth(token: String, user: User, roomId: Long, roomNumber: String) {
        prefs.edit()
            .putString(KEY_TOKEN, token)
            .putString(KEY_USER, userAdapter.toJson(user))
            .putLong(KEY_ROOM_ID, roomId)
            .putString(KEY_ROOM_NUM, roomNumber)
            .apply()
    }

    fun getToken(): String? = prefs.getString(KEY_TOKEN, null)

    fun getUser(): User? {
        val json = prefs.getString(KEY_USER, null) ?: return null
        return try {
            userAdapter.fromJson(json)
        } catch (e: Exception) {
            null
        }
    }

    fun getRoomId(): Long = prefs.getLong(KEY_ROOM_ID, 1L)

    fun getRoomNumber(): String = prefs.getString(KEY_ROOM_NUM, "209") ?: "209"

    fun clear() {
        prefs.edit().clear().apply()
    }

    fun isLoggedIn(): Boolean = getToken() != null
}
