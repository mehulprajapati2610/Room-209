package com.room209.app.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.room209.app.data.repository.RoomRepository
import com.room209.app.ui.components.RoomBottomBar
import com.room209.app.ui.screens.*

@Composable
fun RoomNavGraph(
    repository: RoomRepository,
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    var showActionSheet by remember { mutableStateOf(false) }

    val startDestination = if (repository.sessionManager.isLoggedIn()) {
        Screen.Home.route
    } else {
        // Pre-initialize session for Room 209 so app opens directly as requested
        Screen.Home.route
    }

    LaunchedEffect(Unit) {
        val roomId = repository.sessionManager.getRoomId()
        repository.initializeRealtime(roomId)
    }

    val showBottomBar = currentRoute in listOf(
        Screen.Home.route,
        Screen.Feed.route,
        Screen.Plans.route,
        Screen.Fun.route
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar) {
                RoomBottomBar(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(Screen.Home.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onOpenActionSheet = { showActionSheet = true }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    repository = repository,
                    onNavigateToFeed = { navController.navigate(Screen.Feed.route) },
                    onNavigateToPlans = { navController.navigate(Screen.Plans.route) },
                    onOpenActionSheet = { showActionSheet = true },
                    onProfileClick = { navController.navigate(Screen.Profile.route) }
                )
            }

            composable(Screen.Feed.route) {
                FeedScreen(
                    repository = repository,
                    onOpenShare = { navController.navigate(Screen.ShareFeed.route) },
                    onProfileClick = { navController.navigate(Screen.Profile.route) }
                )
            }

            composable(Screen.ShareFeed.route) {
                ShareFeedScreen(
                    repository = repository,
                    onDismiss = { navController.popBackStack() }
                )
            }

            composable(Screen.Plans.route) {
                PlansScreen(
                    repository = repository,
                    onOpenActionSheet = { showActionSheet = true },
                    onProfileClick = { navController.navigate(Screen.Profile.route) }
                )
            }

            composable(Screen.Fun.route) {
                FunScreen(
                    repository = repository,
                    onOpenActionSheet = { showActionSheet = true },
                    onProfileClick = { navController.navigate(Screen.Profile.route) }
                )
            }

            composable(Screen.Profile.route) {
                ProfileScreen(
                    repository = repository,
                    onBack = { navController.popBackStack() },
                    onLogout = {
                        repository.sessionManager.clear()
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.Login.route) {
                LoginScreen(
                    repository = repository,
                    onLoginSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                )
            }
        }

        if (showActionSheet) {
            CreateActionSheet(
                repository = repository,
                onDismiss = { showActionSheet = false },
                onOpenShareFeed = {
                    navController.navigate(Screen.ShareFeed.route)
                }
            )
        }
    }
}
