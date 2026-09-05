package com.room209.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.room209.app.data.model.AuthRequest
import com.room209.app.data.repository.RoomRepository
import com.room209.app.ui.components.WarmButton
import com.room209.app.ui.components.WarmCard
import com.room209.app.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    repository: RoomRepository,
    onLoginSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("marcus@room209.internal") }
    var password by remember { mutableStateOf("pass123") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val demoResidents = listOf(
        Triple("marcus@room209.internal", "Marcus Reed", "Lead · Bed 1"),
        Triple("alex@room209.internal", "Alex Chen", "Resident · Bed 2"),
        Triple("dev@room209.internal", "Dev Patel", "Resident · Bed 3"),
        Triple("sam@room209.internal", "Sam Taylor", "Resident · Bed 4")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceCanvas)
            .statusBarsPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Room 209 Header Logo
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(AccentSurface)
                .border(1.5.dp, AccentPrimary, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "209",
                style = AppTypography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = AccentPrimary
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "ROOM 209",
            style = AppTypography.headlineLarge,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary
        )
        Text(
            text = "WARM MINIMALIST LIVING · PRIVATE RESIDENCE",
            style = LabelCaps.copy(fontSize = 10.sp),
            color = TextMuted
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Fast Resident Selector
        Text(
            text = "SELECT ROOMMATE PROFILE",
            style = LabelCaps,
            color = TextMuted
        )
        Spacer(modifier = Modifier.height(8.dp))

        WarmCard {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                demoResidents.forEach { (resEmail, name, role) ->
                    val isSelected = email == resEmail
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) AccentSurface else SurfaceSubtle)
                            .border(1.dp, if (isSelected) AccentPrimary else BorderHairline, RoundedCornerShape(8.dp))
                            .clickable {
                                email = resEmail
                                password = "pass123"
                            }
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = name,
                                    style = AppTypography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = role,
                                    style = AppTypography.bodySmall,
                                    color = TextSecondary
                                )
                            }
                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .clip(PillShape)
                                        .background(AccentPrimary)
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text("ACTIVE", style = LabelCaps.copy(fontSize = 9.sp), color = Color.White)
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Email & Password Fields
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Resident Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = errorMessage ?: "",
                style = AppTypography.bodySmall,
                color = ErrorColor
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        WarmButton(
            text = if (isLoading) "CONNECTING..." else "ENTER ROOM 209",
            enabled = email.isNotBlank() && password.isNotBlank() && !isLoading,
            onClick = {
                isLoading = true
                errorMessage = null
                scope.launch {
                    try {
                        val response = repository.apiClient.apiService.login(AuthRequest(email.trim(), password.trim()))
                        isLoading = false
                        if (response.isSuccessful && response.body() != null) {
                            val body = response.body()!!
                            repository.sessionManager.saveAuth(
                                token = body.token,
                                user = body.user,
                                roomId = body.roomId,
                                roomNumber = body.roomNumber
                            )
                            repository.initializeRealtime(body.roomId)
                            onLoginSuccess()
                        } else {
                            errorMessage = "Login failed: Invalid credentials or backend unavailable."
                        }
                    } catch (e: Exception) {
                        isLoading = false
                        // For offline/standalone testing, allow login if backend not booted yet
                        errorMessage = "Cannot reach backend: ${e.message}. Ensure backend is running."
                    }
                }
            }
        )
    }
}
