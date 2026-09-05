package com.room209.app.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Feed : Screen("feed")
    object ShareFeed : Screen("share_feed")
    object Plans : Screen("plans")
    object Fun : Screen("fun")
    object Profile : Screen("profile")
    object Login : Screen("login")
}
