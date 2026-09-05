package com.room209.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.room209.app.data.model.PresenceStatus
import com.room209.app.data.model.User
import com.room209.app.data.repository.RoomRepository
import com.room209.app.ui.navigation.RoomNavGraph
import com.room209.app.ui.theme.Room209Theme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val repository = RoomRepository(applicationContext)

        // Ensure Room 209 default resident is initialized if opening for the first time
        if (repository.sessionManager.getUser() == null) {
            val defaultUser = User(
                id = 1L,
                email = "marcus@room209.internal",
                name = "Marcus Reed",
                bedNumber = "Bed 1",
                roomRole = "Room Lead",
                presenceStatus = PresenceStatus.IN_ROOM
            )
            repository.sessionManager.saveAuth(
                token = "pre_auth_token",
                user = defaultUser,
                roomId = 1L,
                roomNumber = "209"
            )
        }

        setContent {
            Room209Theme {
                RoomNavGraph(repository = repository)
            }
        }
    }
}
